package com.auth.framework.core.jwt.redis;

import com.auth.framework.core.encryption.EncryptionService;
import com.auth.framework.core.jwt.Token;
import com.auth.framework.core.jwt.factory.TokenFactory;
import lombok.SneakyThrows;

public class RedisTokenFactory implements TokenFactory {

    private final EncryptionService encryptionService;

    public RedisTokenFactory(EncryptionService encryptionService) {
        this.encryptionService = encryptionService;
    }

    @Override
    @SneakyThrows
    public Token createToken(String username, String rawToken, Integer duration) {
        return new RedisJWT(username, encryptionService.encrypt(rawToken), duration);

    }
}
