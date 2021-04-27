package com.auth.framework.core.tokens.jwt.manager;

import com.auth.framework.core.constants.AuthenticationConstants;
import com.auth.framework.core.exceptions.KillSessionException;
import com.auth.framework.core.tokens.jwt.JsonWebToken;
import com.auth.framework.core.tokens.jwt.repository.TokenRepository;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class SessionManagerImpl implements SessionManager {

    private final TokenRepository tokenRepository;

    public SessionManagerImpl(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }


    @Override
    public void killSession(String user, String sessionName, HttpServletRequest request) throws KillSessionException {
        String currentSessionName = request.getHeader(AuthenticationConstants.USER_AGENT_HEADER_NAME);
        boolean equals = currentSessionName.equals(sessionName);
        log.info("killSession with params {}, {}, {}, {}", user, sessionName, currentSessionName, equals);
        if (equals)
            throw new KillSessionException("Can't kill active session " + sessionName);
        log.info("tokenRepository.deleteByUsernameAndSession(user, sessionName)");
        tokenRepository.deleteByUsernameAndSession(user, sessionName);
    }

    @Override
    public List<String> getAllSessionsForUsername(String username) {
        return tokenRepository.findByOwner(username)
                .stream()
                .map(JsonWebToken::getSessionName)
                .collect(Collectors.toList());
    }
}
