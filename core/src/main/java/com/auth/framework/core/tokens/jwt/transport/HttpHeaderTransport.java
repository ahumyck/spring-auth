package com.auth.framework.core.tokens.jwt.transport;

import com.auth.framework.core.tokens.jwt.JsonWebToken;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

public class HttpHeaderTransport implements TokenTransport {

    private final static String PRINCIPAL_TOKEN = "principal_token";

    @Override
    public void addToken(HttpServletResponse response, JsonWebToken jsonWebToken) {

    }

    @Override
    public Optional<String> extractRawToken(HttpServletRequest request) {
        return Optional.empty();
    }
}
