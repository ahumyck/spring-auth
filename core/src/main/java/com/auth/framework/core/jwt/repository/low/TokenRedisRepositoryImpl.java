package com.auth.framework.core.jwt.repository.low;

import com.auth.framework.core.jwt.Token;
import com.auth.framework.core.jwt.redis.RedisJWT;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Collection;
import java.util.stream.Collectors;

public class TokenRedisRepositoryImpl implements TokenRedisRepository {

    private final HashOperations<String, Object, Object> hashOperations;
    private static final String KEY = "REDIS_JWT";

    public TokenRedisRepositoryImpl(RedisTemplate<String, Token> redisTemplate) {
        this.hashOperations = redisTemplate.opsForHash();
    }

    @Override
    public RedisJWT findByOwner(String owner) {
        return (RedisJWT) hashOperations.get(KEY, owner);
    }

    @Override
    public void deleteToken(Token token) {
        hashOperations.delete(KEY, token.getOwner());
    }

    @Override
    public void deleteTokenByOwner(String owner) {
        hashOperations.delete(KEY, owner);
    }

    @Override
    public void deleteAllByOwner(Iterable<String> owners) {
        hashOperations.delete(KEY, owners);
    }

    @Override
    public Collection<Token> findAll() {
        return hashOperations
                .entries(KEY)
                .values()
                .stream()
                .map(value -> (RedisJWT) value)
                .collect(Collectors.toList());
    }

    @Override
    public void save(Token token) {
        hashOperations.put(KEY, token.getOwner(), token);
    }
}
