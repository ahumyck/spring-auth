package com.auth.framework.registration.token.password.manager;

import com.auth.framework.registration.token.password.RedisPasswordToken;

public interface PasswordTokenManager {

    RedisPasswordToken createPasswordTokenForUsername(String username);

    boolean isTokenValid(String username, String token);

    void deleteToken(String username);
}
