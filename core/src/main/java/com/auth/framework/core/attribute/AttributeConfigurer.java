package com.auth.framework.core.attribute;

import com.auth.framework.core.users.UserPrincipal;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class AttributeConfigurer {

    protected final Map<String, Predicates<UserPrincipal>> rules = new HashMap<>();

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
