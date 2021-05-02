package com.auth.framework.registration.token.password.generator;

import com.auth.framework.registration.token.password.RedisPasswordToken;

import java.util.UUID;

public class PasswordTokenGeneratorImpl implements PasswordTokenGenerator {

    @Override
    public synchronized RedisPasswordToken generateToken(String username, Integer durationInMinutes) {
        String token;
        synchronized (this) {
            token = UUID.randomUUID().toString().replace("-", "");
        }
        return new RedisPasswordToken(username, token, durationInMinutes);
    }
}
