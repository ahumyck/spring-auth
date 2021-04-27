package com.diplom.impl.factory;

import com.auth.framework.core.action.Action;
import com.auth.framework.core.action.TokenWithdraw;
import com.auth.framework.core.tokens.jwt.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import static com.diplom.impl.ImplApplication.ADMIN_ROLE_NAME;

@Component
public class ActionFactory {

    @Autowired
    private TokenRepository tokenRepository;

    public Action getWithdrawAction(String withdrawFrom) {
        return new TokenWithdraw(tokenRepository, withdrawFrom, new SimpleGrantedAuthority(ADMIN_ROLE_NAME));
    }
}
