package com.diplom.impl.oauth2;

import com.auth.framework.core.tokens.jwt.managers.TokenManager;
import com.auth.framework.core.users.AnonymousUserPrincipal;
import com.auth.framework.core.users.PrincipalAuthenticationToken;
import com.auth.framework.core.users.UserPrincipal;
import com.auth.framework.core.users.UserPrincipalService;
import com.auth.framework.exceptions.TokenGenerationException;
import com.auth.framework.oauth.oauth2.DefaultOAuth2UserPrincipal;
import com.auth.framework.oauth.oauth2.OAuth2SuccessHandler;
import com.diplom.impl.exceptions.UserCreationException;
import com.diplom.impl.service.UserService;
import com.diplom.impl.utils.AuthenticationConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

@Slf4j
@Component
public class OAuth2OnSuccessHandler implements OAuth2SuccessHandler {

    private final UserService userService;
    private final UserPrincipalService userPrincipalService;
    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    private final TokenManager manager;

    @Autowired
    public OAuth2OnSuccessHandler(UserService userService,
                                  UserPrincipalService userPrincipalService, TokenManager manager) {
        this.userService = userService;
        this.userPrincipalService = userPrincipalService;
        this.manager = manager;
    }

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       Authentication authentication) throws IOException {
        try {
            DefaultOAuth2UserPrincipal oAuth2UserPrincipal =
                    (DefaultOAuth2UserPrincipal) authentication.getPrincipal();
            userService.createOAuth2User(oAuth2UserPrincipal);
            String name = oAuth2UserPrincipal.getName();

            manager.createTokenForUsername(response, name,
                    Collections.singletonMap(AuthenticationConstants.USER_AGENT_HEADER_NAME,
                            request.getHeader(AuthenticationConstants.USER_AGENT_HEADER_NAME)));

            UserPrincipal userPrincipal = userPrincipalService.loadUserByUsername(name);
            PrincipalAuthenticationToken authenticationToken = new PrincipalAuthenticationToken(userPrincipal);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        } catch (ClassCastException | UserCreationException | TokenGenerationException e) {
            setAnonymousUser();
        }

        String targetUrl = "/whoami";
        if (response.isCommitted()) {
            return;
        }
        redirectStrategy.sendRedirect(request, response, targetUrl);
    }


    private void setAnonymousUser() {
        UserPrincipal anonymousUserPrincipal = new AnonymousUserPrincipal();
        PrincipalAuthenticationToken authenticationToken = new PrincipalAuthenticationToken(anonymousUserPrincipal);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }
}
