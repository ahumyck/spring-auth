package com.diplom.impl.factory;

import com.auth.framework.registration.token.password.RedisPasswordToken;
import com.auth.framework.registration.token.password.manager.PasswordTokenManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PasswordTokenUrlFactoryTest {


    @Test
    public void test() {

        String username = "FuckMe";
        String token = "myOwnToken";

        PasswordTokenUrlFactory urlFactory = new PasswordTokenUrlFactory(new PasswordTokenManager() {

            @Override
            public RedisPasswordToken createPasswordTokenForUsername(String username) {
                return new RedisPasswordToken(username, token, 20);
            }

            @Override
            public boolean isTokenValid(String username, String token) {
                return true;
            }

            @Override
            public void deleteToken(String username) {

            }
        });

        String url = urlFactory.generateUrl(username);
        Assertions.assertEquals("http://localhost:8080/password?username=FuckMe&token=myOwnToken", url);
    }
}