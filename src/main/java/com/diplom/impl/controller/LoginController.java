package com.diplom.impl.controller;

import com.auth.framework.core.constants.AuthenticationConstants;
import com.auth.framework.core.tokens.jwt.managers.TokenManager;
import com.diplom.impl.requestBody.RegistrationDataRequestBody;
import com.diplom.impl.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;

@RestController
@Slf4j
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private TokenManager manager;

    @PostMapping(value = "/sign-in")
    public String requestBodyLogin(HttpServletRequest request,
                                   HttpServletResponse response,
                                   @RequestBody RegistrationDataRequestBody body) {
        String username = body.getUsername();
        String password = body.getPassword();
        return commonLogin(request, response, username, password);
    }


    @GetMapping(value = "/sign-in")
    public String requestParamLogin(HttpServletRequest request,
                                    HttpServletResponse response,
                                    @RequestParam String username,
                                    @RequestParam String password) {
        return commonLogin(request, response, username, password);
    }

    private String commonLogin(HttpServletRequest request,
                               HttpServletResponse response,
                               String username,
                               String password) {
        try {
            userService.isExistingAndNotLocked(username, password);
            manager.createTokenForUsername(response, username,
                    Collections.singletonMap(AuthenticationConstants.USER_AGENT_HEADER_NAME,
                            request.getHeader(AuthenticationConstants.USER_AGENT_HEADER_NAME)));
        } catch (Exception e) {
            log.warn("Unable to login", e);
            return "Unable to login: " + e.getMessage();
        }
        return "check cookie";
    }
}
