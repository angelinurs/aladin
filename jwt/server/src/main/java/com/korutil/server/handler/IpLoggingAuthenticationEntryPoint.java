package com.medicrm_ai.uploader.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 인증되지 않은 요청(Security 인증 실패) 시 실제 요청 정보를 로그로 남기는 EntryPoint 클래스.
 * - 요청 발생 IP, 접근 경로, 쿼리, User-Agent, Security 거부 사유(예외 메시지) 등
 * - 포렌식/운영자 분석 및 공격 탐지용 로그에 활용 가능
 */
@Slf4j
@Component
public class IpLoggingAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /**
     * Security 인증 실패(403 등) 처리 메서드
     * @param request   클라이언트 요청 정보 (IP, URI, 등)
     * @param response  응답 객체 (403 등 반환용)
     * @param authException Spring Security 인증 실패 예외 정보
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty()) ip = request.getRemoteAddr();

        // 공격/요청 상세 기록
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String query = request.getQueryString();
        String userAgent = request.getHeader("User-Agent");

        String securityStatus = "FORBIDDEN";
        String exceptionMsg = (authException != null) ? authException.getMessage() : "No Exception";

        log.warn(
                "Security forbidden from IP: {} | Method: {} | URI: {} | Query: {} | UA: {} | Status: {} | Reason: {}",
                ip, method, uri, query, userAgent, securityStatus, exceptionMsg
        );

        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden");

    }
}
