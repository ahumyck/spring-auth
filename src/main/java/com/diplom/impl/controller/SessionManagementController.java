package com.diplom.impl.controller;


import com.auth.framework.core.exceptions.KillSessionException;
import com.auth.framework.core.sessions.Session;
import com.auth.framework.core.sessions.SessionManager;
import com.auth.framework.core.users.UserPrincipal;
import com.diplom.impl.requestBody.SessionsResponseBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;


@Slf4j
@RestController
@RequestMapping(value = "/sessions")
public class SessionManagementController {

    @Autowired
    private SessionManager sessionManager;


    @PostMapping(value = "/current")
    public List<Session> getSessions() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
            return sessionManager.getAllSessionsForUsername(principal.getUsername());
        } catch (ClassCastException e) {
            log.warn("Can't get active session for unlogged user {}", authentication.getPrincipal(), e);
            return Collections.emptyList();
        }
    }

    @PostMapping(value = "/kill")
    public SessionsResponseBody killSession(HttpServletResponse response,
                                            @RequestBody Session session) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
            if (principal.getUsername().equals(session.getUsername())) {
                sessionManager.killSession(session);
                return new SessionsResponseBody(sessionManager.getAllSessionsForUsername(principal.getUsername()));
            }
            throw new KillSessionException("Can't kill session of other user");
        } catch (KillSessionException e) {
            response.sendError(400, e.getMessage());
        } catch (ClassCastException e) {
            log.warn("Can't get active session for unauthorized user {}", authentication.getPrincipal(), e);
            response.sendError(400, e.getMessage());
        }
        return null;
    }


}
