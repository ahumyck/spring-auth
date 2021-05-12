package com.auth.framework.core.access.admin;

import com.auth.framework.core.users.UserPrincipal;

public interface AdminUserValidator {

    boolean isAdmin(UserPrincipal userDetails);
}
