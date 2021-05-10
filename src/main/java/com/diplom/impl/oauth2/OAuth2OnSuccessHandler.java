package com.diplom.impl.oauth2;

import com.diplom.impl.utils.AuthenticationConstants;
import com.auth.framework.core.encryption.generator.RandomPasswordGenerator;
import com.auth.framework.core.exceptions.TokenGenerationException;
import com.auth.framework.core.tokens.jwt.managers.TokenManager;
import com.auth.framework.core.users.AnonymousUserPrincipal;
import com.auth.framework.core.users.PrincipalAuthenticationToken;
import com.auth.framework.core.users.UserPrincipal;
import com.auth.framework.core.users.UserPrincipalService;
import com.auth.framework.core.users.oauth2.DefaultOAuth2UserPrincipal;
import com.diplom.impl.exceptions.UserCreationException;
import com.diplom.impl.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class OAuth2OnSuccessHandler implements AuthenticationSuccessHandler {

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
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("onAuthenticationSuccess logic called");
        handle(request, response, authentication);
        clearAuthenticationAttributes(request);

    }

    protected void handle(HttpServletRequest request,
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
            userService.createUserCommon(email, name, password, false, authorities);

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

        //TODO: redirect for controller which will redirect to front application
        String targetUrl = "/hello";

        if (response.isCommitted()) {
            log.info("Response has already been committed. Unable to redirect to {}", targetUrl);
            return;
        }

        redirectStrategy.sendRedirect(request, response, targetUrl);
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return;
        }
        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
    }

    protected String determineTargetUrl(final Authentication authentication) {

        Map<String, String> roleTargetUrlMap = new HashMap<>();
        roleTargetUrlMap.put("ROLE_USER", "/homepage.html");
        roleTargetUrlMap.put("ROLE_ADMIN", "/console.html");

        final Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (final GrantedAuthority grantedAuthority : authorities) {
            String authorityName = grantedAuthority.getAuthority();
            if (roleTargetUrlMap.containsKey(authorityName)) {
                return roleTargetUrlMap.get(authorityName);
            }
        }

        throw new IllegalStateException();
    }

    private void setAnonymousUser() {
        UserPrincipal anonymousUserPrincipal = new AnonymousUserPrincipal();
        PrincipalAuthenticationToken authenticationToken = new PrincipalAuthenticationToken(anonymousUserPrincipal);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        log.info("anonymous user is set to security context");
    }
}
