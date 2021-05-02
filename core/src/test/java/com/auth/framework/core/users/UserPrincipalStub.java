package com.auth.framework.core.users;

import lombok.Builder;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Builder
public class UserPrincipalStub implements UserPrincipal {

    private static final long serialVersionUID = -4297399468815252962L;
    private final String username;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;
    private final Map<String, Object> parameters;

    public UserPrincipalStub(String username, String password,
                             Collection<? extends GrantedAuthority> authorities,
                             Map<String, Object> parameters) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        if (parameters == null) {
            this.parameters = new HashMap<>();
        } else {
            this.parameters = parameters;
        }
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
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

    @Override
    public Map<String, Object> getParameters() {
        return null;
    }

    @Override
    public void putParameter(String key, Object value) {
        this.parameters.put(key, value);
    }

    @Override
    public Object getParameter(String key) {
        return this.parameters.get(key);
    }

    @Override
    public boolean containsParameter(String key) {
        return this.parameters.containsKey(key);
    }

    @Override
    public void removeParameter(String key) {
        this.parameters.remove(key);
    }
}