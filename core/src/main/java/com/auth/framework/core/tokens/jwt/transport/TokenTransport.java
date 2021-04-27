package com.auth.framework.core.tokens.jwt.transport;

import com.auth.framework.core.tokens.jwt.JsonWebToken;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

public interface TokenTransport {

    void addToken(HttpServletResponse response, JsonWebToken jsonWebToken);

    Optional<String> extractRawToken(HttpServletRequest request);
}
