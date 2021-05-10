package com.auth.framework.core.tokens.jwt.repository;


import com.auth.framework.core.tokens.jwt.JsonWebToken;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Date;

@Slf4j
public class CacheGarbageCollector implements Runnable {

    private final Integer timeToLive;
    private final TokenRepository tokenRepository;

    public CacheGarbageCollector(TokenRepository tokenRepository, Integer timeToLive) {
        this.tokenRepository = tokenRepository;
        this.timeToLive = timeToLive;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(timeToLive / 2);
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
