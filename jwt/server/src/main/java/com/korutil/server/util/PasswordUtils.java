package com.korutil.server.util;

import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

public class PasswordUtils {

    private PasswordUtils() {
        throw new IllegalStateException("Utility class");
    }

    private static final PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();;

    // 비밀번호 해시 생성
    public static String generate(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    /**
     * @deprecated
     *  - Bcrypt 는 자동으로 salt, algorith 삽입
     * @apiNote 비밀번호 해시 및 salt 생성
     * @param rawPassword String
     * @param salt String
     * @return String
     */
    public static String generateSaltedPassword(String rawPassword, String salt) {
        return generate(rawPassword + salt);
    }

    /**
     * @deprecated
     *  - Bcrypt 는 자동으로 salt, algorith 삽입
     * @apiNote salt 생성 (UUID 기반으로 생성)
     * @return String
     */
    public static String generateSalt() {
        return generate(UUID.randomUUID().toString());
    }

    // 비밀번호와 해시된 비밀번호 비교
    public static boolean matches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}