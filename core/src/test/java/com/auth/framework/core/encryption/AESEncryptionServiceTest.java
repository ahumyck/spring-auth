package com.auth.framework.core.encryption;

import com.auth.framework.exceptions.EncryptionException;
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
        String encryptionResult = service.encrypt(message);
        Assertions.assertEquals(encryptedMessage, encryptionResult);
    }

    @Test
    public void decryptTest() throws EncryptionException {
        String decrypt = service.decrypt(encryptedMessage);
        Assertions.assertEquals(message, decrypt);
    }

    @Test
    public void bitAttack() {
        try {
            String bitAttack = encryptedMessage.replace("U", "a");
            String decrypt = service.decrypt(bitAttack);
            Assertions.fail(decrypt);
        } catch (EncryptionException exception) {
            Assertions.assertTrue(true);
        }
    }

}