package com.auth.framework.core.tokens.password.generator;

import com.auth.framework.core.tokens.password.PasswordToken;

import java.util.UUID;

public class PasswordTokenGeneratorImpl implements PasswordTokenGenerator {

    @Override
    public synchronized PasswordToken generateToken(String username, Integer durationInMinutes) {
        String token;
        synchronized (this) {
            token = UUID.randomUUID().toString().replace("-", "");
        }
        return new PasswordToken(username, token, durationInMinutes);
    }
}
