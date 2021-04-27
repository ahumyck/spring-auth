package com.diplom.impl.factory.actions;

import com.auth.framework.core.action.Action;
import org.springframework.security.core.GrantedAuthority;

public class ImpersonalizeAction implements Action {

    private final GrantedAuthority adminAuthority;

    public ImpersonalizeAction(GrantedAuthority adminAuthority) {
        this.adminAuthority = adminAuthority;
    }

    @Override
    public Object execute() {
        return null;
    }

    @Override
    public GrantedAuthority getAuthority() {
        return adminAuthority;
    }
}
