package com.korutil.server.handler;

import com.korutil.server.dto.common.CommonApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalResponseExceptionHandler extends ResponseEntityExceptionHandler {

    // CustomRuntimeException 처리
    @ExceptionHandler(CustomRuntimeException.class)
    public ResponseEntity<CommonApiResponse<?>> handleCustomRuntimeException(CustomRuntimeException ex) {
        return ResponseEntity.status(ex.getErrorCode().getStatus()).body(
                CommonApiResponse.error(
                        ex.getErrorCode().getStatus(),
                        ex.getErrorCode().getCode(),
                        ex.getMessage()
                )
        );
    }
}
