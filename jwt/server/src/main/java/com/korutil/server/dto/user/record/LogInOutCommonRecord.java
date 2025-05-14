package com.korutil.server.dto.user.record;

import java.util.Map;

public record LogInOutCommonRecord(
        Long userId,
        String username,
        String password,
        Map<String,String> header,
        String userAgent
) {
    public static LogInOutCommonRecord from( LoginRequest request, Map<String, String> headers) {
        return new LogInOutCommonRecord( null, request.username(), request.password(), headers, headers.get("user-agent"));
    }

    public static LogInOutCommonRecord from( LogoutRequest request, Map<String, String> headers) {
        return new LogInOutCommonRecord(null, request.username(), null, headers, headers.get("user-agent"));
    }
}
