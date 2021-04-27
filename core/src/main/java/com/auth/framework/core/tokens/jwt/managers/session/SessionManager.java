package com.auth.framework.core.tokens.jwt.managers.session;

import com.auth.framework.core.exceptions.KillSessionException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface SessionManager {

    void killSession(String user, Map<String, Object> sessionDetails, HttpServletRequest request) throws KillSessionException;

    List<Session> getAllSessionsForUsername(String username);
}
