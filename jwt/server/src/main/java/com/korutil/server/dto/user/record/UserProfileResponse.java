package com.korutil.server.dto.user.record;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.korutil.server.dto.user.UserProfileDto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "사용자 프로필 응답 객체")
public record UserProfileResponse(

        @Schema(description = "실명", example = "홍길동")
        String name,

        @Schema(description = "전화번호", example = "010-1234-5678")
        String phone,

        @Schema(description = "생년월일", example = "1990-01-01")
        LocalDate birth
) {
    public static UserProfileResponse fromDto(UserProfileDto dto) {
        return new UserProfileResponse(
                dto.getName(),
                dto.getPhone(),
                dto.getBirth()
        );
    }
}
