package com.auth.framework.core.tokens.password.generator;

import com.auth.framework.core.tokens.password.PasswordToken;

public interface PasswordTokenGenerator {
    PasswordToken generateToken(String username, Integer durationInMinutes);
}
