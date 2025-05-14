package com.korutil.server.config;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Slf4j
@Configuration
public class JwtConfig {

    @Value("${jwt.secret-key}")
    private String secretKey;

    private final String ALGORITHM = "HmacSHA256";

    // JWT 발급용 Encoder (HMAC-SHA256)
    @Bean
    public JwtEncoder jwtEncoder() {
        SecretKey key = getKey();
        JWKSource<SecurityContext> jwkSource = new ImmutableSecret<>(key);
        return new NimbusJwtEncoder(jwkSource);
    }

    // JWT 검증용 Decoder
    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withSecretKey(getKey())
                .macAlgorithm(MacAlgorithm.HS256)
                .build();
    }

    // 비밀키 생성
    private SecretKey getKey() {
        byte[] decodedKey = Base64.getDecoder().decode(secretKey);
        if (decodedKey.length < 32) {
            throw new IllegalArgumentException("HMAC-SHA256 requires 256-bit key");
        }
        return new SecretKeySpec(decodedKey, ALGORITHM);
    }
}