package com.korutil.server.dto.user.record;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.korutil.server.dto.user.EmailVerificationDto;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Email 인증 요청 객체")
public record EmailVerificationRequest(

        @Parameter(description = "사용자의 Email", required = true, example = "naru@naru.com")
        @Schema(description = "사용자 인증에 사용되는 Email", example = "naru@naru.com")
        String email,

        @Parameter(description = "인증 Token", required = true, example = "A123E")
        @Schema(description = "사용자 인증에 사용되는 인증 Token", example = "A123E")
        String code
) {
    public EmailVerificationDto toDto() {
        return
                EmailVerificationDto.builder()
                        .email(email())
                        .token(code())
                        .build();
    }
}
