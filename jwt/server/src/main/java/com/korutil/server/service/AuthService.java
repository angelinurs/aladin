package com.korutil.server.service;

import com.korutil.server.dto.common.CommonApiResponse;
import com.korutil.server.dto.jwt.JwtTokenDto;
import com.korutil.server.dto.jwt.JwtTokenResponse;
import com.korutil.server.dto.user.record.LogInOutCommonRecord;
import com.korutil.server.dto.user.record.LoginSuccessResponse;
import com.korutil.server.dto.user.UserDto;
import com.korutil.server.service.security.JwtTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtTokenService jwtTokenService;
    private final LoginService loginService;
    private final UserService userService;

    public CommonApiResponse<LoginSuccessResponse> login(LogInOutCommonRecord data) {

        JwtTokenDto jwtTokenDto = jwtTokenService.generateNewTokens(data);
        log.info("JWT 로그인 성공: {}", data.username());

        loginService.addLoginHistory(jwtTokenDto);
        UserDto userDto = userService.getUser(jwtTokenDto.getUserId());


        return CommonApiResponse.success(
                LoginSuccessResponse.from(userDto, jwtTokenDto)
        );
    }

    public void logout( Map<String, String> headers ) {
        String token = jwtTokenService.getTokenByHeader(headers, "refreshToken");

        JwtTokenDto jwtTokenDto = jwtTokenService.deActivateToken( token );
        loginService.addLogoutHistory( jwtTokenDto );
    }

    public CommonApiResponse<JwtTokenResponse> refreshAccessToken(String refreshToken) {
        JwtTokenDto dto = jwtTokenService.refreshAccessToken(refreshToken);
        return CommonApiResponse.success(dto.toResponse());
    }

}
