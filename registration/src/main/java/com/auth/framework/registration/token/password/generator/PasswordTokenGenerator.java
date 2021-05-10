package com.auth.framework.registration.token.password.generator;

import com.auth.framework.registration.token.password.PasswordToken;

public interface PasswordTokenGenerator {
    PasswordToken generateToken(String username, Integer durationInMinutes);
}
