package com.auth.framework.core.jwt.manager;

import com.auth.framework.core.encryption.EncryptionService;
import com.auth.framework.core.jwt.Token;
import com.auth.framework.core.jwt.identity.IdentityProvider;
import com.auth.framework.core.jwt.repository.TokenStorage;
import com.auth.framework.core.jwt.transport.TokenTransport;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Slf4j
public class TokenManagerImpl implements TokenManager {

    private final IdentityProvider identityProvider;
    private final TokenStorage tokenStorage;
    private final TokenTransport transport;
    private final EncryptionService encryptionService;

    public TokenManagerImpl(IdentityProvider identityProvider,
                            TokenStorage tokenStorage,
                            TokenTransport transport,
                            EncryptionService encryptionService) {
        this.identityProvider = identityProvider;
        this.tokenStorage = tokenStorage;
        this.transport = transport;
        this.encryptionService = encryptionService;
    }

    @Override
    public void createTokenForUsername(HttpServletResponse response, String username) {
        Token token = tokenStorage.findByOwner(username);
        if (token == null) {
            token = identityProvider.generateTokenForUser(username);
            tokenStorage.save(token);
        }
        transport.addToken(response, token);
    }

    @Override
    public Optional<String> validateTokenAndGetUsername(HttpServletRequest request) {
        return transport
                .extractRawToken(request)
                .map(this::tryDecrypt)
                .map(this::tryResolve)
                .map(this::tryFindByOwner)
                .map(this::getOwner);
    }


    private String tryDecrypt(String input) {
        if (input == null) return null;
        try {
            return encryptionService.decrypt(input);
        } catch (Exception e) {
            log.error("Unable to decrypt token {} from request", input, e);
        }
        return null;
    }

    private String tryResolve(String input) {
        if (input == null) return null;
        try {
            return identityProvider.resolveOwner(input);
        } catch (Exception e) {
            log.error("Unable to resolve owner for token {} from request", input, e);
        }
        return null;
    }

    private Token tryFindByOwner(String input) {
        if (input == null) return null;
        try {
            return tokenStorage.findByOwner(input);
        } catch (Exception e) {
            log.error("Unable to find owner for token {} in DB", input, e);
        }
        return null;
    }

    private String getOwner(Token token) {
        if (token == null) return null;
        else return token.getOwner();
    }


}
