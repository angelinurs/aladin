package com.korutil.server.dto.user;

import com.korutil.server.oauth.OAuthProvider;
import com.korutil.server.oauth.OAuthAttributes;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SocialLoginDto {

    private Long id;
    private Long userId;
    private OAuthProvider provider;
    private String providerUserId;
    private String nickname;
    private String email;
    private String profilePictureUrl;

    public static SocialLoginDto of(OAuthAttributes oAuthAttributes, OAuthProvider oAuthProvider) {
        return
                SocialLoginDto.builder()
                        .provider(oAuthProvider)
                        .providerUserId(oAuthAttributes.getOAuth2UserInfo().getId())
                        .nickname(oAuthAttributes.getOAuth2UserInfo().getNickname())
                        .email(oAuthAttributes.getOAuth2UserInfo().getEmail())
                        .profilePictureUrl(oAuthAttributes.getOAuth2UserInfo().getImageUrl())
                        .build();
    }
}
