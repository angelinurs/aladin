package com.korutil.server.dto.jwt;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Refresh Token 요청 객체")
public record RefreshTokenRequest(
        @JsonProperty("refreshToken")
        @JacksonXmlProperty(localName = "refreshToken")
        @Parameter(description = "사용자 Refresh Token", required = true)
        @Schema(description = "사용자 인증을 위한 refreshToken", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MSwiaWF0IjoxNjEyMTEyMjM4fQ.1KP6LBf3hDQnIEaQHpD95-VKD0PfiD5kzQN9P7sPfgw")
        String refreshToken,

        @Parameter(description = "접속 위치", required = true, example = "firefox-123")
        @Schema(description = "사용자 접속 위치", example = "firefox-123")
        @JsonProperty("user_agent")
        String userAgent
) {}
