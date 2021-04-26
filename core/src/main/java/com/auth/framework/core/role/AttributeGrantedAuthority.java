package com.auth.framework.core.role;

import org.springframework.security.core.GrantedAuthority;

import java.util.Objects;

public class AttributeGrantedAuthority implements GrantedAuthority {

    private static final long serialVersionUID = -6803274190309219139L;
    private final String attributeName;

    public AttributeGrantedAuthority(String attributeName) {
        this.attributeName = attributeName;
    }

    @Override
    public String getAuthority() {
        return attributeName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AttributeGrantedAuthority)) return false;
        AttributeGrantedAuthority that = (AttributeGrantedAuthority) o;
        return Objects.equals(attributeName, that.attributeName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(attributeName);
    }
}
