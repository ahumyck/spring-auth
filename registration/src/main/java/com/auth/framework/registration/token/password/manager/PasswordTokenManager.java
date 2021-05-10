package com.auth.framework.registration.token.password.manager;

import com.auth.framework.registration.token.password.PasswordToken;

public interface PasswordTokenManager {

    PasswordToken createPasswordTokenForUsername(String username);

    boolean isTokenValid(String username, String token);

    void deleteToken(String username);
}
