package com.diplom.impl.controller;

import com.auth.framework.exceptions.KillSessionException;
import com.auth.framework.exceptions.TokenGenerationException;
import com.auth.framework.core.sessions.Session;
import com.auth.framework.core.sessions.SessionManager;
import com.auth.framework.core.tokens.jwt.managers.TokenManager;
import com.auth.framework.core.users.UserPrincipal;
import com.diplom.impl.principal.CustomUserPrincipalService;
import com.diplom.impl.requestBody.UsernameRequestBody;
import com.diplom.impl.utils.AuthenticationConstants;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "/admin")
public class AdminController {


    @Autowired
    private CustomUserPrincipalService userPrincipalService;

    @Autowired
    private TokenManager tokenManager;

    @Autowired
    private SessionManager sessionManager;


    @PostMapping(value = "/impersonalization")
    public UserPrincipal impersonalizeFor(HttpServletRequest request,
                                          HttpServletResponse response,
                                          @RequestBody UsernameRequestBody body) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal principal = null;
        final String username = body.getUsername();
        try {
            principal = (UserPrincipal) authentication.getPrincipal();
            Map<String, Object> parameters = new HashMap<>();

            parameters.put(AuthenticationConstants.IMPERSONALIZATION_BY,
                    principal.getUsername());

            parameters.put(AuthenticationConstants.USER_AGENT_HEADER_NAME,
                    request.getHeader(AuthenticationConstants.USER_AGENT_HEADER_NAME));

            tokenManager.createTokenForUsername(response, username, parameters);

            UserPrincipal impersonalizationPrincipal = userPrincipalService.loadUserByUsername(username);

            impersonalizationPrincipal.putParameter(AuthenticationConstants.IMPERSONALIZATION_BY,
                    principal.getUsername());
            return impersonalizationPrincipal;
        } catch (ClassCastException e) {
            log.warn("Unable to get user principal from SecurityContextHolder", e);
        } catch (TokenGenerationException e) {
            log.warn("Unable to generate token for impersonalization", e);
        }
        return principal;
    }


    @PostMapping(value = "/sessions/forUser")
    public List<Session> getSessions(@RequestBody UsernameRequestBody usernameRequestBody) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            return sessionManager.getAllSessionsForUsername(usernameRequestBody.getUsername());
        } catch (ClassCastException e) {
            log.warn("Can't get active session for unlogged user {}", authentication.getPrincipal(), e);
            return Collections.emptyList();
        }
    }

    @PostMapping(value = "/sessions/kill")
    public List<Session> killSession(@RequestBody Session session) {
        try {
            sessionManager.killSession(session);
            return sessionManager.getAllSessionsForUsername(session.getUsername());
        } catch (KillSessionException e) {
            log.warn("Unable to kill session: ", e);
            return Collections.emptyList();
        }
    }
}
