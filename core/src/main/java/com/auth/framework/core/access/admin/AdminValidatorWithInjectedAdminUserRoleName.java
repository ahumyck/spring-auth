package com.auth.framework.core.access.admin;

import com.auth.framework.core.users.UserPrincipal;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class AdminValidatorWithInjectedAdminUserRoleName implements AdminUserValidator {

    private final String adminRoleName;

    public AdminValidatorWithInjectedAdminUserRoleName(String adminRoleName) {
        this.adminRoleName = adminRoleName;
    }

    @Override
    public boolean isAdmin(UserPrincipal userDetails) {
        if (userDetails == null)
            return false;
        if (userDetails.getAuthorities() == null || userDetails.getAuthorities().isEmpty())
            return false;
        return userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(authority -> authority.equals(adminRoleName));
    }
}
