package com.auth.framework.core.action.executor;

import com.auth.framework.core.access.admin.AdminUserValidator;
import com.auth.framework.core.action.Action;
import com.auth.framework.core.exceptions.ActionExecutionException;
import com.auth.framework.core.exceptions.UserHasNoAccessException;
import com.auth.framework.core.users.UserPrincipal;
import org.springframework.security.core.GrantedAuthority;

import java.util.*;

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

    private List<GrantedAuthority> getAuthorities(UserPrincipal principal) {
        Collection<? extends GrantedAuthority> authorities = principal.getAuthorities();
        List<GrantedAuthority> allAuthorities;
        if (authorities != null) {
            allAuthorities = new ArrayList<>(authorities.size());
            allAuthorities.addAll(authorities);
        } else {
            allAuthorities = Collections.emptyList();
        }
        return allAuthorities;
    }

    private <T> boolean haveAccessForAction(UserPrincipal principal, Action<T> action) {
        if (!validator.isAdmin(principal)) {
            String authority = action.getAuthority().getAuthority();
            return getAuthorities(principal)
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .anyMatch(authorityName -> authorityName.equals(authority));
        }
        return true;
    }
}
