package com.auth.framework.core.jwt.repository;

import com.auth.framework.core.jwt.Token;
import com.auth.framework.core.jwt.repository.low.TokenRedisRepository;

import java.util.Collection;

public class RedisTokenStorage implements TokenStorage {

    private final TokenRedisRepository redisRepository;

    public RedisTokenStorage(TokenRedisRepository redisRepository) {
        this.redisRepository = redisRepository;
    }

    @Override
    public Collection<Token> findAll() {
        return redisRepository.findAll();
    }

    @Override
    public void save(Token token) {
        redisRepository.save(token);
    }

    @Override
    public Token findByOwner(String owner) {
        return redisRepository.findByOwner(owner);
    }

    @Override
    public void deleteToken(Token token) {
        redisRepository.deleteToken(token);
    }

    @Override
    public void deleteTokenByOwner(String owner) {
        redisRepository.deleteTokenByOwner(owner);
    }
}
