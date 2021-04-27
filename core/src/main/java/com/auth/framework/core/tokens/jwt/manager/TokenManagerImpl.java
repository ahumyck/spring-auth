package com.auth.framework.core.tokens.jwt.manager;

import com.auth.framework.core.constants.AuthenticationConstants;
import com.auth.framework.core.encryption.EncryptionService;
import com.auth.framework.core.tokens.jwt.JsonWebToken;
import com.auth.framework.core.tokens.jwt.identity.IdentityProvider;
import com.auth.framework.core.tokens.jwt.repository.TokenRepository;
import com.auth.framework.core.tokens.jwt.transport.TokenTransport;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    public void createTokenForUsername(HttpServletRequest request, HttpServletResponse response, String username) {
        String sessionName = request.getHeader(AuthenticationConstants.USER_AGENT_HEADER_NAME);
        JsonWebToken jsonWebToken = tokenRepository.findByOwnerAndSessionName(username, sessionName);
        if (jsonWebToken == null) {
            jsonWebToken = identityProvider.generateTokenForUser(username, sessionName);
            tokenRepository.save(jsonWebToken);
        }
        transport.addToken(response, jsonWebToken);
    }

    @Override
    public Optional<String> validateTokenAndGetUsername(HttpServletRequest request) {
        return transport
                .extractRawToken(request)
                .map(this::tryDecrypt)
                .map(this::tryResolve)
                .map(token -> this.tryFindByOwner(token, request.getHeader(AuthenticationConstants.USER_AGENT_HEADER_NAME)))
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

    private JsonWebToken tryFindByOwner(String input, String sessionName) {
        if (input == null) return null;
        try {
            return tokenRepository.findByOwnerAndSessionName(input, sessionName);
        } catch (Exception e) {
            log.error("Unable to find owner for token {} in DB", input, e);
        }
        return null;
    }

    private String getOwner(JsonWebToken jsonWebToken) {
        if (jsonWebToken == null) return null;
        else return jsonWebToken.getOwner();
    }


}
