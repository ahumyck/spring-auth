package com.auth.framework.core.exceptions.access;

import com.auth.framework.core.action.Action;
import com.auth.framework.core.users.UserPrincipal;

public class UserHasNoAccessException extends Exception {
    private static final long serialVersionUID = 894685956356028018L;

    public UserHasNoAccessException() {
        super();
    }

    public UserHasNoAccessException(UserPrincipal principal) {
        super("User " + principal.getUsername() + " has no access to execute action");
    }

    public UserHasNoAccessException(String message) {
        super(message);
    }
}
