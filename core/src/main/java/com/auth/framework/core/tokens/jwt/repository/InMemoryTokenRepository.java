package com.auth.framework.core.tokens.jwt.repository;

import com.auth.framework.core.tokens.jwt.JsonWebToken;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
public class InMemoryTokenRepository implements TokenRepository {

    private final Map<String, List<JsonWebToken>> storage = new ConcurrentHashMap<>();

    @Override
    public Collection<JsonWebToken> findAll() {
        log.info("Executing find all for json web tokens");
        return storage.values().stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    @Override
    public void save(JsonWebToken jsonWebToken) {
        log.info("saving jwt {}", jsonWebToken);
        List<JsonWebToken> tokens = storage.getOrDefault(jsonWebToken.getOwner(), new ArrayList<>());
        tokens.add(jsonWebToken);
        storage.put(jsonWebToken.getOwner(), tokens);

    }

    @Override
    public JsonWebToken findTokenByParameters(String owner, Map<String, Object> tokenParameters) {
        log.info("looking for user {} token ", owner);

        List<JsonWebToken> tokens = storage.get(owner);
        if (tokens != null) {
            JsonWebToken jsonWebToken = tokens
                    .stream()
                    .filter(token -> Objects.equals(token.getTokenParameters(), tokenParameters))
                    .findAny()
                    .orElse(null);
            if (jsonWebToken == null) {
                log.warn("User has tokens, but i cant find token with params: owner = {}, params = {}",
                        owner,
                        tokenParameters);
            }
            return jsonWebToken;
        }
        log.info("username {} has no tokens", owner);
        return null;
    }

    @Override
    public Collection<JsonWebToken> findByOwner(String owner) {
        log.info("looking for username {} tokens", owner);
        List<JsonWebToken> tokens = storage.get(owner);
        if (tokens != null) {
            return Collections.unmodifiableList(tokens);
        } else {
            log.info("username {} has no tokens", owner);
            return Collections.emptyList();
        }
    }

    @Override
    public void deleteToken(JsonWebToken jsonWebToken) {
        String username = jsonWebToken.getOwner();
        log.info("Invalidation json web token by owner {}", username);

        List<JsonWebToken> tokens = storage.get(username);
        if (tokens != null) {
            tokens.removeIf(token -> token.equals(jsonWebToken));
        } else {
            log.warn("TokenRepository doesn't contain token for username {}",
                    username);
            return;
        }
        log.warn("User has tokens, but i cant find token with params: owner = {}, params = {}",
                username,
                jsonWebToken.getTokenParameters());
    }

    @Override
    public JsonWebToken findByUsernameAndRawToken(String username, String rawToken) {
        log.info("Looking token for username {} and rawToken {}", username, rawToken);
        List<JsonWebToken> tokens = storage.get(username);
        if (tokens != null) {
            return tokens
                    .stream()
                    .filter(token -> token.getRawToken().equals(rawToken))
                    .findAny()
                    .orElse(null);
        }
        log.warn("DB doesnt have token with username {} and rawToken {}", username, rawToken);
        return null;
    }

    @Override
    public void deleteByOwnerAndParameters(String username, Map<String, Object> parameters) {
        log.info("Invalidation json web token by owner {}", username);

        List<JsonWebToken> tokens = storage.get(username);
        if (tokens != null) {
            tokens.removeIf(token -> Objects.equals(token.getTokenParameters(), parameters));
        } else {
            log.warn("TokenRepository doesn't contain token for username {}", username);
            return;
        }
        log.warn("User has tokens, but i cant find token with params: owner = {}, params = {}",
                username,
                parameters);
    }

    @Override
    public void deleteAllUserTokens(String owner) {
        log.info("Invalidation json web token by owner {}", owner);
        storage.remove(owner);
    }
}
