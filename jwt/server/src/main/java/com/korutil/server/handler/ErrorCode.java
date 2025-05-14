package com.korutil.server.handler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;

@Slf4j
@Getter
@AllArgsConstructor
public enum ErrorCode {

    ALREADY_LOGOUT(HttpStatus.NOT_FOUND, "AUTH_001", "이미 로그아웃 되었습니다", LogLevel.WARN),
    NOT_FOUND_TOKEN(HttpStatus.NOT_FOUND, "AUTH_011", "해당 토큰정보가 없습니다.", LogLevel.WARN),
    NOT_FOUND_OAUTH_PROVIDER(HttpStatus.NOT_FOUND, "AUTH_010", "해당 SOCIAL LOGIN 이 존재하지 않습니다.", LogLevel.ERROR),
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "AUTH_012", "해당 사용자가 없습니다.", LogLevel.WARN),
    NOT_FOUND_USER_PROFILE(HttpStatus.NOT_FOUND, "AUTH_013", "해당 사용자 상세 정보가 없습니다.", LogLevel.WARN),
    NOT_FOUND_CREDENTIALS(HttpStatus.NOT_FOUND, "AUTH_014", "해당 패스워드가 없습니다.", LogLevel.WARN),
    NOT_FOUND_EMAIL_VERIFICATION(HttpStatus.NOT_FOUND, "AUTH_015", "해당 이메일 인증이 없습니다.", LogLevel.WARN),

    ALREADY_EXIST_EMAIL(HttpStatus.CONFLICT, "AUTH_021", "이미 존재하는 메일입니다.", LogLevel.WARN),
    INVALID_USER_ID(HttpStatus.BAD_REQUEST, "AUTH_031", "유효하지 않은 사용자 아이디", LogLevel.WARN),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "AUTH_032", "유효하지 않은 패스워드", LogLevel.WARN),  // 인증 헤더가 잘못된 경우
    INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH_033", "유효하지 않거나 만료된 액세스 토큰", LogLevel.ERROR),  // 잘못된 액세스 토큰
    MISSING_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH_034", "액세스 토큰이 누락되었습니다.", LogLevel.ERROR),  // 액세스 토큰 누락
    UNAUTHORIZED_ACCESS(HttpStatus.UNAUTHORIZED, "AUTH_041", "허가되지 않은 접근", LogLevel.WARN),
    FAILED_EMAIL_SEND(HttpStatus.SERVICE_UNAVAILABLE, "FAIL_001", "Email 전송 실패", LogLevel.WARN),

    NOT_FOUND_TODO(HttpStatus.NOT_FOUND, "TODO_001", "해당 TODO 가 없습니다.", LogLevel.ERROR);



    private final HttpStatus status;  // HTTP 상태 코드 추가
    private final String code;
    private final String message;
    private final LogLevel logLevel;

}