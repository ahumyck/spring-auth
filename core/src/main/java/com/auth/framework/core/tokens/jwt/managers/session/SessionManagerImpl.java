package com.auth.framework.core.tokens.jwt.managers.session;

import com.auth.framework.core.exceptions.KillSessionException;
import com.auth.framework.core.tokens.jwt.JsonWebToken;
import com.auth.framework.core.tokens.jwt.managers.TokenManager;
import com.auth.framework.core.tokens.jwt.params.TokenParameters;
import com.auth.framework.core.tokens.jwt.repository.TokenRepository;
import com.auth.framework.core.users.UserPrincipal;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public class SessionManagerImpl implements SessionManager {

    private final TokenRepository tokenRepository;
    private final TokenManager manager;

    public SessionManagerImpl(TokenRepository tokenRepository, TokenManager manager) {
        this.tokenRepository = tokenRepository;
        this.manager = manager;
    }


    @Override
    public void killSession(UserPrincipal principal, Session session, HttpServletRequest request) throws KillSessionException {
        String username = principal.getUsername();
        String sessionOwner = session.getUsername();

        if (username.equals(sessionOwner)) {
            Optional<JsonWebToken> possibleJWT = manager.validateAndGetToken(request);
            if (possibleJWT.isPresent()) {
                JsonWebToken jsonWebToken = possibleJWT.get();

                log.info(
                        "trying to kill session with params: username - {}, session parameters - {}",
                        username, jsonWebToken.getTokenParameters()
                );

                if (Objects.equals(session.getParameters(), jsonWebToken.getTokenParameters().asMap())) {
                    log.warn("Can't kill session that is currently using");
                    throw new KillSessionException("Can't kill active session " + session);
                }
                tokenRepository.deleteByUsernameAndSession(username, new TokenParameters(session.getParameters()));
            } else {
                log.warn("request had no token");
            }
        } else {
            log.warn("Principal username {} and session username {} do not match", username, sessionOwner);
        }

    }


    @Override
    public List<Session> getAllSessionsForUsername(String username) {
        return tokenRepository
                .findByOwner(username)
                .stream()
                .map(Session::new)
                .collect(Collectors.toList());
    }

}
