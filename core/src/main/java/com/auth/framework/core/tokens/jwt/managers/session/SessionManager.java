package com.auth.framework.core.tokens.jwt.managers.session;

import com.auth.framework.core.exceptions.KillSessionException;
import com.auth.framework.core.users.UserPrincipal;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface SessionManager {

    List<Session> getAllSessionsForUsername(String username);

    void killSession(UserPrincipal principal, Session session, HttpServletRequest request) throws KillSessionException;
}
