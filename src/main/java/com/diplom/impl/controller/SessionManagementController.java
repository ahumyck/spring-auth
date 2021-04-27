package com.diplom.impl.controller;


import com.auth.framework.core.exceptions.KillSessionException;
import com.auth.framework.core.tokens.jwt.manager.SessionManager;
import com.diplom.impl.requestBody.SessionsResponseBody;
import com.diplom.impl.requestBody.UsernameRequestBody;
import com.diplom.impl.requestBody.UsernameSessionBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@Slf4j
public class SessionManagementController {

    @Autowired
    private SessionManager sessionManager;


    @PostMapping(value = "/sessions")
    public SessionsResponseBody getSessions(@RequestBody UsernameRequestBody body) {
        return new SessionsResponseBody(sessionManager.getAllSessionsForUsername(body.getUsername()));
    }

    @PostMapping(value = "/killSession")
    public String killSession(HttpServletRequest request, @RequestBody UsernameSessionBody body) {
        try {
            log.info("Trying to kill session {}", body);
            sessionManager.killSession(body.getUsername(), body.getSessionName(), request);
            return body + " was killed";
        } catch (KillSessionException e) {
            return "Can't kill myself: " + e.getMessage();
        }
    }


}
