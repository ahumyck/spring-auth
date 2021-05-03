package com.auth.framework.core.attribute;

import com.auth.framework.core.users.UserPrincipal;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

@ToString
@Slf4j
public class AttributeConfigurer {

    protected final Map<String, Predicates<UserPrincipal>> rules = new ConcurrentHashMap<>();

    protected final Map<AntPathRequestMatcher, Predicates<UserPrincipal>> predicatesRules = new TreeMap<>(
            (o1, o2) -> {
                String pattern1 = o1.getPattern();
                String pattern2 = o2.getPattern();

                int compare = Integer.compare(pattern2.length(), pattern1.length());
                if (compare == 0) {
                    return pattern1.compareTo(pattern2);
                } else return compare;
            }
    );

    public Map<AntPathRequestMatcher, Predicates<UserPrincipal>> getPredicatesByMatchers() {
        if (predicatesRules.size() == 0) {
            for (Map.Entry<String, Predicates<UserPrincipal>> entry : rules.entrySet()) {
                String pattern = entry.getKey();
                Predicates<UserPrincipal> value = entry.getValue();
                predicatesRules.put(new AntPathRequestMatcher(pattern), value);
            }
        }
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
