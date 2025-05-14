package com.korutil.server.dto.jwt;

public final class CustomClaimNames {

    private CustomClaimNames() {
        throw new IllegalStateException("Static Class");
    }

    public static final String USERNAME = "username";
    public static final String EMAIL = "email";
    public static final String USER_AGENT = "user_agent";
    public static final String SECRET_KEY = "secret_key";
    public static final String TYPE = "type";
    public static final String REFRESH = "refresh";
    public static final String AUTHORITIES = "authorities";

}
