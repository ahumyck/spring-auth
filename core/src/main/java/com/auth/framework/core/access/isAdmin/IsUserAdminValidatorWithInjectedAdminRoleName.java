package com.auth.framework.core.access.isAdmin;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class IsUserAdminValidatorWithInjectedAdminRoleName implements IsUserAdminValidator {

    private final String adminRoleName;

    public IsUserAdminValidatorWithInjectedAdminRoleName(String adminRoleName) {
        this.adminRoleName = adminRoleName;
    }

    @Override
    public boolean isAdmin(UserDetails userDetails) {
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
