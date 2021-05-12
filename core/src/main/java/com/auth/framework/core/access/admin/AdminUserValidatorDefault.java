package com.auth.framework.core.access.admin;

import com.auth.framework.core.users.UserPrincipal;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.List;

public class AdminUserValidatorDefault implements AdminUserValidator {

    private final List<String> adminNames;

    public AdminUserValidatorDefault() {
        this.adminNames = Arrays.asList("administrator",
                "admin",
                "role_admin",
                "role_administrator");
    }

    @Override
    public boolean isAdmin(UserPrincipal userDetails) {
        if (userDetails == null)
            return false;
        if (userDetails.getAuthorities() == null || userDetails.getAuthorities().isEmpty())
            return false;
        return adminNames
                .stream()
                .anyMatch(admin -> userDetails.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .anyMatch(admin::equalsIgnoreCase));
    }
}
