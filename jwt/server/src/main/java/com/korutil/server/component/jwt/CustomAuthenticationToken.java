package com.korutil.server.component.jwt;

import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Getter
public class CustomAuthenticationToken extends UsernamePasswordAuthenticationToken {
    private final String secretKey;
    private final String userAgent;

    // 인증 전용 생성자
    public CustomAuthenticationToken(Object principal, Object credentials, String secretKey, String userAgent) {
        super(principal, credentials);
        this.secretKey = secretKey;
        this.userAgent = userAgent;
        setAuthenticated(false);
    }

    // 인증 후 생성자
    public CustomAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities, String secretKey, String userAgent) {
        super(principal, credentials, authorities);
        this.secretKey = secretKey;
        this.userAgent = userAgent;
    }
}

