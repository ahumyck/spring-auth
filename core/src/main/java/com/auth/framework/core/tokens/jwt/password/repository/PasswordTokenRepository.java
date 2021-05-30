package com.auth.framework.core.tokens.jwt.password.repository;

import com.auth.framework.core.tokens.jwt.password.PasswordToken;

import java.util.List;

public interface PasswordTokenRepository {

    List<PasswordToken> findAll();

    void save(PasswordToken passwordToken);

    boolean validate(String username, String token);

    PasswordToken find(String username);

    void remove(String owner);
}
