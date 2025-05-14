package com.korutil.server.dto.user;

import com.korutil.server.dto.user.constant.ROLE;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDateTime;
import java.util.Collection;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    private Long id;  // 사용자 ID

    private String username;  // 이름 또는 닉네임
    private String email;  // 이메일

    private ROLE role;  // 사용자 권한 (USER, ADMIN 등)

    private Boolean emailVerified;  // 이메일 인증 여부

    private Collection<? extends GrantedAuthority> authorities;  // 사용자 권한

    private LocalDateTime createdAt;  // 생성 시간
    private LocalDateTime updatedAt;  // 수정 시간
    private LocalDateTime lastLoginAt;  // 마지막 로그인 시간 (로그인 기록에서 가져옴)
    private LocalDateTime deletedAt;  // 삭제 시간 (소프트 삭제)

    private Boolean activated;  // 계정 활성화 여부

}
