package com.auth.framework.core.tokens.password.repository;

import com.auth.framework.core.tokens.password.PasswordToken;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.stream.Collectors;

public class RedisPasswordTokenRepository implements PasswordTokenRepository {

    private final HashOperations<String, Object, Object> hashOperations;
    private static final String KEY = "PASS_TOKEN";

    public RedisPasswordTokenRepository(RedisTemplate<String, PasswordToken> redisTemplate) {
        this.hashOperations = redisTemplate.opsForHash();
    }

    @Override
    public List<PasswordToken> findAll() {
        return hashOperations
                .values(KEY)
                .stream()
                .filter(PasswordToken.class::isInstance)
                .map(PasswordToken.class::cast)
                .collect(Collectors.toList());
    }

    @Override
    public void save(PasswordToken token) {
        hashOperations.put(KEY, token.getOwner(), token);
    }

    @Override
    public boolean validate(String username, String token) {
        PasswordToken passwordToken = find(username);
        if (passwordToken == null) return false;
        return passwordToken.getToken().equals(token);
    }

    @Override
    public PasswordToken find(String username) {
        return (PasswordToken) hashOperations.get(KEY, username);
    }

    @Override
    public void remove(String owner) {
        hashOperations.delete(KEY, owner);
    }
}
