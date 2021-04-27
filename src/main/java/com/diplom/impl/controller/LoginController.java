package com.diplom.impl.controller;

import com.auth.framework.core.tokens.jwt.manager.TokenManager;
import com.diplom.impl.requestBody.RegistrationDataRequestBody;
import com.diplom.impl.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@Slf4j
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private TokenManager manager;

    @PostMapping(value = "/login")
    public String postMe(HttpServletRequest request, HttpServletResponse response, @RequestBody RegistrationDataRequestBody body) {
        String username = body.getUsername();
        String password = body.getPassword();
        try {
            userService.isExistingAndNotLocked(username, password);
            manager.createTokenForUsername(request, response, username);
        } catch (Exception e) {
            log.warn("Unable to login", e);
            return "Unable to login: " + e.getMessage();
        }
        return "check cookie";
    }


    @GetMapping(value = "/login")
    public String postMe(HttpServletRequest request, HttpServletResponse response,
                         @RequestParam String username,
                         @RequestParam String password) {
        try {
            userService.isExistingAndNotLocked(username, password);
            manager.createTokenForUsername(request, response, username);
        } catch (Exception e) {
            log.warn("Unable to login", e);
            return "Unable to login: " + e.getMessage();
        }
        return "check cookie";
    }
}
