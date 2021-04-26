package com.auth.framework.core.access.isAdmin;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.List;

public class IsUserAdminValidatorDefault implements IsUserAdminValidator {

    private final List<String> adminNames;

    public IsUserAdminValidatorDefault() {
        this.adminNames = Arrays.asList("administrator", "admin");
    }

    @Override
    public boolean isAdmin(UserDetails userDetails) {
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
