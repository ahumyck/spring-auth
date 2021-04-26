package com.auth.framework.core.action;

import org.springframework.security.core.GrantedAuthority;

public class ActionStub implements Action {

    private final GrantedAuthority authority;
    private final String description;

    public ActionStub(GrantedAuthority authority, String description) {
        this.authority = authority;
        this.description = description;
    }

    @Override
    public Object execute() {
        return new Object();
    }

    @Override
    public GrantedAuthority getAuthority() {
        return authority;
    }
}