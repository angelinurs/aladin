package com.korutil.server.dto.user.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.util.StringUtils;

import java.util.Locale;

public enum ROLE {
    ADMIN,
    USER,
    GUEST;

    @JsonCreator
    public static ROLE fromString(String role) {
        try {
            return StringUtils.hasText(role)? ROLE.valueOf(role.toUpperCase(Locale.ROOT)) : GUEST;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid role name: " + role, e);
        }
    }
}
