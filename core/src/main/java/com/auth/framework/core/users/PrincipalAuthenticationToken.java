package com.auth.framework.core.users;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class PrincipalAuthenticationToken extends UsernamePasswordAuthenticationToken {

    private static final long serialVersionUID = -2621796543939175975L;
    private Map<String, Object> parameters = null;

    public PrincipalAuthenticationToken(Object principal, Object credentials) {
        super(principal, credentials);
        this.parameters = Collections.emptyMap();
    }

    public PrincipalAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
        handleParameters(parameters);
    }

    public PrincipalAuthenticationToken(Object principal,
                                        Object credentials,
                                        Collection<? extends GrantedAuthority> authorities,
                                        Map<String, Object> parameters) {
        this(principal, credentials, authorities);
        handleParameters(parameters);
    }

    public PrincipalAuthenticationToken(UserPrincipal userPrincipal, Object credentials) {
        super(userPrincipal, credentials, userPrincipal.getAuthorities());
        handleParameters(userPrincipal.getParameters());
    }

    public PrincipalAuthenticationToken(UserPrincipal userPrincipal) {
        this(userPrincipal, null, userPrincipal.getAuthorities(), userPrincipal.getParameters());
        handleParameters(userPrincipal.getParameters());
    }

    private void handleParameters(Map<String, Object> parameters) {
        if (parameters == null) {
            this.parameters = Collections.emptyMap();
        } else {
            this.parameters = Collections.unmodifiableMap(parameters);
        }
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }
}
