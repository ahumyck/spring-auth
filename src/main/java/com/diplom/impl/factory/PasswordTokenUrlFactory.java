package com.diplom.impl.factory;

import com.auth.framework.core.tokens.jwt.password.PasswordToken;
import com.auth.framework.core.tokens.jwt.password.manager.PasswordTokenManager;
import org.springframework.stereotype.Component;

@Component
public class PasswordTokenUrlFactory {

    private final PasswordTokenManager manager;

    public PasswordTokenUrlFactory(PasswordTokenManager manager) {
        this.manager = manager;
    }

    public String generateUrl(String username) {
        PasswordToken passwordToken = manager.createPasswordTokenForUsername(username);
        String token = passwordToken.getToken();
        return "http://localhost:8080/password?username=" + username + "&token=" + token;
    }
}
