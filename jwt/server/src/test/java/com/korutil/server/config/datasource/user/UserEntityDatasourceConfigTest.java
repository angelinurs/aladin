package com.korutil.server.config.datasource.user;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.iv.RandomIvGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserEntityDatasourceConfigTest {

    @Test
    @DisplayName("jasypt 암호화/복호화")
    void encodeByJasypt() {

        String dbPassword = "naru0914";
        String jasyptPassword = "korutil";
        String algorithm = "PBEWithHMACSHA512AndAES_256";

        // 암호화 설정
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword(jasyptPassword);                       // 암복호화 키
        encryptor.setAlgorithm(algorithm);  // 강력한 알고리즘
        encryptor.setIvGenerator(new RandomIvGenerator());      // IV 생성기

        // 암호화 실행
        String encryptedText = encryptor.encrypt(dbPassword);
        // 복호화 실행
        String decryptedText = encryptor.decrypt(encryptedText);

        System.out.println("암호화된 텍스트: " + encryptedText);
        System.out.println("복호화된 텍스트: " + decryptedText);

        // 원본과 복호화된 텍스트 비교
        Assertions.assertEquals(dbPassword, decryptedText);

    }

}