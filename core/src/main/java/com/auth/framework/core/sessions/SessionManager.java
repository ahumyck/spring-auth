package com.auth.framework.core.sessions;

import com.auth.framework.core.exceptions.KillSessionException;
import com.auth.framework.core.tokens.jwt.JsonWebToken;
import com.auth.framework.core.users.UserPrincipal;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.function.Predicate;

public interface SessionManager {

    List<Session> getAllSessionsForUsername(String username);

    List<Session> getFilteredSessionForUsername(String username, Predicate<? super JsonWebToken>... predicates);

    void killSession(Session session) throws KillSessionException;
}
