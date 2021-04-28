package com.auth.framework.core.access.admin;

import org.springframework.security.core.userdetails.UserDetails;

public interface AdminUserValidator {

    boolean isAdmin(UserDetails userDetails);
}
