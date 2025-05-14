package com.korutil.server.handler.social;

import com.korutil.server.handler.CustomRuntimeException;
import com.korutil.server.handler.ErrorCode;
import com.korutil.server.oauth.CustomOauth2User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@RequiredArgsConstructor
@Component
public class CustomOAuthLogoutSuccessHandler implements LogoutSuccessHandler {

    private static final String FRONT_URI="http://localhost:3000/";
    @Value("${oauth.kakao.client-id}")
    private String KAKAO_CLIENT_ID;
    @Value("${oauth.kakao.logout-uri}")
    private String KAKAO_LOGOUT_URI;
    @Value("${oauth.kakao.logout-redirect-uri}")
    private String KAKAO_LOGOUT_REDIRECT_URI;



    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        if (!(authentication.getPrincipal() instanceof OAuth2User)) {
            throw new CustomRuntimeException(ErrorCode.UNAUTHORIZED_ACCESS,
                    "Unsupported principal type: " + authentication.getPrincipal().getClass());
        }

        CustomOauth2User customOauth2User = (CustomOauth2User) authentication.getPrincipal();

        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.sendRedirect(FRONT_URI);
    }

    private String getRedirectUriKakao() {
        // TODO. logout 기록 남김.
        return UriComponentsBuilder.fromUriString(KAKAO_LOGOUT_URI)
                .queryParam("client_id", KAKAO_CLIENT_ID)
                .queryParam("logout_redirect_uri", URLEncoder.encode(KAKAO_LOGOUT_REDIRECT_URI, StandardCharsets.UTF_8))
                .build().toUriString();
    }

    private String getRedirectUriNaver() {
        // TODO. logout 기록 남김.
        return UriComponentsBuilder.fromUriString(KAKAO_LOGOUT_URI)
                .queryParam("client_id", KAKAO_CLIENT_ID)
                .queryParam("logout_redirect_uri", URLEncoder.encode(KAKAO_LOGOUT_REDIRECT_URI, StandardCharsets.UTF_8))
                .build().toUriString();
    }
}
