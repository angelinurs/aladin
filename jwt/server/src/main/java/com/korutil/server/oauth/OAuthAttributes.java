package com.korutil.server.oauth;

import com.korutil.server.handler.CustomRuntimeException;
import com.korutil.server.handler.ErrorCode;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@Getter
@RequiredArgsConstructor
@Builder
public class OAuthAttributes {
    private final String nameAttributeKey;
    private final OAuth2UserInfo oAuth2UserInfo;

    public static OAuthAttributes of(OAuthProvider oAuthProvider, String userNameAttributeKey, Map<String, Object> attributes) {
        return
                switch (oAuthProvider) {
                    case KAKAO -> ofKakao(userNameAttributeKey, attributes);
                    case NAVER -> ofNaver(userNameAttributeKey, attributes);
                    case GOOGLE -> ofGoogle(userNameAttributeKey, attributes);
                    default -> throw new CustomRuntimeException(ErrorCode.UNAUTHORIZED_ACCESS);
                };
    }

    public static OAuthAttributes ofKakao(String userNameAttributeKey, Map<String, Object> attributes) {
        return
                OAuthAttributes.builder()
                        .nameAttributeKey(userNameAttributeKey)
                        .oAuth2UserInfo(new KakaoOAuth2UserInfo(attributes, userNameAttributeKey))
                        .build();
    }

    public static OAuthAttributes ofNaver(String userNameAttributeKey, Map<String, Object> attributes) {
        return
                OAuthAttributes.builder()
                        .nameAttributeKey(userNameAttributeKey)
                        .oAuth2UserInfo(new KakaoOAuth2UserInfo(attributes, userNameAttributeKey))
                        .build();
    }

    public static OAuthAttributes ofGoogle(String userNameAttributeKey, Map<String, Object> attributes) {
        return
                OAuthAttributes.builder()
                        .nameAttributeKey(userNameAttributeKey)
                        .oAuth2UserInfo(new KakaoOAuth2UserInfo(attributes, userNameAttributeKey))
                        .build();
    }
}
