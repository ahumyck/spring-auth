package com.auth.framework.core.tokens.jwt.repository;


import com.auth.framework.core.tokens.jwt.JsonWebToken;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Date;

@Slf4j
public class CacheGarbageCollector implements Runnable {

    private final TokenRepository tokenRepository;
    private final Integer sleepTime;

    public CacheGarbageCollector(TokenRepository tokenRepository, Integer timeToLive) {
        this.tokenRepository = tokenRepository;
        this.sleepTime = timeToLive * 60 * 500; //(300 m * 60 s / m * 1000 ml / s) / 2 = 300 * 60 * 500 ml
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(sleepTime);
                Collection<JsonWebToken> tokens = tokenRepository.findAll();
                Date now = new Date();
                for (JsonWebToken token : tokens) {
                    Date expireDate = token.getExpireDate();
                    if (now.after(expireDate)) tokenRepository.deleteToken(token);
                }

            } catch (InterruptedException e) {
                log.error("Something tried to interrupt me", e);
            }

        }
    }
}
