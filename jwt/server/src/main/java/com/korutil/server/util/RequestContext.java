package com.korutil.server.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.StringUtils;

import java.util.List;

public class RequestContext {

    private RequestContext() {
        throw new IllegalStateException("Utility class");
    }

    private static final ThreadLocal<HttpServletRequest> requestThreadLocal = new ThreadLocal<>();

    public static void setRequest(HttpServletRequest request) {
        requestThreadLocal.set(request);
    }

    public static HttpServletRequest getRequest() {
        return requestThreadLocal.get();
    }

    // 클라이언트의 IP 주소를 반환 (프록시 처리 포함)
    public static String getClientIp() {
        HttpServletRequest request = requestThreadLocal.get();
        if (request != null) {
            List<String> headerNames = List.of(
                    "X-Forwarded-For",
                    "Proxy-Client-IP",
                    "WL-Proxy-Client-IP",
                    "HTTP_CLIENT_IP",
                    "HTTP_X_FORWARDED_FOR"
            );

            String clientIp = headerNames.stream()
                    .map(request::getHeader)
                    .filter(StringUtils::hasText)
                    .findFirst()
                    .orElse(request.getRemoteAddr());

            if( StringUtils.hasText(clientIp) ) {
                clientIp = clientIp.split(",")[0].trim();
            }
            return clientIp;
        }
        return "";
    }

    public static void clear() {
        requestThreadLocal.remove();
    }
}
