package com.auth.framework.core.tokens.jwt.factory;

import com.auth.framework.core.encryption.EncryptionService;
import com.auth.framework.core.tokens.jwt.JsonWebToken;
import com.auth.framework.core.tokens.jwt.JsonWebTokenImpl;
import lombok.SneakyThrows;

import java.util.Map;

public class TokenFactoryImpl implements TokenFactory {

    private final EncryptionService encryptionService;

    public TokenFactoryImpl(EncryptionService encryptionService) {
        this.encryptionService = encryptionService;
    }


    @Override
    @SneakyThrows
    public JsonWebToken createToken(String username,
                                    String rawToken,
                                    Integer duration,
                                    Map<String, Object> parameters) {
        return new JsonWebTokenImpl(username,
                encryptionService.encrypt(rawToken),
                duration,
                parameters);
    }
}
