package com.auth.framework.core.jwt.transport;

import com.auth.framework.core.jwt.Token;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

public interface TokenTransport {

    void addToken(HttpServletResponse response, Token token);

    Optional<String> extractRawToken(HttpServletRequest request);
}
