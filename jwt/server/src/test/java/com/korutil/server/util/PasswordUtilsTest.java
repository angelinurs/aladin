package com.korutil.server.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordUtilsTest {

    @DisplayName("BCrypt test")
    @Test
    void testBCrypt() {

        String oldPassword = "naru";

        // 기존 비밀번호 암호화
        String oldCrypted = PasswordUtils.generate(oldPassword);

        // 원본 비밀번호와 암호화된 비밀번호를 비교하는 로직을 수정
        // 여기서는 두 비밀번호가 동일한지 확인하는 것이므로, 암호화된 값이 동일할 필요가 없다.
        Assertions.assertTrue(PasswordUtils.matches(oldPassword, oldCrypted));
    }

}