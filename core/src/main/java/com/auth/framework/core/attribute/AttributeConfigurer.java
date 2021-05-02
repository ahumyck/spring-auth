package com.auth.framework.core.attribute;

import com.auth.framework.core.users.UserPrincipal;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

public class AttributeConfigurer {

    protected final Map<String, Predicates<UserPrincipal>> rules = new ConcurrentHashMap<>();

    protected final Map<AntPathRequestMatcher, Predicates<UserPrincipal>> predicatesRules = new TreeMap<>(
            Comparator.comparing(AntPathRequestMatcher::getPattern).reversed()
    );

    public Map<String, Predicates<UserPrincipal>> getPredicatesByPattern() {
        return Collections.unmodifiableMap(rules);
    }

    public Map<AntPathRequestMatcher, Predicates<UserPrincipal>> getPredicatesByMatchers() {
        return Collections.unmodifiableMap(predicatesRules);
    }

    @SafeVarargs
    public final AttributeConfigurer predicatesMatchAny(String pattern, Predicate<UserPrincipal>... predicates) {
        Predicates<UserPrincipal> userPrincipalPredicates = rules.computeIfAbsent(pattern, k -> new Predicates<>());
        userPrincipalPredicates.add(PredicateType.ANY, predicates);
        rules.put(pattern, userPrincipalPredicates);
        return this;
    }

    @SafeVarargs
    public final AttributeConfigurer predicatesMatchAll(String pattern, Predicate<UserPrincipal>... predicates) {
        Predicates<UserPrincipal> userPrincipalPredicates = rules.computeIfAbsent(pattern, k -> new Predicates<>());
        userPrincipalPredicates.add(PredicateType.ALL, predicates);
        rules.put(pattern, userPrincipalPredicates);
        return this;
    }
}
