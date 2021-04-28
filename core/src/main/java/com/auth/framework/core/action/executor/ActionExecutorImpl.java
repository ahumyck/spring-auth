package com.auth.framework.core.action.executor;

import com.auth.framework.core.access.admin.AdminUserValidator;
import com.auth.framework.core.action.Action;
import com.auth.framework.core.exceptions.ActionExecutionException;
import com.auth.framework.core.exceptions.UserHasNoAccessException;
import com.auth.framework.core.role.AttributeGrantedAuthority;
import com.auth.framework.core.users.UserPrincipal;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class ActionExecutorImpl implements ActionExecutor {

    private final AdminUserValidator validator;

    public ActionExecutorImpl(AdminUserValidator validator) {
        this.validator = validator;
    }

    @Override
    public <T> T executeAs(UserPrincipal principal, Action<T> action) throws UserHasNoAccessException, ActionExecutionException {
        Objects.requireNonNull(principal);
        Objects.requireNonNull(action);
        if (haveAccessForAction(principal, action)) {
            return action.execute();
        }
        throw new UserHasNoAccessException(principal);
    }

    private List<GrantedAuthority> getAllUserAuthorities(UserPrincipal principal) {
        int totalSize = 0;

        Collection<? extends GrantedAuthority> authorities = principal.getAuthorities();
        Collection<? extends AttributeGrantedAuthority> attributes = principal.getAttributes();

        boolean isAuthoritiesNull = authorities == null;
        boolean isAttributesNull = attributes == null;

        if (!isAuthoritiesNull) totalSize += authorities.size();
        if (!isAttributesNull) totalSize += attributes.size();

        List<GrantedAuthority> allAuthorities = new ArrayList<>(totalSize);

        if (!isAuthoritiesNull) allAuthorities.addAll(authorities);
        if (!isAttributesNull) allAuthorities.addAll(attributes);
        return allAuthorities;
    }

    private <T> boolean haveAccessForAction(UserPrincipal principal, Action<T> action) {
        if (!validator.isAdmin(principal)) {
            String authority = action.getAuthority().getAuthority();
            return getAllUserAuthorities(principal)
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .anyMatch(authorityName -> authorityName.equals(authority));
        }
        return true;
    }
}
