package com.auth.framework.core.tokens.jwt.transport;

import com.auth.framework.core.tokens.jwt.JsonWebToken;
import com.auth.framework.core.utils.ValidationCenter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

public class HttpHeaderTransport implements TokenTransport {

    private final String fieldName;

    public HttpHeaderTransport(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public void addToken(HttpServletResponse response, JsonWebToken jsonWebToken) {
        response.addHeader(fieldName, jsonWebToken.getRawToken());
    }

    @Override
    public Optional<String> extractRawToken(HttpServletRequest request) {
        String header = request.getHeader(fieldName);
        if (ValidationCenter.isValidString(header)) {
            return Optional.of(header);
        }
        return Optional.empty();
    }
}
