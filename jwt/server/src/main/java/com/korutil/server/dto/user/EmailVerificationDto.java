package com.korutil.server.dto.user;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailVerificationDto {

    public static final String SIGNUP_SUBJECT = "Amurlab.com 가입 이메일 인증 코드입니다.";

    private String token;  // 인증 토큰 (랜덤 문자열)
    private String email;  // 인증 토큰 (랜덤 문자열)
    private String subject;
    private Boolean isVerified;  // 인증 여부 (기본값: 인증되지 않음)
    private LocalDateTime expirationAt;  // 인증 유효기간
    private LocalDateTime emailSentAt;

    public void setSignupMail() {
        this.setSubject( SIGNUP_SUBJECT );
        updateToken();
    }

    private void updateToken() {
        this.setToken(crateToken());
    }

    private String crateToken() {

        String[] uuid = UUID.randomUUID().toString().split("-");

        return Arrays.stream(uuid)
                .map( text -> text.substring(0,1))
                .collect(Collectors.joining()).toUpperCase(Locale.ROOT);
    }
}
