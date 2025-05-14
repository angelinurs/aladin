package com.korutil.server.domain.user;

import com.korutil.server.dto.user.LoginHistoryDto;
import com.korutil.server.domain.usecase.BaseEntity;
import com.korutil.server.util.CustomLocalDateTimeUtils;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "login_history", schema = "member")
public class LoginHistoryEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;  // 사용자 ID (users 테이블의 외래키)

    @Column(name = "logout_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime logoutAt;  // 마지막 로그인 시간

    @Column(name = "login_ip", length = 45)
    private String loginIp;  // 로그인 IP

    @Column(name = "user_agent", length = 255)
    private String userAgent;  // User-Agent (로그인한 디바이스 정보 등)

    @PrePersist
    public void prePersist() {
        this.createdAt = CustomLocalDateTimeUtils.getNow();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = CustomLocalDateTimeUtils.getNow();
    }

    public void setLogoutAt( LocalDateTime logoutAt) {
        this.logoutAt = logoutAt;
    }

    public LoginHistoryDto toDto() {
        return LoginHistoryDto.builder()
                .id(this.id)
                .userId(this.userId)
                .logoutAt(this.logoutAt)
                .loginIp(this.loginIp)
                .userAgent(this.userAgent)
                .createdAt(this.createdAt)  // BaseEntity에서 상속받은 createdAt
                .updatedAt(this.updatedAt)  // BaseEntity에서 상속받은 updatedAt
                .build();
    }

    // DTO -> Entity 변환 메소드
    public static LoginHistoryEntity fromDto(LoginHistoryDto dto) {
        return LoginHistoryEntity.builder()
                .userId(dto.getUserId())
                .logoutAt(dto.getLogoutAt())
                .loginIp(dto.getLoginIp())
                .userAgent(dto.getUserAgent())
                .build();
    }
}
