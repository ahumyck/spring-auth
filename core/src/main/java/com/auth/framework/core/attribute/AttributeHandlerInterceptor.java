package com.auth.framework.core.attribute;

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
//        log.info("rules => {}", configurer.rules);
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//            log.info("preHandling request: {}, {}", request.getRequestURL().toString(), request.getRequestURI());
            for (Map.Entry<String, Predicates<UserPrincipal>> predicatesByPattern : configurer.rules.entrySet()) {
                String patternKey = predicatesByPattern.getKey();
//                log.info("preHandling patternKey {} for request", patternKey);
                AntPathRequestMatcher antPathRequestMatcher = new AntPathRequestMatcher(patternKey);
                if (antPathRequestMatcher.matches(request)) {
//                    log.info("patternKey {} matched for request", patternKey);
                    UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
                    Predicates<UserPrincipal> predicates = predicatesByPattern.getValue();
                    boolean applyResult = predicates.apply(principal);
//                    log.info("apply result for patternKey {}, principal {} and request: {}",
//                            patternKey, principal.getUsername(), apply);
                    if (!applyResult) {
                        String errorMsg = "User" + principal.getUsername() +
                                "has no attributes to get access for " + request.getRequestURI();
                        log.warn(errorMsg);
                        response.sendError(403, errorMsg);
                    }
                    return applyResult;
                }
            }
        } catch (ClassCastException e) {
            log.info("Impossible to apply attributes rules for unauthorized user", e);
        }
        return true;
    }
}
