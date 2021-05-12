package com.diplom.impl.oauth2;

import com.auth.framework.core.encryption.generator.RandomPasswordGenerator;
import com.auth.framework.core.exceptions.TokenGenerationException;
import com.auth.framework.core.tokens.jwt.managers.TokenManager;
import com.auth.framework.core.users.AnonymousUserPrincipal;
import com.auth.framework.core.users.PrincipalAuthenticationToken;
import com.auth.framework.core.users.UserPrincipal;
import com.auth.framework.core.users.UserPrincipalService;
import com.auth.framework.oauth.oauth2.DefaultOAuth2UserPrincipal;
import com.auth.framework.oauth.oauth2.OAuth2SuccessHandler;
import com.diplom.impl.exceptions.UserCreationException;
import com.diplom.impl.service.UserService;
import com.diplom.impl.utils.AuthenticationConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

@Slf4j
@Component
public class OAuth2OnSuccessHandler implements OAuth2SuccessHandler {

    private final UserService userService;
    private final RandomPasswordGenerator passwordGenerator;
    private final UserPrincipalService userPrincipalService;
    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    private final TokenManager manager;

    @Autowired
    public OAuth2OnSuccessHandler(UserService userService,
                                  RandomPasswordGenerator passwordGenerator,
                                  UserPrincipalService userPrincipalService, TokenManager manager) {
        this.userService = userService;
        this.passwordGenerator = passwordGenerator;
        this.userPrincipalService = userPrincipalService;
        this.manager = manager;
    }

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       Authentication authentication) throws IOException {
        String username = "";
        try {
            DefaultOAuth2UserPrincipal oAuth2UserPrincipal = (DefaultOAuth2UserPrincipal) authentication.getPrincipal();
            log.info("oauth2 principal => {}", oAuth2UserPrincipal);
            String name = oAuth2UserPrincipal.getName();
            String email = oAuth2UserPrincipal.getEmail();
            String password = passwordGenerator.generatePasswordThenEncodeAsBase64();
            Collection<? extends GrantedAuthority> authorities = oAuth2UserPrincipal.getAuthorities();
            try {
                userService.createUserCommon(email, name, password, false, authorities);
            } catch (UserCreationException e) {
                log.info("User with params: name - {}, email - {} already exists", name, email);
            }

            UserPrincipal userPrincipal = userPrincipalService.loadUserByUsername(name);
            PrincipalAuthenticationToken authenticationToken = new PrincipalAuthenticationToken(userPrincipal);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            manager.createTokenForUsername(response, userPrincipal.getUsername(),
                    Collections.singletonMap(AuthenticationConstants.USER_AGENT_HEADER_NAME,
                            request.getHeader(AuthenticationConstants.USER_AGENT_HEADER_NAME)));

            log.info("userPrincipal {} is set to security context", userPrincipal);

        } catch (ClassCastException e) {
            log.error("Unable to cast authentication to DefaultOAuth2UserPrincipal", e);
            setAnonymousUser();
        } catch (UserCreationException e) {
            log.error("Unable to create user", e);
            setAnonymousUser();
        } catch (TokenGenerationException e) {
            log.error("Unable to generate token for user {}", username, e);
            setAnonymousUser();
        }

        String targetUrl = "/hello";

        if (response.isCommitted()) {
            log.info("Response has already been committed. Unable to redirect to {}", targetUrl);
            return;
        }

        redirectStrategy.sendRedirect(request, response, targetUrl);
    }


    private void setAnonymousUser() {
        UserPrincipal anonymousUserPrincipal = new AnonymousUserPrincipal();
        PrincipalAuthenticationToken authenticationToken = new PrincipalAuthenticationToken(anonymousUserPrincipal);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        log.info("anonymous user is set to security context");
    }
}
