package com.auth.framework.core.tokens.jwt.manager;

import com.auth.framework.core.exceptions.KillSessionException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface SessionManager {

    void killSession(String user, String sessionName, HttpServletRequest request) throws KillSessionException;

    List<String> getAllSessionsForUsername(String username);
}
