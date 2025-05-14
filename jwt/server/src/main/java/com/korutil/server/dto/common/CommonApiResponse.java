package com.korutil.server.dto.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.korutil.server.util.CustomLocalDateTimeUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "공통 API 응답 구조<br />Error/반환 값이 있는 응답의 경우 참고")
public class CommonApiResponse<T> {

    @Schema(description = "응답 시간", example = "2025-02-04T15:30:00")
    private LocalDateTime timestamp;

    @JsonIgnore
    @Schema(description = "HTTP 상태 코드", example = "200")
    private int status;

    @Schema(description = "에러 코드", example = "RESOURCE_NOT_FOUND")
    private String code;

    @Schema(description = "응답 메시지", example = "Success")
    private String message;

    @Schema(description = "상세 오류 정보 (개발용)")
    private String details;

    @Schema(description = "응답 데이터")
    private T data;

    // success
    public static <T> CommonApiResponse<T> success(T data) {
        return CommonApiResponse.<T>builder()
                .timestamp(CustomLocalDateTimeUtils.getNow())
                .data(data)
                .build();
    }

    // Error
    public static <T> CommonApiResponse<T> error(
            HttpStatus status,
            String errorCode,
            String message
    ) {
        return CommonApiResponse.<T>builder()
                .timestamp(CustomLocalDateTimeUtils.getNow())
                .status(status.value())
                .code(errorCode)
                .message(message)
                .build();
    }
}
