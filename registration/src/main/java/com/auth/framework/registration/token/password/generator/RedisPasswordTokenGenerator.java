package com.auth.framework.registration.token.password.generator;

import com.auth.framework.registration.token.password.PasswordToken;
import com.auth.framework.registration.token.password.RedisPasswordToken;

public class RedisPasswordTokenGenerator implements PasswordTokenGenerator {

    private final BasePasswordTokenGenerator basePasswordTokenGenerator;

    public RedisPasswordTokenGenerator(BasePasswordTokenGenerator basePasswordTokenGenerator) {
        this.basePasswordTokenGenerator = basePasswordTokenGenerator;
    }

    @Override
    public PasswordToken generateToken(String username, Integer durationInMinutes) {
        return new RedisPasswordToken(basePasswordTokenGenerator.generateToken(username, durationInMinutes));
    }
}
