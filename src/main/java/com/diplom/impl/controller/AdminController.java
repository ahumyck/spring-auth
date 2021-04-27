package com.diplom.impl.controller;

import com.auth.framework.core.action.Action;
import com.auth.framework.core.action.executor.ActionExecutor;
import com.auth.framework.core.constants.AuthenticationConstants;
import com.auth.framework.core.exceptions.UserHasNoAccessException;
import com.auth.framework.core.tokens.jwt.managers.TokenManager;
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
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;

import static com.diplom.impl.ImplApplication.ADMIN_ROLE_NAME;

@RestController(value = "/admin")
@Slf4j
public class AdminController {

    private final GrantedAuthority adminAuthority = new SimpleGrantedAuthority(ADMIN_ROLE_NAME);


    @Autowired
    private TokenManager tokenManager;

    @Autowired
    private ActionExecutor actionExecutor;


    @PostMapping(value = "/impersonalization")
    public TokenParameters impersonalizeFor(HttpServletRequest request,
                                            HttpServletResponse response,
                                            @RequestBody UsernameRequestBody body) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final String username = body.getUsername();
        try {
            UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
            return (TokenParameters) actionExecutor.executeAs(principal, new Action() {
                @Override
                public Object execute() {
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
        }
        return new TokenParameters(Collections.emptyMap());
    }
}
