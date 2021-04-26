package com.auth.framework.core.users;

import com.auth.framework.core.role.AttributeGrantedAuthority;
import lombok.Builder;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Builder
public class UserPrincipalStub implements UserPrincipal {

    private static final long serialVersionUID = -4297399468815252962L;
    private final String username;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;
    private final Collection<? extends AttributeGrantedAuthority> attributes;

    public UserPrincipalStub(String username, String password, Collection<? extends GrantedAuthority> authorities, Collection<? extends AttributeGrantedAuthority> attributes) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.attributes = attributes;
    }


    @Override
    public Collection<? extends AttributeGrantedAuthority> getAttributes() {
        return attributes;
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
}