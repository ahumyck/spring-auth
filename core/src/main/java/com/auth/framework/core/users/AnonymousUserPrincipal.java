package com.auth.framework.core.users;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class AnonymousUserPrincipal implements UserPrincipal {

    private static final long serialVersionUID = 8984689467996223031L;

    public static final GrantedAuthority authority = new SimpleGrantedAuthority("anonymousUser");

    @Override
    public Map<String, Object> getParameters() {
        return Collections.emptyMap();
    }

    @Override
    public void putParameter(String key, Object value) {

    }

    @Override
    public Object getParameter(String key) {
        return null;
    }

    @Override
    public boolean containsParameter(String key) {
        return false;
    }

    @Override
    public void removeParameter(String key) {

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(authority);
    }

    @Override
    public String getPassword() {
        return "password";
    }

    @Override
    public String getUsername() {
        return "anonymousUser";
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
