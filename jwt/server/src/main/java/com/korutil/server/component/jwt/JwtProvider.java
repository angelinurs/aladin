package com.korutil.server.component.jwt;

import com.korutil.server.dto.jwt.CustomClaimNames;
import com.korutil.server.dto.jwt.JwtTokenDto;
import com.korutil.server.handler.CustomRuntimeException;
import com.korutil.server.handler.ErrorCode;
import com.korutil.server.oauth.CustomOauth2User;
import com.korutil.server.oauth.OAuth2UserInfo;
import com.korutil.server.service.security.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtProvider {

    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;

    private final CustomUserDetailsService customUserDetailsService;

    @Value("${jwt.issuer-uri}")
    private String issuerUri;

    @Value("${jwt.access-token-expiration}") // 예: 3600 (1시간)
    private long accessTokenExpiration;

    @Value("${jwt.refresh-token-expiration}") // 예: 604800 (7일)
    private long refreshTokenExpiration;

    private static final JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();

    public Jwt decodeToken(String token) {
        return jwtDecoder.decode(token);
    }

    public UserDetails getUserDetails( Jwt jwt ) {
        String username = jwt.getSubject();

        // 사용자 정보 조회 추가
        return customUserDetailsService.loadUserByUsername(username);
    }

    public String getUserAgent( Jwt jwt ) {
        return jwt.getClaimAsString(CustomClaimNames.USER_AGENT);

    }

    public String getUserSecret( Jwt jwt ) {
        return jwt.getClaimAsString(CustomClaimNames.SECRET_KEY);
    }

    public Authentication getAuthentication( String refreshToken ) {

        Jwt jwt = decodeToken(refreshToken);
        UserDetails userDetails = getUserDetails( jwt );
        String secretKey = getUserSecret(jwt);
        String userAgent = getUserAgent(jwt);

        return
                new CustomAuthenticationToken(userDetails, null, userDetails.getAuthorities(), secretKey, userAgent);
    }

    public String getJwtToken( JwtClaimsSet claims ) {
        return jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
    }

    public JwtClaimsSet getAccessClaimSet(Instant now, String subject, Collection<? extends GrantedAuthority> authorities, String userAgent, String secretKey ) {

        Map<String, Object> customClaims = Map.of(
                StandardClaimNames.EMAIL, subject,
                CustomClaimNames.USER_AGENT, userAgent,
                CustomClaimNames.SECRET_KEY, secretKey,
                CustomClaimNames.AUTHORITIES, authorities.stream()
                                            .map(GrantedAuthority::getAuthority)
                                            .toList()
        );

        return getClaimSet( now, subject, customClaims, accessTokenExpiration);
    }

    public JwtClaimsSet getAccessClaimSetForOAuth2(Instant now, CustomOauth2User customOauth2User, String userAgent, String secretKey ) {

        Collection<? extends GrantedAuthority>  authorities = customOauth2User.getAuthorities();
        OAuth2UserInfo oAuth2UserInfo = customOauth2User.getOAuthAttributes().getOAuth2UserInfo();
        String email = oAuth2UserInfo.getEmail();

        Map<String, Object> customClaims = Map.of(
                StandardClaimNames.EMAIL, email,
                CustomClaimNames.USER_AGENT, userAgent,
                CustomClaimNames.SECRET_KEY, secretKey,
                CustomClaimNames.AUTHORITIES, authorities.stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList()
        );

        return getClaimSet( now, email, customClaims, accessTokenExpiration);
    }

    public JwtClaimsSet getRefreshClaimSet( Instant now, String subject, String userAgent, String secretKey ) {

        Map<String, Object> customClaims = Map.of(
                CustomClaimNames.USER_AGENT, userAgent,
                CustomClaimNames.SECRET_KEY, secretKey,
                CustomClaimNames.TYPE, CustomClaimNames.REFRESH
        );

        return getClaimSet( now, subject, customClaims, refreshTokenExpiration);
    }

    public JwtClaimsSet getRefreshClaimSetForOAuth2( Instant now, CustomOauth2User customOauth2User, String userAgent, String secretKey ) {

        Map<String, Object> customClaims = Map.of(
                CustomClaimNames.USER_AGENT, userAgent,
                CustomClaimNames.SECRET_KEY, secretKey,
                CustomClaimNames.TYPE, CustomClaimNames.REFRESH
        );

        return getClaimSet( now, customOauth2User.getOAuthAttributes().getOAuth2UserInfo().getEmail(), customClaims, refreshTokenExpiration);
    }

    private JwtClaimsSet getClaimSet(Instant now, String subject, Map<String, Object> customClaims, Long expiration) {
        return
                JwtClaimsSet.builder()
                        .issuer(issuerUri)
                        .issuedAt(now)
                        .expiresAt(now.plusSeconds(expiration))
                        .subject(subject)
                        .claims( claims -> claims.putAll(customClaims) )
                        .build();
    }

    // Access & Refresh Token 생성
    public JwtTokenDto generateJwtTokens(Authentication authentication) {
        if (!(authentication.getPrincipal() instanceof UserDetails userDetails)) {
            throw new CustomRuntimeException(ErrorCode.UNAUTHORIZED_ACCESS,
                    "Unsupported principal type: " + authentication.getPrincipal().getClass());
        }

        if (accessTokenExpiration <= 0) {
            throw new CustomRuntimeException(ErrorCode.UNAUTHORIZED_ACCESS, "JWT expiration must be a positive value");
        }

        Instant now = Instant.now();

        CustomAuthenticationToken customAuthenticationToken = (CustomAuthenticationToken) authentication;
        JwtClaimsSet accessClaims = getAccessClaimSet( now, userDetails.getUsername(), userDetails.getAuthorities(), customAuthenticationToken.getUserAgent(), customAuthenticationToken.getSecretKey() );

        String accessToken = getJwtToken(accessClaims);

        // Refresh Token (보통 권한 없이, 최소 정보만 포함)
        JwtClaimsSet refreshClaims = getRefreshClaimSet( now, userDetails.getUsername(), customAuthenticationToken.getUserAgent(), customAuthenticationToken.getSecretKey() );

        String refreshToken = getJwtToken(refreshClaims);

        log.debug("Generated AccessToken and RefreshToken for user: {}", userDetails.getUsername());

        return JwtTokenDto.from(
                userDetails.getUsername(),
                customAuthenticationToken.getSecretKey(), customAuthenticationToken.getUserAgent(),
                accessToken, refreshToken);
    }

    public JwtTokenDto generateJwtTokensForOAuth2(Authentication authentication) {
        if (!(authentication.getPrincipal() instanceof OAuth2User)) {
            throw new CustomRuntimeException(ErrorCode.UNAUTHORIZED_ACCESS,
                    "Unsupported principal type: " + authentication.getPrincipal().getClass());
        }

        if (accessTokenExpiration <= 0) {
            throw new CustomRuntimeException(ErrorCode.UNAUTHORIZED_ACCESS, "JWT expiration must be a positive value");
        }

        Instant now = Instant.now();

        String secretKey = UUID.randomUUID().toString();
        CustomOauth2User customOauth2User = (CustomOauth2User) authentication.getPrincipal();
        JwtClaimsSet accessClaims = getAccessClaimSetForOAuth2( now, customOauth2User, "", secretKey);

        String accessToken = getJwtToken(accessClaims);

        // Refresh Token (보통 권한 없이, 최소 정보만 포함)
        JwtClaimsSet refreshClaims = getRefreshClaimSetForOAuth2( now, customOauth2User, "", secretKey );

        String refreshToken = getJwtToken(refreshClaims);

        log.debug("Generated AccessToken and RefreshToken for user: {}", customOauth2User.getOAuthAttributes().getOAuth2UserInfo().getEmail());

        String username = JwtTokenDto.extractUsernameFromEmail(
                customOauth2User.getOAuthAttributes().getOAuth2UserInfo().getEmail());
        return JwtTokenDto.from(
                username,
                secretKey, "",
                accessToken, refreshToken);
    }

    public boolean validateRefreshToken(String refreshToken, String userAgent, String uuid) {
        Jwt jwt = jwtDecoder.decode(refreshToken);
        return CustomClaimNames.REFRESH.equals(jwt.getClaim(CustomClaimNames.TYPE)) &&
                userAgent.equals(jwt.getClaim(CustomClaimNames.USER_AGENT)) &&
                uuid.equals(jwt.getClaim(CustomClaimNames.SECRET_KEY)) &&
                !jwt.getExpiresAt().isBefore(Instant.now());
    }
}
