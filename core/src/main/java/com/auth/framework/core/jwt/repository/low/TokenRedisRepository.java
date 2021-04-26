package com.auth.framework.core.jwt.repository.low;

import com.auth.framework.core.jwt.Token;
import com.auth.framework.core.jwt.redis.RedisJWT;

import java.util.Collection;

public interface TokenRedisRepository {

    RedisJWT findByOwner(String owner);

    void deleteAllByOwner(Iterable<String> owners);

    Collection<Token> findAll();

    void save(Token token);

    void deleteToken(Token token);

    void deleteTokenByOwner(String owner);
}
