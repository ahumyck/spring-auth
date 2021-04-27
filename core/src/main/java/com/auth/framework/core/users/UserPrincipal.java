package com.auth.framework.core.users;

import com.auth.framework.core.role.AttributeGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public interface UserPrincipal extends UserDetails {

    Collection<? extends AttributeGrantedAuthority> getAttributes();
}
