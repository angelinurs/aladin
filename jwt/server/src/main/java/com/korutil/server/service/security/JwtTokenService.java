package com.korutil.server.service.security;

import com.korutil.server.component.jwt.CustomAuthenticationToken;
import com.korutil.server.component.jwt.JwtProvider;
import com.korutil.server.repository.dao.user.JwtTokenDao;
import com.korutil.server.repository.dao.user.SocialLoginDao;
import com.korutil.server.repository.dao.user.UserDao;
import com.korutil.server.dto.jwt.JwtTokenDto;
import com.korutil.server.dto.user.record.LogInOutCommonRecord;
import com.korutil.server.dto.user.UserDto;
import com.korutil.server.handler.CustomRuntimeException;
import com.korutil.server.handler.ErrorCode;
import com.korutil.server.repository.jpa.user.SocialLoginRepository;
import com.korutil.server.util.PasswordUtils;
import com.korutil.server.util.RequestContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtTokenService {
    private final SocialLoginDao socialLoginDao;
    private final SocialLoginRepository socialLoginRepository;

    private final JwtProvider jwtProvider;
    private final CustomUserDetailsService customUserDetailsService;

    private final UserDao userDao;
    private final JwtTokenDao jwtTokenDao;

    // Refresh Token 갱신
    public void saveAccessToken( JwtTokenDto jwtTokenDto) {
        // Refresh Token을 저장
        UserDto userDto = userDao.getUserByUsername(jwtTokenDto.getUsername());
        jwtTokenDto.setUserId(userDto.getId());
        jwtTokenDto.setClientIp(RequestContext.getClientIp());
        jwtTokenDao.saveAccessToken( jwtTokenDto );
    }

    // Refresh Token 갱신
    public void updateJwtToken(JwtTokenDto jwtTokenDto) {
        // Refresh Token을 저장
        UserDto userDto = userDao.getUserByUsername(jwtTokenDto.getUsername());
        jwtTokenDto.setUserId(userDto.getId());
        jwtTokenDao.updateToken( jwtTokenDto );
    }

    // Refresh Token 유효성 체크
    public boolean isValidRefreshToken(String refreshToken) {

        Jwt jwt = jwtProvider.decodeToken(refreshToken);
        UserDetails userDetails = jwtProvider.getUserDetails( jwt );
        String secretKey = jwtProvider.getUserSecret(jwt);
        String userAgent = jwtProvider.getUserAgent(jwt);

        UserDto userDto = userDao.getUserByUsername(userDetails.getUsername());
        JwtTokenDto tokenDto = jwtTokenDao.getRefreshToken( userDto.getId(), userAgent, secretKey );

        return tokenDto.isEqualRefreshToken(refreshToken);
    }

    public JwtTokenDto deActivateToken(String token) {
        Jwt jwt = jwtProvider.decodeToken(token);
        UserDetails userDetails = jwtProvider.getUserDetails(jwt);
        UserDto userDto = userDao.getUserByUsername(userDetails.getUsername());
        String userAgent = jwtProvider.getUserAgent(jwt);
        String secretKey = jwtProvider.getUserSecret(jwt);
        JwtTokenDto jwtTokenDto = jwtTokenDao.findByUserIdAndUserAgent( userDto.getId(), userAgent,  secretKey);
        jwtTokenDto.deActivate();

        jwtTokenDao.updateToken( jwtTokenDto );

        return jwtTokenDto;
    }

    // Refresh Token으로 Access Token 재발급
    public JwtTokenDto refreshAccessToken(String refreshToken) {
        try {
            // Refresh Token이 유효한지 확인
            if (!isValidRefreshToken(refreshToken)) {
                throw new CustomRuntimeException(ErrorCode.UNAUTHORIZED_ACCESS,
                        "Invalid refresh token");
            }

            // 유효한 Refresh Token인 경우 Access Token 재발급
            Authentication authentication = jwtProvider.getAuthentication( refreshToken );
            JwtTokenDto jwtTokenDto = jwtProvider.generateJwtTokens(authentication);

            // 새로운 Token을 DB에 update
            updateJwtToken(jwtTokenDto);

            return jwtTokenDto;
        } catch (JwtException | IllegalArgumentException e) {
            log.error("Invalid Refresh Token", e);
            throw new CustomRuntimeException(ErrorCode.UNAUTHORIZED_ACCESS,
                    "Invalid or expired refresh token");
        }
    }

    public JwtTokenDto generateNewTokens( LogInOutCommonRecord data  )  {

        Authentication authentication = getAuthentication(data);
        JwtTokenDto jwtTokenDto = jwtProvider.generateJwtTokens( authentication );
        saveAccessToken( jwtTokenDto );

        return jwtTokenDto;
    }

    public JwtTokenDto generateNewTokensForOAuth2( Authentication authentication  )  {

        JwtTokenDto jwtTokenDto = jwtProvider.generateJwtTokensForOAuth2( authentication );
        saveAccessToken( jwtTokenDto );

        return jwtTokenDto;

    }

    private Authentication getAuthentication( LogInOutCommonRecord data ) {

        // 1. 사용자 정보를 DB에서 조회
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(data.username());

        // 2. 패스워드 검증
        if (!PasswordUtils.matches(data.password(), userDetails.getPassword())) {
            throw new CustomRuntimeException(ErrorCode.INVALID_PASSWORD);
        }

        // 3. 인증이 성공하면 CustomAuthentication 반환
        return new CustomAuthenticationToken(
                userDetails,
//                data.username(),
                data.password(),
//                new ArrayList<>(),  // 권한 정보는 빈 리스트로 설정 (실제로 필요시 추가)
                UUID.randomUUID().toString(),
                data.userAgent()
        );
    }

    public UserDto getUser( String token ) {
        // Token이 유효한지 확인
        if (!isValidRefreshToken(token)) {
            throw new CustomRuntimeException(ErrorCode.INVALID_ACCESS_TOKEN);
        }
        Jwt jwt = jwtProvider.decodeToken(token);
        UserDetails userDetails = jwtProvider.getUserDetails( jwt );

        return userDao.getUserByUsername(userDetails.getUsername());
    }

    public UserDto getUserFromToken( String token ) {
        Jwt jwt = jwtProvider.decodeToken(token);
        UserDetails userDetails = jwtProvider.getUserDetails( jwt );

        return userDao.getUserByUsername(userDetails.getUsername());
    }

    public String getTokenByHeader(Map<String, String> headers, String tokenType) {
        String token = headers.containsKey("authorization")?
                headers.get("authorization").replace("Bearer ", "") :
                headers.getOrDefault(tokenType, null);


        if( !StringUtils.hasText(token) ) {
            throw new CustomRuntimeException(ErrorCode.MISSING_ACCESS_TOKEN);
        }

        return token;
    }

    public JwtTokenDto getAuthenticationJwtToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null ) {
            throw new CustomRuntimeException(ErrorCode.MISSING_ACCESS_TOKEN);
        }

        log.error( ">> ### authentication name: {}", authentication.getName() );
        log.error( ">> ### Principal type: {}", authentication.getPrincipal().getClass());

        String username = authentication.getName();

        UserDto userDto = userDao.getUserByUsername(username);

        return jwtTokenDao.findByUserId(userDto.getId());


//        if(authentication.getPrincipal() instanceof UserDetails userDetails) {
//            String username = userDetails.getUsername();
//            UserDto userDto = userDao.getUserByUsername(username);
//
//            return jwtTokenDao.findByUserId(userDto.getId());
//
//        } else if(authentication.getPrincipal() instanceof OAuth2User oAuth2User) {
//            CustomOauth2User customOauth2User = (CustomOauth2User) oAuth2User;
//            String providerUserId = customOauth2User.getOAuthAttributes().getOAuth2UserInfo().getId();
//            OAuthProvider oAuthProvider = customOauth2User.getOAuthProvider();
//
//            SocialLoginDto socialLoginDto = socialLoginDao.findByProviderUserIdAndOAuthProvider(providerUserId, oAuthProvider);
//
//            return jwtTokenDao.findByUserId(socialLoginDto.getUserId());
//
//        } else {
//            throw new CustomRuntimeException(ErrorCode.INVALID_ACCESS_TOKEN);
//        }
    }

    public Long getUserIdFromToken() {
        JwtTokenDto jwtTokenDto = getAuthenticationJwtToken();
        return jwtTokenDto.getUserId();
    }
}
