package com.auth.framework.core.encryption;

import com.auth.framework.exceptions.EncryptionException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class AESEncryptionService implements EncryptionService {

    private final byte[] password;

    // AES-GCM parameters
    private static final int AES_KEY_SIZE = 128; //bits
    private static final int GCM_NONCE_LENGTH = 12; // bytes
    private static final int GCM_TAG_LENGTH = 16; // bytes


    public AESEncryptionService(byte[] password) {
        this.password = password;
    }

    private SecretKey getSecretKey(SecureRandom secureRandom) throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(AES_KEY_SIZE, secureRandom);
        return keyGen.generateKey();
    }

    private byte[] generateBytes(SecureRandom secureRandom, int length) {
        final byte[] bytes = new byte[length];
        secureRandom.nextBytes(bytes);
        return bytes;
    }

    private byte[] generateNonce(SecureRandom secureRandom) {
        return generateBytes(secureRandom, GCM_NONCE_LENGTH);
    }

    private byte[] generateAAD(SecureRandom secureRandom) {
        return generateBytes(secureRandom, 16);
    }

    private GCMParameterSpec generateSpec(SecureRandom secureRandom) {
        return new GCMParameterSpec(GCM_TAG_LENGTH * 8, generateNonce(secureRandom));
    }

    @Override
    public String encrypt(String input) throws EncryptionException {
        try {
            SecureRandom random = new SecureRandom(password);
            SecretKey secretKey = getSecretKey(random);

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding", "SunJCE");
            GCMParameterSpec spec = generateSpec(random);

            cipher.init(Cipher.ENCRYPT_MODE, secretKey, spec);
            cipher.updateAAD(generateAAD(random));
            return new String(Base64.getEncoder().encode(cipher.doFinal(input.getBytes())));
        } catch (Exception e) {
            throw new EncryptionException(e);
        }
    }

    @Override
    public String decrypt(String input) throws EncryptionException {
        try {
            SecureRandom random = new SecureRandom(password);
            SecretKey secretKey = getSecretKey(random);

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding", "SunJCE");
            GCMParameterSpec spec = generateSpec(random);

            cipher.init(Cipher.DECRYPT_MODE, secretKey, spec);
            cipher.updateAAD(generateAAD(random));
            Base64.getDecoder().decode(input.getBytes());
            return new String(cipher.doFinal(Base64.getDecoder().decode(input.getBytes())));
        } catch (Exception e) {
            throw new EncryptionException(e);
        }
    }
}
