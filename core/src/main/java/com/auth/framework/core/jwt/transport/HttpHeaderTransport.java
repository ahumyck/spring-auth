package com.auth.framework.core.jwt.transport;

import com.auth.framework.core.jwt.Token;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

public class HttpHeaderTransport implements TokenTransport {

    private final static String PRINCIPAL_TOKEN = "principal_token";

    @Override
    public void addToken(HttpServletResponse response, Token token) {

    }

    @Override
    public Optional<String> extractRawToken(HttpServletRequest request) {
        return Optional.empty();
    }
}
