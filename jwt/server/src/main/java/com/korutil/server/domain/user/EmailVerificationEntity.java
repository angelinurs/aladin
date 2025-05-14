package com.korutil.server.domain.user;

import com.korutil.server.dto.user.EmailVerificationDto;
import com.korutil.server.domain.usecase.BaseEntity;
import com.korutil.server.util.CustomLocalDateTimeUtils;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "email_verification", schema = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class EmailVerificationEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(nullable = false)
    private String token;  // 인증 토큰 (랜덤 문자열)

    @Column(nullable = false)
    private String email;  // email

    @Column(nullable = false)
    @Builder.Default
    private Boolean isVerified = false;  // 인증 여부 (기본값: 인증되지 않음)

    @Column(nullable = false)
    private LocalDateTime expirationAt;  // 인증 유효기간

    @Column(nullable = false)
    private LocalDateTime emailSentAt;  // 이메일 발송 시간

    public void update() {
        this.isVerified = true;
    }

    public EmailVerificationDto toDto() {
        return
                EmailVerificationDto.builder()
                        .token(this.token)
                        .isVerified(this.isVerified)
                        .expirationAt(this.expirationAt)
                        .emailSentAt(this.emailSentAt)
                        .build();
    }

    public static EmailVerificationEntity fromDto( EmailVerificationDto dto ) {
        LocalDateTime now = CustomLocalDateTimeUtils.getNow();
        return
                EmailVerificationEntity.builder()
                        .token(dto.getToken())
                        .email(dto.getEmail())
                        .expirationAt(now.plusMinutes(30))
                        .emailSentAt(now)
                        .build();

    }
}