package com.korutil.server.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @implNote front 요청 전용
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "oauth.kakao")
public class OAuthKakaoProperties {
    private String clientId;
    private String clientSecret;

    private String loginUri;
    private String loginRedirectUri;

    private String logoutUri;
    private String logoutRedirectUri;

    private String buttonImage;

    private String frontCallbackUri;

    public String getAuthLoginUrl() {
        return UriComponentsBuilder.fromUriString(loginUri)
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", URLEncoder.encode(loginRedirectUri, StandardCharsets.UTF_8))
                .queryParam("response_type", "code")
                .build().toUriString();
    }

    public String getAuthLogoutUrl() {
        return UriComponentsBuilder.fromUriString(logoutUri)
                .queryParam("client_id", clientId)
                .queryParam("logout_redirect_uri", URLEncoder.encode(logoutRedirectUri, StandardCharsets.UTF_8))
                .queryParam("response_type", "code")
                .build().toUriString();
    }

}
