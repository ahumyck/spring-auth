package com.auth.framework.core.tokens.jwt.repository;

import com.auth.framework.core.constants.AuthenticationConstants;
import com.auth.framework.core.tokens.jwt.JsonWebToken;
import com.auth.framework.core.tokens.jwt.params.TokenParameters;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
public class InMemoryTokenRepository implements TokenRepository {

    private final Map<String, List<JsonWebToken>> storage = new ConcurrentHashMap<>();

    public InMemoryTokenRepository(Integer timeToLive, TimeUnit unit) {

    }

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
    public JsonWebToken findTokenByParameters(String owner, String sessionName, TokenParameters tokenParameters) {
        log.info("looking for user {} session {} token", owner, sessionName);

        List<JsonWebToken> tokens = storage.get(owner);
        if (tokens != null) {
            TokenParameters copy = createCompleteCopy(sessionName, tokenParameters);
            for (JsonWebToken token : tokens) {
                if (copy.getParameters().equals(token.getParameters())) return token;
            }
        } else {
            log.warn("user {} has no tokens", owner);
        }
        log.warn("User has tokens, but i cant find token with params: owner = {}, session name = {}, params = {}",
                owner,
                sessionName,
                tokenParameters);
        return null;
    }

    @Override
    public Collection<JsonWebToken> findByOwner(String owner) {
        log.info("looking for user {} tokens", owner);
        List<JsonWebToken> tokens = storage.get(owner);
        if (tokens != null) {
            return Collections.unmodifiableList(tokens);
        } else {
            log.warn("user {} has no tokens", owner);
            return Collections.emptyList();
        }
    }

    @Override
    public void deleteToken(JsonWebToken jsonWebToken) {
        String username = jsonWebToken.getOwner();
        String sessionName = jsonWebToken.getSessionName();
        Map<String, Object> parameters = jsonWebToken.getParameters();
        log.info("Invalidation json web token by owner {}", username);

        List<JsonWebToken> tokens = storage.get(username);
        if (tokens != null) {
            tokens.removeIf(token -> token.equals(jsonWebToken));
        } else {
            log.warn("TokenRepository doesn't contain token for username {} with session {}",
                    username,
                    sessionName);
            return;
        }
        log.warn("User has tokens, but i cant find token with params: owner = {}, session name = {}, params = {}",
                username,
                sessionName,
                parameters);
    }

    @Override
    public void deleteByUsernameAndSession(String username, String sessionName, TokenParameters parameters) {
        log.info("Invalidation json web token by owner {}", username);

        List<JsonWebToken> tokens = storage.get(username);
        if (tokens != null) {
            TokenParameters copy = createCompleteCopy(sessionName, parameters);
            tokens.removeIf(token -> copy.getParameters().equals(token.getParameters()));
        } else {
            log.warn("TokenRepository doesn't contain token for username {} with session {}",
                    username,
                    sessionName);
            return;
        }
        log.warn("User has tokens, but i cant find token with params: owner = {}, session name = {}, params = {}",
                username,
                sessionName,
                parameters);
    }

    @Override
    public void deleteAllUserTokens(String owner) {
        log.info("Invalidation json web token by owner {}", owner);
        storage.remove(owner);
    }

    private TokenParameters createCompleteCopy(String sessionName, TokenParameters parameters) {
        TokenParameters.Builder builder = TokenParameters
                .getBuilder()
                .addParameter(AuthenticationConstants.SESSION_PARAMETER, sessionName);
        if (parameters != null) {
            for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                builder.addParameter(entry.getKey(), entry.getValue());
            }
        }
        return builder.build();
    }
}
