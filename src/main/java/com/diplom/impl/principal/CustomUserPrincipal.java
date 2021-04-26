package com.diplom.impl.principal;

import com.auth.framework.core.role.AttributeGrantedAuthority;
import com.auth.framework.core.users.UserPrincipal;
import com.diplom.impl.model.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

public class CustomUserPrincipal implements UserPrincipal {

    private static final long serialVersionUID = 2791908996117135518L;
    private final User user;

    public CustomUserPrincipal(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends AttributeGrantedAuthority> getAttributes() {
        return user.getAttributeSet()
                .stream()
                .map(attribute -> new AttributeGrantedAuthority(attribute.getAttributeName()))
                .collect(Collectors.toSet());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(user.getRole().getRoleName()));
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
