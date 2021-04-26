package com.auth.framework.core.users;

import com.auth.framework.core.role.AttributeGrantedAuthority;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class PrincipalAuthenticationToken extends UsernamePasswordAuthenticationToken {

    private static final long serialVersionUID = -2621796543939175975L;
    private Collection<AttributeGrantedAuthority> attributes;

    public PrincipalAuthenticationToken(Object principal, Object credentials) {
        super(principal, credentials);
        this.attributes = Collections.emptyList();
    }

    public PrincipalAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
        this.attributes = Collections.emptyList();
    }

    private void handleAttributes(Collection<? extends AttributeGrantedAuthority> attributes) {
        if (attributes == null) {
            this.attributes = Collections.emptyList();
        } else {
            for (AttributeGrantedAuthority attribute : attributes) {
                Assert.notNull(attribute, "Authorities collection cannot contain any null elements");
            }
            this.attributes = Collections.unmodifiableCollection(new ArrayList<>(attributes));
        }
    }

    public PrincipalAuthenticationToken(Object principal,
                                        Object credentials,
                                        Collection<? extends GrantedAuthority> authorities,
                                        Collection<? extends AttributeGrantedAuthority> attributes) {
        this(principal, credentials, authorities);
        handleAttributes(attributes);
    }

    public PrincipalAuthenticationToken(UserPrincipal userPrincipal, Object credentials) {
        super(userPrincipal, credentials, userPrincipal.getAuthorities());
        handleAttributes(userPrincipal.getAttributes());
    }

    public PrincipalAuthenticationToken(UserPrincipal userPrincipal) {
        super(userPrincipal, null, userPrincipal.getAuthorities());
        handleAttributes(userPrincipal.getAttributes());
    }

    public Collection<AttributeGrantedAuthority> getAttributes() {
        return attributes;
    }
}
