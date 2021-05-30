package com.auth.framework.core.tokens.jwt.password.manager;

import com.auth.framework.core.tokens.jwt.password.PasswordToken;

public interface PasswordTokenManager {

    PasswordToken createPasswordTokenForUsername(String username);

    boolean isTokenValid(String username, String token);

    void deleteToken(String username);
}
