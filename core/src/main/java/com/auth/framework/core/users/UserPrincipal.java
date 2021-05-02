package com.auth.framework.core.users;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;

public interface UserPrincipal extends UserDetails {

    Map<String, Object> getParameters();

    void putParameter(String key, Object value);

    Object getParameter(String key);

    boolean containsParameter(String key);

    void removeParameter(String key);
}
