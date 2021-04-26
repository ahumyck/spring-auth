package com.auth.framework.core.encryption.generator;

import java.security.SecureRandom;
import java.util.Base64;

public class RandomPasswordGeneratorImpl implements RandomPasswordGenerator {

    private final SecureRandom secureRandom = new SecureRandom();
    private final int defaultPasswordLength = 64;

    @Override
    public String generatePasswordThenEncodeAsBase64() {
        byte[] passwordAsBytes = new byte[defaultPasswordLength];
        secureRandom.nextBytes(passwordAsBytes);
        return Base64.getEncoder().encodeToString(passwordAsBytes);
    }

    @Override
    public byte[] generatePasswordAsRawBytes() {
        byte[] password = new byte[defaultPasswordLength];
        secureRandom.nextBytes(password);
        return password;
    }
}
