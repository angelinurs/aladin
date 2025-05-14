package com.korutil.server.dto.user.record;

import com.korutil.server.dto.user.constant.ROLE;
import com.korutil.server.dto.user.UserDto;

import java.time.LocalDateTime;

public record UserResponse(
        Long id,
        String username,
        String email,
        ROLE role,
        Boolean emailVerified,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime lastLoginAt,
        LocalDateTime deletedAt,
        Boolean activated
) {

    public static UserResponse fromDto(UserDto dto) {
        return new UserResponse(
                dto.getId(),
                dto.getUsername(),
                dto.getEmail(),
                dto.getRole(),
                dto.getEmailVerified(),
                dto.getCreatedAt(),
                dto.getUpdatedAt(),
                dto.getLastLoginAt(),
                dto.getDeletedAt(),
                dto.getActivated()
        );
    }
}