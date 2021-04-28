package com.diplom.impl.controller;

import com.auth.framework.core.action.Action;
import com.auth.framework.core.action.executor.ActionExecutor;
import com.auth.framework.core.constants.AuthenticationConstants;
import com.auth.framework.core.exceptions.ActionExecutionException;
import com.auth.framework.core.exceptions.KillSessionException;
import com.auth.framework.core.exceptions.UserHasNoAccessException;
import com.auth.framework.core.tokens.jwt.managers.TokenManager;
import com.auth.framework.core.tokens.jwt.managers.session.Session;
import com.auth.framework.core.tokens.jwt.managers.session.SessionManager;
import com.auth.framework.core.tokens.jwt.params.TokenParameters;
import com.auth.framework.core.users.UserPrincipal;
import com.diplom.impl.requestBody.UsernameRequestBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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

import static com.diplom.impl.ImplApplication.ADMIN_ROLE_NAME;

@Slf4j
@RestController
@RequestMapping(value = "/admin")
public class AdminController {

    private final GrantedAuthority adminAuthority = new SimpleGrantedAuthority(ADMIN_ROLE_NAME);


    @Autowired
    private TokenManager tokenManager;

    @Autowired
    private ActionExecutor actionExecutor;

    @Autowired
    private SessionManager sessionManager;


    @PostMapping(value = "/impersonalization")
    public TokenParameters impersonalizeFor(HttpServletRequest request,
                                            HttpServletResponse response,
                                            @RequestBody UsernameRequestBody body) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final String username = body.getUsername();
        try {
            UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
            return actionExecutor.executeAs(principal, new Action<TokenParameters>() {
                @Override
                public TokenParameters execute() {
                    TokenParameters parameters = TokenParameters
                            .getBuilder()
                            .addParameter(AuthenticationConstants.IMPERSONALIZATION_PARAMETER, username)
                            .build();
                    tokenManager.createTokenForUsername(request, response, username, parameters);
                    response.addHeader(AuthenticationConstants.IMPERSONALIZATION_PARAMETER, username);
                    return parameters;
                }

                @Override
                public GrantedAuthority getAuthority() {
                    return adminAuthority;
                }
            });
        } catch (ClassCastException e) {
            log.warn("Unable to get user principal from SecurityContextHolder", e);
        } catch (UserHasNoAccessException e) {
            log.warn("User principal has no rights to impersonalize for user {}", username, e);
        } catch (ActionExecutionException e) {
            log.warn("Exception while executing action: ", e);
        }
        return new TokenParameters(Collections.emptyMap());
    }


    @PostMapping(value = "/sessions/forUser")
    public List<Session> getSessions(@RequestBody UsernameRequestBody usernameRequestBody) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
            return actionExecutor.executeAs(principal, new Action<List<Session>>() {
                @Override
                public List<Session> execute() {
                    return sessionManager.getAllSessionsForUsername(usernameRequestBody.getUsername());
                }

                @Override
                public GrantedAuthority getAuthority() {
                    return adminAuthority;
                }
            });
        } catch (ClassCastException e) {
            log.warn("Can't get active session for unlogged user {}", authentication.getPrincipal(), e);
            return Collections.emptyList();
        } catch (UserHasNoAccessException | ActionExecutionException e) {
            log.warn("Unable to execute action: ", e);
            return Collections.emptyList();
        }
    }

    @PostMapping(value = "/sessions/kill")
    public List<Session> killSession(HttpServletRequest request,
                                     HttpServletResponse response,
                                     @RequestBody Session session) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
            return actionExecutor.executeAs(principal, new Action<List<Session>>() {
                @Override
                public List<Session> execute() throws ActionExecutionException {
                    try {
                        sessionManager.killSession(principal, session, request);
                        return sessionManager.getAllSessionsForUsername(principal.getUsername());
                    } catch (KillSessionException e) {
                        throw new ActionExecutionException(e);
                    }
                }

                @Override
                public GrantedAuthority getAuthority() {
                    return adminAuthority;
                }
            });
        } catch (ClassCastException e) {
            log.warn("Can't get active session for unlogged user {}", authentication.getPrincipal(), e);
            return Collections.emptyList();
        } catch (UserHasNoAccessException | ActionExecutionException e) {
            log.warn("Unable to execute action: ", e);
            return Collections.emptyList();
        }
    }
}
