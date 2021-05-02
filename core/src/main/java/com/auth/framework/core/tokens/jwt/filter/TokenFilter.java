package com.auth.framework.core.tokens.jwt.filter;

import com.auth.framework.core.tokens.jwt.JsonWebToken;
import com.auth.framework.core.tokens.jwt.managers.TokenManager;
import com.auth.framework.core.users.AnonymousUserPrincipal;
import com.auth.framework.core.users.PrincipalAuthenticationToken;
import com.auth.framework.core.users.UserPrincipal;
import com.auth.framework.core.users.UserPrincipalService;
import lombok.extern.slf4j.Slf4j;
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
            log.info("filtering");
            Optional<JsonWebToken> optionalToken = manager.validateAndGetToken(request);
            if (optionalToken.isPresent()) {
                JsonWebToken jsonWebToken = optionalToken.get();
                String owner = jsonWebToken.getOwner();
                UserPrincipal userPrincipal = principalService.loadUserByUsername(owner);

                jsonWebToken
                        .getTokenParameters()
                        .forEach(userPrincipal::putParameter);


                PrincipalAuthenticationToken authenticationToken = new PrincipalAuthenticationToken(userPrincipal);
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                log.info("put actual information");
            } else {
                UserPrincipal anonymousUserPrincipal = new AnonymousUserPrincipal();
                PrincipalAuthenticationToken authenticationToken = new PrincipalAuthenticationToken(anonymousUserPrincipal);
                log.info("put fake information");
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        } catch (Exception e) {
            log.error("Error validating token: ", e);
            SecurityContextHolder.clearContext();
        }
        filterChain.doFilter(request, response);
    }


}
