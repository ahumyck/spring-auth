package com.auth.framework.core.tokens.jwt.factory;

import com.auth.framework.core.encryption.EncryptionService;
import com.auth.framework.core.tokens.jwt.JsonWebToken;
import com.auth.framework.core.tokens.jwt.JsonWebTokenImpl;
import com.auth.framework.core.tokens.jwt.params.TokenParameters;
import lombok.SneakyThrows;

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
                                    String sessionName,
                                    TokenParameters parameters) {
        JsonWebTokenImpl jsonWebToken = new JsonWebTokenImpl(username, encryptionService.encrypt(rawToken), duration, sessionName);
        if (parameters != null) {
            parameters.forEach(jsonWebToken::addParameter);
        }
        return jsonWebToken;
    }
}
