package com.auth.framework.core.tokens.jwt.managers;

import com.auth.framework.core.encryption.EncryptionService;
import com.auth.framework.core.exceptions.EncryptionException;
import com.auth.framework.core.exceptions.ResolveOwnerException;
import com.auth.framework.core.exceptions.TokenGenerationException;
import com.auth.framework.core.tokens.jwt.JsonWebToken;
import com.auth.framework.core.tokens.jwt.identity.IdentityProvider;
import com.auth.framework.core.tokens.jwt.repository.TokenRepository;
import com.auth.framework.core.tokens.jwt.transport.TokenTransport;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class TokenManagerImpl implements TokenManager {

    private final IdentityProvider identityProvider;
    private final TokenRepository tokenRepository;
    private final TokenTransport transport;
    private final EncryptionService encryptionService;


    public TokenManagerImpl(IdentityProvider identityProvider,
                            TokenRepository tokenRepository,
                            TokenTransport transport,
                            EncryptionService encryptionService) {
        this.identityProvider = identityProvider;
        this.tokenRepository = tokenRepository;
        this.transport = transport;
        this.encryptionService = encryptionService;
    }

    @Override
    public void createTokenForUsername(HttpServletResponse response,
                                       String username,
                                       Map<String, Object> parameters) throws TokenGenerationException {
        JsonWebToken jsonWebToken = tokenRepository.findTokenByParameters(username, parameters);
        if (jsonWebToken == null) {
            log.info("Creating token with params: username = {}, params = {}",
                    username, parameters);
            jsonWebToken = identityProvider.generateTokenForUser(username, parameters);
            tokenRepository.save(jsonWebToken);
        }
        transport.addToken(response, jsonWebToken);
    }

    @Override
    public Optional<JsonWebToken> validateAndGetToken(HttpServletRequest request) {
        Optional<String> possibleJWT = transport.extractRawToken(request);
        if (possibleJWT.isPresent()) {
            String rawToken = possibleJWT.get();
            String decrypted = tryDecrypt(rawToken);
            String owner = tryResolve(decrypted);
            JsonWebToken jsonWebToken = tryFindByOwner(owner, rawToken);
            if (jsonWebToken != null)
                return Optional.of(jsonWebToken);
        }
        return Optional.empty();
    }


    private String tryDecrypt(String input) {
        if (input == null) return null;
        try {
            return encryptionService.decrypt(input);
        } catch (EncryptionException e) {
            log.error("Unable to decrypt token {} from request", input, e);
        }
        return null;
    }

    private String tryResolve(String input) {
        if (input == null) return null;
        try {
            return identityProvider.resolveOwner(input);
        } catch (ResolveOwnerException e) {
            log.error("Unable to resolve owner for token {} from request", input, e);
        }
        return null;
    }

    private JsonWebToken tryFindByOwner(String username, String rawToken) {
        if (username == null) return null;
        try {
            return tokenRepository.findByUsernameAndRawToken(username, rawToken);
        } catch (Exception e) {
            log.error("Unable to find token with params: username - {}, rawToken - {}",
                    username, rawToken, e);
        }
        return null;
    }


}
