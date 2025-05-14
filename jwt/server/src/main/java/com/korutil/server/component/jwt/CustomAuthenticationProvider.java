package com.korutil.server.component.jwt;

import com.korutil.server.handler.CustomRuntimeException;
import com.korutil.server.handler.ErrorCode;
import com.korutil.server.service.security.CustomUserDetailsService;
import com.korutil.server.util.PasswordUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final CustomUserDetailsService customUserDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (authentication instanceof CustomAuthenticationToken customAuthenticationToken) {
            String username = customAuthenticationToken.getName();
            String password = (String) customAuthenticationToken.getCredentials();
            String secretKey = customAuthenticationToken.getSecretKey();
            String userAgent = customAuthenticationToken.getUserAgent();

            // 1. 사용자 정보를 DB에서 조회
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

            // 2. 패스워드 검증
            if (!PasswordUtils.matches(password, userDetails.getPassword())) {
                throw new CustomRuntimeException(ErrorCode.INVALID_PASSWORD);
            }

            // 3. 인증이 성공하면 CustomAuthentication 반환
            return new CustomAuthenticationToken(userDetails, password, userDetails.getAuthorities(), secretKey, userAgent);
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return CustomAuthenticationToken.class.isAssignableFrom(authentication);
    }
}