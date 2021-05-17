package com.auth.framework.core.tokens.jwt.filter;

import com.auth.framework.core.tokens.jwt.JsonWebToken;
import com.auth.framework.core.tokens.jwt.managers.TokenManager;
import com.auth.framework.core.users.AnonymousUserPrincipal;
import com.auth.framework.core.users.PrincipalAuthenticationToken;
import com.auth.framework.core.users.UserPrincipal;
import com.auth.framework.core.users.UserPrincipalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;


/**
 * Фильтр для аутентификации на основе токенов
 */
@Slf4j
public class TokenFilter extends OncePerRequestFilter {


    private final TokenManager manager;
    private final UserPrincipalService principalService;

    public TokenFilter(TokenManager manager,
                       UserPrincipalService principalService) {
        this.manager = manager;
        this.principalService = principalService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        try {
            log.debug("request details => {}", request.getRequestURI());
            Optional<JsonWebToken> optionalToken = manager.validateAndGetToken(request);
            if (optionalToken.isPresent()) {
                JsonWebToken jsonWebToken = optionalToken.get();
                String owner = jsonWebToken.getOwner();

                UserPrincipal userPrincipal = principalService.loadUserByUsername(owner);
                userPrincipal.putAll(jsonWebToken.getTokenParameters());

                PrincipalAuthenticationToken authenticationToken = new PrincipalAuthenticationToken(userPrincipal);
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                log.debug("putActualInformation {}", userPrincipal);
            } else {
                putAnonymousUserPrincipal();
            }
        } catch (Exception e) {
            log.debug("Error validating token: ", e);
            putAnonymousUserPrincipal();
        }
        filterChain.doFilter(request, response);
    }

    private void putAnonymousUserPrincipal() {
        UserPrincipal anonymousUserPrincipal = new AnonymousUserPrincipal();
        PrincipalAuthenticationToken authenticationToken = new PrincipalAuthenticationToken(anonymousUserPrincipal);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        log.debug("putAnonymousUserPrincipal information");
    }


}
