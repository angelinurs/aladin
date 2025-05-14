package com.korutil.server.dto.user.record;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.korutil.server.dto.user.constant.ROLE;
import com.korutil.server.dto.user.UserDto;

import java.time.LocalDate;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserRequest(
        String username,
        String password,
        String email,
        ROLE role,
        String name,
        String phone,
        LocalDate birth,
        Boolean emailVerified
) {
    // 기본 생성자에서 role 필드의 기본값 설정
    public UserRequest {
        if (role == null) {
            role = ROLE.USER; // 기본값 설정 (ROLE.USER)
        }
    }

    // DTO로 변환하는 메서드
    public UserDto toDto() {
        return UserDto.builder()
                .username(this.username())
                .email(this.email())
                .role(this.role())
                .emailVerified(this.emailVerified())
                .build();
    }
}