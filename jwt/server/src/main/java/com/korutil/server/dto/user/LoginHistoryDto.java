package com.korutil.server.dto.user;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginHistoryDto {

    private Long id;  // 로그인 기록 ID
    private Long userId;  // 사용자 ID
    private LocalDateTime logoutAt;  // 마지막 로그인 시간
    private String loginIp;  // 로그인 IP
    private String userAgent;  // User-Agent (로그인한 디바이스 정보 등)
    private LocalDateTime createdAt;  // 생성 시간
    private LocalDateTime updatedAt;  // 수정 시간
}
