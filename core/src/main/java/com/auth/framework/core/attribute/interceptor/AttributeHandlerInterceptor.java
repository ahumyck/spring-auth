package com.auth.framework.core.attribute.interceptor;

import com.auth.framework.core.attribute.AttributeConfigurer;
import com.auth.framework.core.attribute.Predicates;
import com.auth.framework.core.users.UserPrincipal;
import lombok.extern.slf4j.Slf4j;
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
            log.info("authentication => {}", authentication);
            UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
            log.info("principal => {}", principal);
            for (Map.Entry<AntPathRequestMatcher, Predicates<UserPrincipal>> predicatesByMatcher : configurer.getPredicatesByMatchers().entrySet()) {
                AntPathRequestMatcher antPathRequestMatcher = predicatesByMatcher.getKey();
                if (antPathRequestMatcher.matches(request)) {
                    Predicates<UserPrincipal> predicates = predicatesByMatcher.getValue();
                    boolean applyResult = predicates.apply(principal);
                    if (!applyResult) {
                        log.warn("User with name {} has no attributes to get access for {}", principal.getUsername(), request.getRequestURI());
                    }
                    return applyResult;
                }
            }
        } catch (ClassCastException | NullPointerException e) {
            log.error("Unexpected exception occurred", e);
            return false;
        }
        return true;
    }
}
