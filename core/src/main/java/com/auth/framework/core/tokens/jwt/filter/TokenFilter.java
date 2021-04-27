package com.auth.framework.core.tokens.jwt.filter;

import com.auth.framework.core.constants.AuthenticationConstants;
import com.auth.framework.core.tokens.jwt.JsonWebToken;
import com.auth.framework.core.tokens.jwt.managers.TokenManager;
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
import java.util.Collections;
import java.util.Map;
import java.util.Optional;


/**
 * Фильтр для аутентификации на основе токенов
 */
@Slf4j
public class TokenFilter extends OncePerRequestFilter {


    private final TokenManager manager;
    private final UserPrincipalService principalService;
    private final HeaderList headerList;

    public TokenFilter(TokenManager manager,
                       UserPrincipalService principalService, HeaderList headerList) {
        this.manager = manager;
        this.principalService = principalService;
        this.headerList = headerList;
    }

    public TokenFilter(TokenManager manager,
                       UserPrincipalService principalService) {
        this.manager = manager;
        this.principalService = principalService;
        this.headerList = new HeaderList(Collections.singletonList(AuthenticationConstants.IMPERSONALIZATION_PARAMETER));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        try {
            Optional<JsonWebToken> optionalToken = manager.validateAndGetToken(request, headerList);
            if (optionalToken.isPresent()) {
                JsonWebToken jsonWebToken = optionalToken.get();

                fillRequestAttributes(request, jsonWebToken);
                fillResponseHeader(response, jsonWebToken);

                String owner = jsonWebToken.getOwner();
                UserPrincipal userPrincipal = principalService.loadUserByUsername(owner);
                PrincipalAuthenticationToken authenticationToken = new PrincipalAuthenticationToken(userPrincipal);
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            } else {
                SecurityContextHolder.clearContext();
            }
        } catch (Exception e) {
            log.error("Error validating token: ", e);
            SecurityContextHolder.clearContext();
        }
        filterChain.doFilter(request, response);
    }

    private void fillRequestAttributes(HttpServletRequest request, JsonWebToken jsonWebToken) {
        Map<String, Object> parameters = jsonWebToken.getParameters();
        for (String header : headerList.getHeaders()) {
            request.setAttribute(header, parameters.get(header));
        }
    }

    private void fillResponseHeader(HttpServletResponse response, JsonWebToken jsonWebToken) {
        Map<String, Object> parameters = jsonWebToken.getParameters();
        for (String header : headerList.getHeaders()) {
            response.setHeader(header, (String) parameters.get(header));
        }
    }
}
