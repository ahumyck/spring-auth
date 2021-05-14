package com.diplom.impl.transport;

import com.auth.framework.core.tokens.jwt.JsonWebToken;
import com.auth.framework.core.tokens.jwt.transport.TokenTransport;
import com.auth.framework.core.utils.ValidationCenter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

public class BearerTokenTransport implements TokenTransport {

    public static final String AUTHORIZATION = "Authorization";

    @Override
    public void addToken(HttpServletResponse response, JsonWebToken jsonWebToken) {
        response.addHeader(AUTHORIZATION, jsonWebToken.getRawToken());
    }

    @Override
    public Optional<String> extractRawToken(HttpServletRequest request) {
        String bearer = request.getHeader(AUTHORIZATION);
        if (ValidationCenter.isValidString(bearer) && bearer.startsWith("Bearer ")) {
            return Optional.of(bearer.substring(7));
        }
        return Optional.empty();
    }
}
