package com.auth.framework.core.tokens.jwt.repository;


import com.auth.framework.core.tokens.jwt.JsonWebToken;
import com.auth.framework.core.tokens.jwt.JsonWebTokenImpl;
import com.auth.framework.core.tokens.jwt.factory.TokenFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

class InMemoryTokenRepositoryTest {

    private final Integer timeToLive = 1;
    private final TokenRepository tokenRepository = new InMemoryTokenRepository();
    private final CacheGarbageCollector runnable = new CacheGarbageCollector(tokenRepository, timeToLive);
    private final TokenFactory factory = JsonWebTokenImpl::new;

    @BeforeEach
    public void prepareData() {
        tokenRepository.save(
                factory.createToken("user1", "token11", timeToLive, Collections.emptyMap())
        );
        tokenRepository.save(
                factory.createToken("user1", "token12", timeToLive, Collections.emptyMap())
        );
        tokenRepository.save(
                factory.createToken("user3", "token3", timeToLive, Collections.emptyMap())
        );
    }


    @Test
    public void addAndReadTokenTest() {
        JsonWebToken token = factory.createToken(
                "newUser",
                "newToken",
                timeToLive,
                Collections.emptyMap());

        tokenRepository.save(token);
        JsonWebToken tokenFromDataBase = tokenRepository.findByUsernameAndRawToken("newUser","newToken");
        Assertions.assertEquals(token, tokenFromDataBase);
    }

    @Test
    public void readAllUserTokens() {
        Collection<JsonWebToken> user1Tokens = tokenRepository.findByOwner("user1");
        Assertions.assertEquals(2, user1Tokens.size());

        Iterator<JsonWebToken> iterator = user1Tokens.iterator();
        Assertions.assertEquals("token11", iterator.next().getRawToken());
        Assertions.assertEquals("token12", iterator.next().getRawToken());
    }

    @Test
    public void deleteToken() {
        tokenRepository.deleteByOwnerAndParameters("user3", Collections.emptyMap());
        JsonWebToken token = tokenRepository.findTokenByParameters("user3", Collections.emptyMap());
        Assertions.assertNull(token);
    }

    @Test
    public void autoDelete() throws InterruptedException {
        Thread garbageCollectorThread = new Thread(runnable);
        garbageCollectorThread.start();
        Thread.sleep(timeToLive * 60 * 1000L + 500L);
        Collection<JsonWebToken> all = tokenRepository.findAll();
        Assertions.assertEquals(0, all.size());
    }


}