package com.korutil.server.controller;

import com.korutil.server.config.properties.OAuthKakaoProperties;
import com.korutil.server.dto.common.CommonApiResponse;
import com.korutil.server.dto.jwt.JwtTokenResponse;
import com.korutil.server.dto.user.record.LogInOutCommonRecord;
import com.korutil.server.dto.user.record.LoginRequest;
import com.korutil.server.dto.jwt.RefreshTokenRequest;
import com.korutil.server.dto.user.record.LoginSuccessResponse;
import com.korutil.server.handler.CustomRuntimeException;
import com.korutil.server.handler.ErrorCode;
import com.korutil.server.oauth.OAuthProvider;
import com.korutil.server.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final OAuthKakaoProperties oAuthKakaoProperties;

    // Refresh Token으로 Access Token 재발급
    @PostMapping("/reissue")
    @Operation(
            summary = "Refresh access token",
            description = "Refresh access token using the provided refresh token.",
            parameters = {
                    @Parameter(name = "User-Agent")
            }
    )
    public CommonApiResponse<JwtTokenResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        // Refresh Token 검증 및 새로운 Access Token 발급
        return authService.refreshAccessToken(request.refreshToken());
    }

    // 지정된 Social login url 응답
    @PostMapping("/provider/login/{provider}")
    @Operation(
            summary = "get Social login url",
            description = "지정된 Social login url 응답"
    )
    public CommonApiResponse<String> getOAuthLoginUrlByProvider(@PathVariable String provider) {

        String url = switch (OAuthProvider.fromString(provider) ) {
            case KAKAO -> oAuthKakaoProperties.getAuthLoginUrl();
            case NAVER -> oAuthKakaoProperties.getAuthLoginUrl();
            default -> throw new CustomRuntimeException(ErrorCode.NOT_FOUND_OAUTH_PROVIDER, provider);
        };
        return CommonApiResponse.success(url);
    }

    // 지정된 Social logout url 응답
    @PostMapping("/provider/logout/{provider}")
    @Operation(
            summary = "get Social logout url",
            description = "지정된 Social logout url 응답"
    )
    public CommonApiResponse<String> getOAuthLogoutUrlByProvider(@PathVariable String provider) {

        String url = switch (OAuthProvider.fromString(provider) ) {
            case KAKAO -> oAuthKakaoProperties.getAuthLogoutUrl();
            case NAVER -> oAuthKakaoProperties.getAuthLogoutUrl();
            default -> throw new CustomRuntimeException(ErrorCode.NOT_FOUND_OAUTH_PROVIDER, provider);
        };
        return CommonApiResponse.success(url);
    }
}
