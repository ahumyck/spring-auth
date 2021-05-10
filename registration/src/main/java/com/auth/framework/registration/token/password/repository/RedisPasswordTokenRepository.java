package com.auth.framework.registration.token.password.repository;

import com.auth.framework.registration.token.password.PasswordToken;
import com.auth.framework.registration.token.password.RedisPasswordToken;
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
