package com.korutil.server.dto.user.record;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "사용자 로그아웃 요청 객체")
public record LogoutRequest(

        @Parameter(description = "사용자의 로그인 아이디 (유저네임)", required = true, example = "naru")
        @Schema(description = "로그인에 사용되는 유저네임", example = "naru")
        String username
) {}
