package com.diplom.impl.principal;

import com.auth.framework.core.users.UserPrincipal;
import com.diplom.impl.model.entity.Role;
import com.diplom.impl.model.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.stream.Collectors;

public class CustomUserPrincipal implements UserPrincipal {

    private static final long serialVersionUID = 2791908996117135518L;
    private final User user;
    private final Map<String, Object> parameters = new HashMap<>();

    public CustomUserPrincipal(User user) {
        this.user = user;
        this.parameters.put("date", this.user.getDate());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRoles()
                .stream()
                .map(Role::getRoleName)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toCollection(HashSet::new));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !user.isLocked();
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
        return parameters;
    }

    @Override
    public void putParameter(String key, Object value) {
        parameters.put(key, value);
    }

    @Override
    public Object getParameter(String key) {
        return parameters.get(key);
    }

    @Override
    public void putAll(Map<String, Object> parameters) {
        this.parameters.putAll(parameters);
    }

    @Override
    public boolean containsParameter(String key) {
        return parameters.containsKey(key);
    }

    @Override
    public void removeParameter(String key) {
        parameters.remove(key);
    }

    @Override
    public String toString() {
        return "CustomUserPrincipal{" +
                "user=" + user.getUsername() +
                ", parameters=" + parameters +
                '}';
    }
}
