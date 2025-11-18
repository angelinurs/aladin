import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.iv.RandomIvGenerator;
import org.junit.jupiter.api.Test;

public class JasyptTest {

    @Test
    void encrypt() {
        // 암호화할 평문
        String plainText = "--PASSWORD--";
        // 암호화 키 (설정과 동일하게)
        String password = "sc301";
        // 알고리즘 (설정과 동일하게)
        String algorithm = "PBEWithHMACSHA512AndAES_256";

        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword(password);
        encryptor.setAlgorithm(algorithm);
        encryptor.setIvGenerator(new RandomIvGenerator());

        String encryptedText = encryptor.encrypt(plainText);
        System.out.println("ENC(" + encryptedText + ")");
    }

    @Test
    void decrypt() {
        // 복호화할 암호화된 텍스트 (ENC() 안의 값만)
        String encryptedText = "pNSVT+sPkBURyPFOdcnED3CZrwNTP6f6FASaHOlHVplCjsOaBlwMSFFhFhd6zcTT"; // 실제 암호화된 값으로 교체
        // 암호화 키 (설정과 동일하게)
        String password = "sc301";
        // 알고리즘 (설정과 동일하게)
        String algorithm = "PBEWithHMACSHA512AndAES_256";

        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword(password);
        encryptor.setAlgorithm(algorithm);
        encryptor.setIvGenerator(new RandomIvGenerator());

        String decryptedText = encryptor.decrypt(encryptedText);
        System.out.println("Decrypted: " + decryptedText);
    }
}
