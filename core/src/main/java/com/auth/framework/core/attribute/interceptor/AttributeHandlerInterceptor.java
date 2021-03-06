package com.auth.framework.core.attribute.interceptor;

import com.auth.framework.core.attribute.AttributeConfigurer;
import com.auth.framework.core.attribute.Predicates;
import com.auth.framework.core.users.UserPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Slf4j
public class AttributeHandlerInterceptor implements HandlerInterceptor {

    private final AttributeConfigurer configurer;

    public AttributeHandlerInterceptor(AttributeConfigurer configurer) {
        this.configurer = configurer;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            log.debug("authentication => {}", authentication);
            for (Map.Entry<AntPathRequestMatcher, Predicates<UserPrincipal>> predicatesByMatcher : configurer.getPredicatesByMatchers().entrySet()) {
                AntPathRequestMatcher antPathRequestMatcher = predicatesByMatcher.getKey();
                if (antPathRequestMatcher.matches(request)) {
                    UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
                    Predicates<UserPrincipal> predicates = predicatesByMatcher.getValue();
                    boolean applyResult = predicates.test(principal);
                    if (!applyResult) {
                        log.debug("User '{}' has no attributes to get access for '{}'",
                                principal.getUsername(),
                                request.getRequestURI());
                        response.setStatus(HttpStatus.UNAUTHORIZED.value());
                        response.sendError(HttpStatus.UNAUTHORIZED.value(), "You have not attributes to get access for this page");
                    }
                    return applyResult;
                }
            }
        } catch (ClassCastException | NullPointerException e) {
            log.error("Unexpected exception occurred", e);
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "User has no attributes to get access for " + request.getRequestURI());
            return false;
        }
        return true;
    }
}
