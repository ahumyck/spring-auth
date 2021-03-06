package com.auth.framework.core.tokens.jwt.transport;

import com.auth.framework.core.tokens.jwt.JsonWebToken;
import com.auth.framework.core.utils.ValidationCenter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

public class CookieTransport implements TokenTransport {

    private final String fieldName;

    public CookieTransport(String fieldName) {
        this.fieldName = fieldName;
    }

    private Cookie createCookie(JsonWebToken jsonWebToken) {
        return createCookie(jsonWebToken.getRawToken(), jsonWebToken.getTimeToLive());
    }

    private Cookie createCookie(String token, int duration) {
        Cookie cookie = new Cookie(fieldName, token);
        cookie.setMaxAge(duration);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        return cookie;
    }

    @Override
    public void addToken(HttpServletResponse response, JsonWebToken jsonWebToken) {
        response.addCookie(createCookie(jsonWebToken));
    }

    @Override
    public Optional<String> extractRawToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (fieldName.equals(cookie.getName())) {
                    String token = cookie.getValue();
                    if (ValidationCenter.isValidString(token)) return Optional.of(token);
                }
            }
        }
        return Optional.empty();
    }
}
