package com.korutil.server.dto.user.record;

import com.korutil.server.dto.user.SocialLoginDto;
import com.korutil.server.oauth.OAuthProvider;

public record SocialLoginResponse (
        OAuthProvider provider,
        String nickname,
        String email
) {
    // `OAuthAttributes`와 `OAuthProvider`를 기반으로 `SocialLoginDto` 객체를 생성하는 메서드
    public static SocialLoginResponse ofDto(SocialLoginDto dto) {
        return new SocialLoginResponse(
                dto.getProvider(),
                dto.getNickname(),
                dto.getEmail()
        );
    }
}
