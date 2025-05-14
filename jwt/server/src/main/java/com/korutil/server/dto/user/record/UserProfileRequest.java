package com.korutil.server.dto.user.record;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.korutil.server.dto.user.UserProfileDto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "사용자 프로필 요청 객체")
public record UserProfileRequest(

        @Schema(description = "이름 또는 닉네임", example = "나루")
        String name,

        @Schema(description = "전화번호", example = "01012345678")
        String phone,

        @Schema(description = "생년월일", type = "string", format = "date", example = "1990-01-01")
        LocalDate birth
) {
    public UserProfileDto toDto() {
        return
                UserProfileDto.builder()
                        .name(name)
                        .phone(UserProfileDto.getJustNumber(phone))
                        .birth(birth)
                        .build();
    }
}