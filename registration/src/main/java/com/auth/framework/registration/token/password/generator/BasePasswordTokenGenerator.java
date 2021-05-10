package com.auth.framework.registration.token.password.generator;

import com.auth.framework.registration.token.password.BasePasswordToken;

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
