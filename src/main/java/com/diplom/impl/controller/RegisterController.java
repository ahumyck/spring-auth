package com.diplom.impl.controller;

import com.auth.framework.core.tokens.jwt.password.manager.PasswordTokenManager;
import com.diplom.impl.exceptions.UserCreationException;
import com.diplom.impl.factory.email.ContentGenerator;
import com.diplom.impl.factory.email.EmailSender;
import com.diplom.impl.requestBody.RegistrationDataRequestBody;
import com.diplom.impl.requestBody.UsernamePasswordRequestBody;
import com.diplom.impl.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class RegisterController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailSender emailSender;

    @Autowired
    private ContentGenerator contentGenerator;

    @Autowired
    private PasswordTokenManager passwordTokenManager;

    @PostMapping(value = "/register")
    public String firstPhaseRegistration(@RequestBody RegistrationDataRequestBody body) {
        try {
            userService.createUser(body);
            return emailSender.sendMessage(body.getEmail(),
                    contentGenerator.generateMessageForUser(body.getUsername()));
        } catch (UserCreationException e) {
            return "Unable to create user: " + e.getMessage();
        }
    }

    @GetMapping(value = "/password")
    public String secondPhaseRegistration(@RequestParam String token, @RequestBody UsernamePasswordRequestBody body) {
        String username = body.getUsername();
        log.info("secondPhaseRegistration for username: '{}'", username);
        if (passwordTokenManager.isTokenValid(username, token)) {
            try {
                userService.checkAndUnlockAccount(username, body.getPassword());
                passwordTokenManager.deleteToken(username);
                return "Registration for username " + username + " completed";
            } catch (Exception e) {
                log.info("Unable to unlock account", e);
                return e.getMessage();
            }
        }
        return "Invalid token";
    }
}
