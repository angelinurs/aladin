package com.korutil.server.oauth;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.util.StringUtils;

import java.util.Locale;

public enum OAuthProvider {
    KAKAO,
    GOOGLE,
    NAVER;

    @JsonCreator
    public static OAuthProvider fromString(String registrationId) {
        try {
            if(!StringUtils.hasText(registrationId)) {
                return GOOGLE;
            }
            return OAuthProvider.valueOf(registrationId.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid role name: " + registrationId, e);
        }
    }
}
