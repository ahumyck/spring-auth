package com.auth.framework.core.tokens.password.manager;

import com.auth.framework.core.tokens.password.PasswordToken;
import com.auth.framework.core.tokens.password.generator.PasswordTokenGenerator;
import com.auth.framework.core.tokens.password.repository.PasswordTokenRepository;

public class PasswordTokenManagerImpl implements PasswordTokenManager {

    private final PasswordTokenGenerator generator;
    private final PasswordTokenRepository storage;
    private final Integer timeToLive;

    public PasswordTokenManagerImpl(PasswordTokenGenerator generator, PasswordTokenRepository storage, Integer timeToLive) {
        this.generator = generator;
        this.storage = storage;
        this.timeToLive = timeToLive;
    }

    @Override
    public PasswordToken createPasswordTokenForUsername(String username) {
        if (storage.find(username) != null) {
            storage.remove(username);
        }
        PasswordToken passwordToken = generator.generateToken(username, timeToLive);
        storage.save(passwordToken);
        return passwordToken;
    }

    @Override
    public boolean isTokenValid(String username, String token) {
        return storage.validate(username, token);
    }

    @Override
    public void deleteToken(String username) {
        storage.remove(username);
    }

}
