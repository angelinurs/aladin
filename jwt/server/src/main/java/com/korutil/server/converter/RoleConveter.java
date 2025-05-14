package com.korutil.server.converter;

import com.korutil.server.dto.user.constant.ROLE;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.util.StringUtils;

import java.util.Locale;

@Converter(autoApply = true)
public class RoleConveter implements AttributeConverter<ROLE, String> {
    @Override
    public String convertToDatabaseColumn(ROLE role) {
        return role != null? role.name().toUpperCase(Locale.ROOT): ROLE.GUEST.name();
    }

    @Override
    public ROLE convertToEntityAttribute(String role) {
        return StringUtils.hasText(role)? ROLE.fromString(role): ROLE.GUEST;
    }
}