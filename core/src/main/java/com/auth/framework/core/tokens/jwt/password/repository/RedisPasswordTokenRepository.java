package com.auth.framework.core.tokens.jwt.password.repository;

import com.auth.framework.core.tokens.jwt.password.PasswordToken;
import com.auth.framework.core.tokens.jwt.password.RedisPasswordToken;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.stream.Collectors;

public class RedisPasswordTokenRepository implements PasswordTokenRepository {

    private final HashOperations<String, Object, Object> hashOperations;
    private static final String KEY = "PASSWORD_RESET_TOKENS";

    public RedisPasswordTokenRepository(RedisTemplate<String, PasswordToken> redisTemplate) {
        this.hashOperations = redisTemplate.opsForHash();
    }

    @Override
    public List<PasswordToken> findAll() {
        return hashOperations
                .values(KEY)
                .stream()
                .filter(RedisPasswordToken.class::isInstance)
                .map(RedisPasswordToken.class::cast)
                .collect(Collectors.toList());
    }

    @Override
    public void save(PasswordToken passwordToken) {
        hashOperations.put(KEY, passwordToken.getOwner(), passwordToken);
    }

    @Override
    public boolean validate(String username, String token) {
        RedisPasswordToken redisPasswordToken = find(username);
        if (redisPasswordToken == null) return false;
        return redisPasswordToken.getToken().equals(token);
    }

    @Override
    public RedisPasswordToken find(String username) {
        return (RedisPasswordToken) hashOperations.get(KEY, username);
    }

    @Override
    public void remove(String owner) {
        hashOperations.delete(KEY, owner);
    }
}
