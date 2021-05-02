package com.auth.framework.core.attribute;

import com.auth.framework.core.users.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.annotation.PostConstruct;
import java.util.Map;

public abstract class BaseAttributeConfigurerAdapter implements AttributeConfigurerAdapter {

    @Autowired
    protected AttributeConfigurer configurer;

    @PostConstruct
    private void init() {
        attributes(configurer);
        for (Map.Entry<String, Predicates<UserPrincipal>> predicatesByPattern : configurer.getPredicatesByPattern().entrySet()) {
            configurer.predicatesRules.put(new AntPathRequestMatcher(predicatesByPattern.getKey()), predicatesByPattern.getValue());
        }
    }
}
