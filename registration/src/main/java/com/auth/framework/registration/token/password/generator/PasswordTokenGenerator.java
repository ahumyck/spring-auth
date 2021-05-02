package com.auth.framework.registration.token.password.generator;

import com.auth.framework.registration.token.password.RedisPasswordToken;

public interface PasswordTokenGenerator {
    RedisPasswordToken generateToken(String username, Integer durationInMinutes);
}
