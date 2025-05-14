package com.korutil.server.domain.user;

import com.korutil.server.dto.jwt.JwtTokenDto;
import com.korutil.server.domain.usecase.BaseEntity;
import com.korutil.server.util.CustomLocalDateTimeUtils;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "jwt_token", schema = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class JwtTokenEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;  // 사용자 ID (users 테이블의 외래키)

    @Column(name = "user_agent", length = 512, nullable = false)
    private String userAgent;  // Access Token

    @Column(name = "secret_key", length = 512, nullable = false)
    private String secretKey;  // Access Token

    @Column(name = "client_ip", length = 512, nullable = false)
    private String clientIp;  // Access Token

    @Column(name = "activated", nullable = false)
    @Builder.Default
    private Boolean activated = Boolean.TRUE;  // Refresh Token의 활성화 상태 (기본값은 TRUE)

    @Column(name = "access_token", length = 512, nullable = false)
    private String accessToken;  // Access Token (필요시 저장)

    @Column(name = "refresh_token", length = 512, nullable = false)
    private String refreshToken;  // Refresh Token

    @Column(name = "refresh_token_created_at", nullable = false)
    private LocalDateTime refreshTokenCreatedAt;  // Refresh Token 발급 시간

    @Column(name = "refresh_token_updated_at")
    private LocalDateTime refreshTokenUpdatedAt;  // Refresh Token 갱신 시간

    @PrePersist
    public void prePersist() {
        if (this.refreshToken != null) {
            this.refreshTokenCreatedAt = CustomLocalDateTimeUtils.getNow();
        }
    }

    @PreUpdate
    public void preUpdate() {
        if (this.refreshToken != null) {
            this.refreshTokenUpdatedAt = CustomLocalDateTimeUtils.getNow();
        }
    }

    public void updateToken(JwtTokenDto dto) {
        this.accessToken = dto.getAccessToken();
        this.refreshToken = dto.getRefreshToken();
        this.activated = dto.getActivated();
        this.refreshTokenUpdatedAt = CustomLocalDateTimeUtils.getNow();
    }

    public JwtTokenDto toDto() {
        return JwtTokenDto.builder()
                .userId(this.userId)
                .userAgent(this.userAgent)
                .secretKey(this.secretKey)
                .clientIp(this.clientIp)
                .accessToken(this.accessToken)
                .activated(this.activated)
                .refreshToken(this.refreshToken)
                .refreshTokenCreatedAt(this.refreshTokenCreatedAt)
                .refreshTokenUpdatedAt(this.refreshTokenUpdatedAt)
                .build();
    }

    public static JwtTokenEntity fromDto(JwtTokenDto dto) {
        return JwtTokenEntity.builder()
                .userId(dto.getUserId())
                .userAgent(dto.getUserAgent())
                .secretKey(dto.getSecretKey())
                .clientIp(dto.getClientIp())
                .accessToken(dto.getAccessToken())
                .refreshToken(dto.getRefreshToken())
                .build();
    }
}
