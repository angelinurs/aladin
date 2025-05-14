package com.korutil.server.handler.social;

import com.korutil.server.dto.jwt.JwtTokenDto;
import com.korutil.server.service.security.JwtTokenService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.function.Function;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private static final String FRONT_CALLBACK_URI="http://localhost:3000/oauth/callback";
    private final JwtTokenService jwtTokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("OAuth login 성공!");
        log.info("{}", authentication.getName());
        log.info("{}", authentication);

        JwtTokenDto jwtTokenDto = jwtTokenService.generateNewTokensForOAuth2(authentication);
        jwtTokenDto.setUsername(authentication.getName());

        String redirectUrl = UriComponentsBuilder.fromUriString(FRONT_CALLBACK_URI)
                .queryParam("accessToken", jwtTokenDto.getAccessToken())
                .queryParam("refreshToken", jwtTokenDto.getRefreshToken())
                .build().toUriString();
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");

        response.sendRedirect(redirectUrl);
    }
}
