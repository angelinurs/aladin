package com.korutil.server.domain.user;

import com.korutil.server.oauth.OAuthProvider;
import com.korutil.server.dto.user.SocialLoginDto;
import com.korutil.server.domain.usecase.AdvancedBaseEntity;
import com.korutil.server.util.CustomLocalDateTimeUtils;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "social_logins", schema = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class SocialLoginEntity extends AdvancedBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId; // 사용자 ID (users 테이블의 외래키)

    @Column(name = "provider", nullable = false, length = 255)
    private OAuthProvider provider; // 소셜 로그인 제공자 (Google, Kakao 등)

    @Column(name = "provider_user_id", nullable = false, length = 255)
    private String providerUserId; // 소셜 로그인 제공자에서 제공하는 고유 ID

    @Column(name = "nickname", nullable = false, length = 255)
    private String nickname; // 소셜 로그인 제공자에서 제공하는 사용자 nickname

    @Column(name = "email", nullable = false, length = 255)
    private String email; // 소셜 로그인 제공자에서 제공하는 사용자 email

    @Column(name = "profile_picture_url", length = 255)
    private String profilePictureUrl; // 프로필 사진 URL (선택적)

    public void deActivated()   {
        softDelete();
    }

    public static SocialLoginEntity fromDto(SocialLoginDto dto) {
        return
                SocialLoginEntity.builder()
                        .userId(dto.getUserId())
                        .provider(dto.getProvider())
                        .providerUserId(dto.getProviderUserId())
                        .nickname(dto.getNickname())
                        .email(dto.getEmail())
                        .profilePictureUrl(dto.getProfilePictureUrl())
                        .build();
    }

    public SocialLoginDto toDto() {
        return
                SocialLoginDto.builder()
                        .id(this.id)
                        .userId(this.userId)
                        .provider(this.provider)
                        .providerUserId(this.providerUserId)
                        .nickname(this.nickname)
                        .email(this.email)
                        .profilePictureUrl(this.profilePictureUrl)
                        .build();
    }
}