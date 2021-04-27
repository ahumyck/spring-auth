package com.auth.framework.core.tokens.jwt.managers.session;

import com.auth.framework.core.constants.AuthenticationConstants;
import com.auth.framework.core.exceptions.KillSessionException;
import com.auth.framework.core.tokens.jwt.JsonWebToken;
import com.auth.framework.core.tokens.jwt.params.TokenParameters;
import com.auth.framework.core.tokens.jwt.repository.TokenRepository;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class SessionManagerImpl implements SessionManager {

    private final TokenRepository tokenRepository;

    public SessionManagerImpl(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }


    @Override
    public void killSession(String user, Map<String, Object> sessionDetails, HttpServletRequest request) throws KillSessionException {
        String currentSessionName = request.getHeader(AuthenticationConstants.USER_AGENT_HEADER_NAME);
        String sessionName = (String) sessionDetails.get(AuthenticationConstants.SESSION_PARAMETER);
        TokenParameters parameters = new TokenParameters(sessionDetails);
        log.info("killSession with params {}, {}, {}, {}", user, sessionName, currentSessionName, parameters);
        JsonWebToken jsonWebToken = tokenRepository.findTokenByParameters(user, sessionName, parameters);
        log.info("json web token => " + jsonWebToken);
        if (currentSessionName.equals(sessionName) && jsonWebToken != null)
            throw new KillSessionException("Can't kill active session " + sessionName);
        log.info("tokenRepository.deleteToken(jsonWebToken)");
        tokenRepository.deleteByUsernameAndSession(user, sessionName, parameters);
    }


    @Override
    public List<Session> getAllSessionsForUsername(String username) {
        return tokenRepository.findByOwner(username)
                .stream()
                .map(Session::new)
                .collect(Collectors.toList());
    }

}
