package com.auth.framework.core.access.isAdmin;

import org.springframework.security.core.userdetails.UserDetails;

public interface IsUserAdminValidator {

    boolean isAdmin(UserDetails userDetails);
}
