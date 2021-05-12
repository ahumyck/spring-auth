package com.auth.framework.registration.token.password.repository;

import com.auth.framework.registration.token.password.PasswordToken;

import java.util.List;

public interface PasswordTokenRepository {

    List<PasswordToken> findAll();

    void save(PasswordToken passwordToken);

    boolean validate(String username, String token);

    PasswordToken find(String username);

    void remove(String owner);
}
