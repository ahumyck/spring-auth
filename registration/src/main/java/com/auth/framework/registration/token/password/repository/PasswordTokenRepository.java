package com.auth.framework.registration.token.password.repository;

import com.auth.framework.registration.token.password.RedisPasswordToken;

import java.util.List;

public interface PasswordTokenRepository {

    List<RedisPasswordToken> findAll();

    void save(RedisPasswordToken redisPasswordToken);

    boolean validate(String username, String token);

    RedisPasswordToken find(String username);

    void remove(String owner);
}
