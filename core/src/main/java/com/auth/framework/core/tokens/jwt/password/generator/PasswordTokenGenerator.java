package com.auth.framework.core.tokens.jwt.password.generator;

import com.auth.framework.core.tokens.jwt.password.PasswordToken;

public interface PasswordTokenGenerator {
    PasswordToken generateToken(String username, Integer durationInMinutes);
}
