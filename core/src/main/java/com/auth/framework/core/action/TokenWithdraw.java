package com.auth.framework.core.action;

import com.auth.framework.core.tokens.jwt.repository.TokenRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class TokenWithdraw implements Action {

    private final TokenRepository tokenRepository;
    private final String username;
    private final GrantedAuthority adminAuthority;

    public TokenWithdraw(TokenRepository tokenRepository, String withdrawFrom, GrantedAuthority adminAuthority) {
        this.tokenRepository = tokenRepository;
        this.username = withdrawFrom;
        this.adminAuthority = adminAuthority;
    }

    public TokenWithdraw(TokenRepository tokenRepository, String withdrawFrom, String adminRoleName) {
        this.tokenRepository = tokenRepository;
        this.username = withdrawFrom;
        this.adminAuthority = new SimpleGrantedAuthority(adminRoleName);
    }


    @Override
    public Object execute() {
        tokenRepository.deleteAllUserTokens(username);
        return null;
    }

    @Override
    public GrantedAuthority getAuthority() {
        return adminAuthority;
    }
}
