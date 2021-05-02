package com.diplom.impl.factory;

import com.auth.framework.registration.token.password.RedisPasswordToken;
import com.auth.framework.registration.token.password.manager.PasswordTokenManager;
import org.springframework.stereotype.Component;

@Component
public class PasswordTokenUrlFactory {

    private final PasswordTokenManager manager;

    public PasswordTokenUrlFactory(PasswordTokenManager manager) {
        this.manager = manager;
    }

    public String generateUrl(String username) {
        RedisPasswordToken redisPasswordToken = manager.createPasswordTokenForUsername(username);
        String token = redisPasswordToken.getToken();
        return "http://localhost:8080/password?username=" + username + "&token=" + token;
    }
}
