package com.auth.framework.core.tokens.jwt.password.generator;

import com.auth.framework.core.tokens.jwt.password.BasePasswordToken;

import java.util.UUID;

public class BasePasswordTokenGenerator implements PasswordTokenGenerator {

    @Override
    public BasePasswordToken generateToken(String username, Integer durationInMinutes) {
        String token;
        synchronized (this) { //чтобы случайно не смогло получиться два одинаковых токена
            token = UUID.randomUUID().toString().replace("-", "");
        }
        return new BasePasswordToken(username, token, durationInMinutes);
    }
}
