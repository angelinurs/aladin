package com.korutil.server.dto.user.record;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.korutil.server.dto.user.constant.ROLE;
import com.korutil.server.dto.user.UserDto;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "이용자 정보 간소화")
public record UserSimpleResponse(
        @Schema(description = "사용자 아이디", example = "kiwi123")
        String username,

        @Schema(description = "사용자 이메일", example = "kiwi@example.com")
        String email,

        @Schema(description = "사용자 권한(역할)", example = "USER")
        ROLE role
) {
    public static UserSimpleResponse fromDto(UserDto dto) {
        return new UserSimpleResponse(
                dto.getUsername(),
                dto.getEmail(),
                dto.getRole()
        );
    }
}