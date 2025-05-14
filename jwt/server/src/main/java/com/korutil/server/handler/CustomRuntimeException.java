package com.korutil.server.handler;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class CustomRuntimeException extends RuntimeException {

    private final ErrorCode errorCode;
    private final String details;  // 예외에 대한 추가적인 설명

    // 기본 생성자
    public CustomRuntimeException(ErrorCode errorCode) {
        super(errorCode.getMessage()); // 기본 메시지 설정
        this.errorCode = errorCode;
        this.details = null; // 디테일이 없으면 null
        logError();
    }

    // 메시지 + 디테일을 포함하는 생성자
    public CustomRuntimeException(ErrorCode errorCode, String details) {
        super(errorCode.getMessage() + ": " + details); // 메시지 + 디테일
        this.errorCode = errorCode;
        this.details = details;
        logError();
    }

    // 로그를 출력하는 메서드
    private void logError() {
        String message = details == null ? errorCode.getMessage() : errorCode.getMessage() + ": " + details;
        switch (errorCode.getLogLevel()) {
            case WARN -> log.warn(message);
            case ERROR -> log.error(message);
            case DEBUG -> log.debug(message);
            default -> log.info(message);
        }
    }
}
