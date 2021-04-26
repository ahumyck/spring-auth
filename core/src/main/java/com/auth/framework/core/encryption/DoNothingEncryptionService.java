package com.auth.framework.core.encryption;

public class DoNothingEncryptionService implements EncryptionService {
    @Override
    public String encrypt(String input) {
        return input;
    }

    @Override
    public String decrypt(String input) {
        return input;
    }
}
