package com.korutil.server.config.datasource;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JwtConfigTest {

    @Autowired private JwtEncoder jwtEncoder;
    @Autowired private JwtDecoder jwtDecoder;

    private final String secretKey = "ymyzWpOOhXnd/ElWJ8ZUKnr3+IDfZcQhFuoaM5L6AJI=";

    @DisplayName("[1] 빈 생성 테스트")
    @Test
    void beansAreCreated() {
        assertNotNull(jwtEncoder);
        assertNotNull(jwtDecoder);
    }

    @DisplayName("[2] 키 길이 검증")
    @Test
    void secretKeyHas32Bytes() {
        SecretKey key = decodeSecretKey(secretKey);
        assertEquals(32, key.getEncoded().length);
    }

    @DisplayName("[3] 통합 테스트 (인코딩/디코딩)")
    @Test
    void encodeAndDecodeJwt() {
        // JWT 생성
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .subject("test-user")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plus(1, ChronoUnit.HOURS))
                .build();

        // 명시적 JwsHeader 사용
        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();

        // 인코딩
        Jwt jwt = jwtEncoder.encode(JwtEncoderParameters.from(header, claims));

        // 디코딩
        Jwt decoded = jwtDecoder.decode(jwt.getTokenValue());

        // 검증
        assertEquals("test-user", decoded.getSubject());
    }

    @DisplayName("[4] 잘못된 키 길이 테스트")
    @Test
    void invalidKeyLengthThrowsException() {
        String invalidKey = Base64.getEncoder().encodeToString(new byte[16]);
        assertThrows(IllegalArgumentException.class, () -> decodeSecretKey(invalidKey));
    }

    // 키 디코딩 유틸리티 메서드
    private SecretKey decodeSecretKey(String base64Key) {
        byte[] decoded = Base64.getDecoder().decode(base64Key);
        if (decoded.length != 32) {
            throw new IllegalArgumentException("키는 32바이트여야 합니다.");
        }
        return new SecretKeySpec(decoded, "HmacSHA256");
    }
}
