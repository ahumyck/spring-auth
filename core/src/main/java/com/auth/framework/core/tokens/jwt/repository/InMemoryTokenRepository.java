package com.auth.framework.core.tokens.jwt.repository;

import com.auth.framework.core.tokens.jwt.JsonWebToken;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
public class InMemoryTokenRepository implements TokenRepository {

    private final Map<String, Map<String, JsonWebToken>> storage = new ConcurrentHashMap<>();

    public InMemoryTokenRepository(Integer timeToLive, TimeUnit unit) {

    }

    @Override
    public Collection<JsonWebToken> findAll() {
        log.info("Executing find all for json web tokens");
        return storage.values().stream()
                .map(Map::values)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    @Override
    public void save(JsonWebToken jsonWebToken) {
        log.info("saving jwt {}", jsonWebToken);
        Map<String, JsonWebToken> sessionTokenMap = storage.getOrDefault(jsonWebToken.getOwner(), new ConcurrentHashMap<>());
        sessionTokenMap.put(jsonWebToken.getSessionName(), jsonWebToken);
        storage.put(jsonWebToken.getOwner(), sessionTokenMap);

    }

    public JsonWebToken findByOwnerAndSessionName(String owner, String sessionName) {
        log.info("looking for user {} session {} token", owner, sessionName);
        Map<String, JsonWebToken> sessionTokenMap = storage.get(owner);
        if (sessionTokenMap != null) {
            return sessionTokenMap.get(sessionName);
        } else {
            log.warn("user {} has no tokens", owner);
            return null;
        }
    }

    @Override
    public Collection<JsonWebToken> findByOwner(String owner) {
        log.info("looking for user {} tokens", owner);
        Map<String, JsonWebToken> sessionTokenMap = storage.get(owner);
        if (sessionTokenMap != null) {
            return Collections.unmodifiableCollection(sessionTokenMap.values());
        } else {
            log.warn("user {} has no tokens", owner);
            return Collections.emptyList();
        }
    }

    @Override
    public void deleteToken(JsonWebToken jsonWebToken) {
        deleteByUsernameAndSession(jsonWebToken.getOwner(), jsonWebToken.getSessionName());
    }

    @Override
    public void deleteByUsernameAndSession(String username, String sessionName) {
        log.info("Invalidation json web token by owner {}", username);

        Map<String, JsonWebToken> sessionTokenMap = storage.get(username);
        if (sessionTokenMap != null) {
            sessionTokenMap.remove(sessionName);
            storage.put(username, sessionTokenMap);
        } else {
            log.warn("TokenRepository doesn't contain token for username {} with session {}",
                    username,
                    sessionName);
        }
    }

    @Override
    public void deleteAllUserTokens(String owner) {
        log.info("Invalidation json web token by owner {}", owner);
        storage.remove(owner);
    }
}
