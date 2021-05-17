package com.diplom.impl.controller.test;

import com.auth.framework.core.exceptions.TokenGenerationException;
import com.auth.framework.core.tokens.jwt.JsonWebToken;
import com.auth.framework.core.tokens.jwt.managers.TokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@RestController
public class AuthenticationController {

    private final TokenManager tokenManager;

    @Autowired
    public AuthenticationController(TokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

    @GetMapping("/test/token")
    public void generateToken(HttpServletResponse response,
                              @RequestParam String username) throws TokenGenerationException {
        tokenManager.createTokenForUsername(response, username);
    }


    @GetMapping("/test/whoami")
    public Optional<JsonWebToken> validateUsername(HttpServletRequest request) {
        return tokenManager.validateAndGetToken(request);
    }
}
