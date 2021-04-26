package com.auth.framework.core.action;

import com.auth.framework.core.jwt.repository.TokenStorage;
import org.springframework.security.core.GrantedAuthority;

public class TokenWithdraw implements Action {

    private final TokenStorage tokenStorage;
    private final String username;
    private final GrantedAuthority adminAuthority;

    public TokenWithdraw(TokenStorage tokenStorage, String withdrawFrom, GrantedAuthority adminAuthority) {
        this.tokenStorage = tokenStorage;
        this.username = withdrawFrom;
        this.adminAuthority = adminAuthority;
    }


    @Override
    public Object execute() {
        tokenStorage.deleteTokenByOwner(username);
        return null;
    }

    @Override
    public GrantedAuthority getAuthority() {
        return adminAuthority;
    }
}
