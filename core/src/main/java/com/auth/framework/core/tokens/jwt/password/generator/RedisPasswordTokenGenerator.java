package com.auth.framework.core.tokens.jwt.password.generator;

import com.auth.framework.core.tokens.jwt.password.PasswordToken;
import com.auth.framework.core.tokens.jwt.password.RedisPasswordToken;

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
