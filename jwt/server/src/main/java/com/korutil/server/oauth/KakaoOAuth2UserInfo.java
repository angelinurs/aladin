package com.korutil.server.oauth;

import java.util.Map;
import java.util.Optional;

public class KakaoOAuth2UserInfo extends OAuth2UserInfo {

    public KakaoOAuth2UserInfo(Map<String, Object> attributes, String userNameAttributeKey) {
        super(attributes, userNameAttributeKey);
    }

    @Override
    public String getId() {
        return Optional.ofNullable(attributes)
                .map(attr -> attr.get(nameAttributeKey))  // "id"를 추출
//                .filter(String.class::isInstance)  // "id"가 String 타입인지 확인
                .map(String::valueOf)  // String 타입으로 안전하게 캐스팅
                .orElse(null);  // null이면 null 반환
    }

    @Override
    public String getNickname() {
        return Optional.ofNullable(attributes)
                .map(attr -> attr.get("kakao_account"))
                .filter(Map.class::isInstance)  // "kakao_account"가 Map인 경우만 처리
                .map(Map.class::cast)  // 안전하게 Map으로 캐스팅
                .map(account -> account.get("profile"))
                .filter(Map.class::isInstance)  // "profile"이 Map인 경우만 처리
                .map(Map.class::cast)  // 안전하게 Map으로 캐스팅
                .map(profile -> profile.get("nickname"))
                .map(String.class::cast)  // "nickname"이 String인 경우만 처리
                .orElse(null);
    }

    @Override
    public String getEmail() {
        return Optional.ofNullable(attributes)
                .map(attr -> attr.get("kakao_account"))
                .filter(Map.class::isInstance)  // "kakao_account"가 Map인 경우만 처리
                .map(Map.class::cast)  // 안전하게 Map으로 캐스팅
                .map(profile -> profile.get("email"))
                .map(String.class::cast)  // "email"이 String인 경우만 처리
                .orElse(null);
    }

    @Override
    public String getImageUrl() {
        return Optional.ofNullable(attributes)
                .map(attr -> attr.get("kakao_account"))
                .filter(Map.class::isInstance)  // "kakao_account"가 Map인 경우만 처리
                .map(Map.class::cast)  // 안전하게 Map으로 캐스팅
                .map(account -> account.get("profile"))
                .filter(Map.class::isInstance)  // "profile"이 Map인 경우만 처리
                .map(Map.class::cast)  // 안전하게 Map으로 캐스팅
                .map(attr -> attr.get("profile_image_url"))
                .filter(String.class::isInstance)
                .map(String.class::cast)
                .orElse(null);
    }
}
