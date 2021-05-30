package com.diplom.impl.controller.test;


import com.auth.framework.core.tokens.jwt.password.PasswordToken;
import com.auth.framework.core.tokens.jwt.password.manager.PasswordTokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/password-reset-token")
public class PasswordResetTokenController {

    @Autowired
    private PasswordTokenManager manager;

    @PostMapping(value = "/generate")
    public PasswordToken generate(@RequestParam String username) {
        return manager.createPasswordTokenForUsername(username);
    }

    @PostMapping(value = "/validate")
    public boolean validate(@RequestParam String username,
                            @RequestParam String tokenValue) {
        return manager.isTokenValid(username, tokenValue);
    }

    @PostMapping(value = "/delete")
    public void deleteToken(@RequestParam String username) {
        manager.deleteToken(username);
    }
}
