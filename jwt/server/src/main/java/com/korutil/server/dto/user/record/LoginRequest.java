package com.korutil.server.dto.user.record;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "사용자 로그인 요청 객체")
public record LoginRequest(

        @Parameter(description = "사용자의 로그인 아이디 (유저네임)", required = true, example = "naru")
        @Schema(description = "로그인에 사용되는 유저네임", example = "naru")
        String username,

        @Parameter(description = "사용자의 비밀번호", required = true, example = "123")
        @Schema(description = "로그인에 사용되는 비밀번호", example = "123")
        String password
) {}
