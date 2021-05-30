package com.auth.framework.core.encryption;

import com.auth.framework.exceptions.EncryptionException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

class AESEncryptionServiceTest {

    private final EncryptionService service =
            new AESEncryptionService("EncryptionService".getBytes(StandardCharsets.UTF_8));
    private final String encryptedMessage = "Uy8o2CMAmZyMiPT7CxBFM8bQpbSNfU4+ed6O/3q3lqQ=";
    private final String message = "encryptedMessage";

    @Test
    public void encryptTest() throws EncryptionException {
        System.out.println("Тест шифрования");
        String encryptionResult = service.encrypt(message);
        System.out.printf("Результат шифрования = %s, \nожидаемый результат шифрования = %s\n",
                encryptionResult, encryptedMessage);
        Assertions.assertEquals(encryptedMessage, encryptionResult);
    }

    @Test
    public void decryptTest() throws EncryptionException {
        System.out.println("Тест дешифрования");
        String decrypt = service.decrypt(encryptedMessage);
        System.out.printf("Результат дешифрования = %s, \nожидаемый результат дешифрования = %s\n",
                decrypt, message);
        Assertions.assertEquals(message, decrypt);
    }

    @Test
    public void bitAttack() {
        try {
            System.out.println("Тест нарушения целостности данных");
            String bitAttack = encryptedMessage.replace("U", "a");
            String decrypt = service.decrypt(bitAttack);
            System.out.println("Атака произошла успешно\n");
            Assertions.fail(decrypt);
        } catch (EncryptionException exception) {
            System.out.println("Целостность данных была нарушена\n");
            Assertions.assertTrue(true);
        }
    }

}