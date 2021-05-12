package com.auth.framework.oauth.oauth2;

import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

@ToString
public class DefaultOAuth2UserPrincipal implements OAuth2UserPrincipal {

    private final OAuth2User user;

    public DefaultOAuth2UserPrincipal(OAuth2User user) {
        this.user = user;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return user.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getAuthorities();
    }

    public String getEmail() {
        return user.getAttribute("email");
    }

    @Override
    public String getName() {
        return user.getAttribute("name");
    }
}
