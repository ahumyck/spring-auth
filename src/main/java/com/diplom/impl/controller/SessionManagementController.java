package com.diplom.impl.controller;


import com.auth.framework.core.sessions.Session;
import com.auth.framework.core.sessions.SessionManager;
import com.auth.framework.core.users.UserPrincipal;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Slf4j
@RestController
@RequestMapping(value = "/sessions")
public class SessionManagementController {

    @Autowired
    private SessionManager sessionManager;

    @PostMapping(value = "/current")
    public List<Session> getSessions(@AuthenticationPrincipal UserPrincipal principal) {
        return sessionManager.getAllSessionsForUsername(principal.getUsername());
    }

    @PostMapping(value = "/kill")
    @SneakyThrows
    public List<Session> killSession(@RequestBody Session session,
                                     @AuthenticationPrincipal UserPrincipal principal) {
        if (principal.getUsername().equals(session.getUsername())) {
            sessionManager.killSession(session);
            return sessionManager.getAllSessionsForUsername(principal.getUsername());
        } else return null;
    }


}
