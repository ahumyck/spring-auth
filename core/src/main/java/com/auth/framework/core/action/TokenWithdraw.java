package com.auth.framework.core.action;

import com.auth.framework.core.tokens.jwt.repository.TokenRepository;
import org.springframework.security.core.GrantedAuthority;

public class TokenWithdraw implements Action {

    private final TokenRepository tokenRepository;
    private final String username;
    private final GrantedAuthority adminAuthority;

    public TokenWithdraw(TokenRepository tokenRepository, String withdrawFrom, GrantedAuthority adminAuthority) {
        this.tokenRepository = tokenRepository;
        this.username = withdrawFrom;
        this.adminAuthority = adminAuthority;
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
