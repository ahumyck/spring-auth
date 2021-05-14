package com.diplom.impl.controller;


import com.auth.framework.core.users.UserPrincipal;
import com.diplom.impl.requestBody.UsernameRequestBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class TokenWithdrawControllerExample {

    public String withdraw(UsernameRequestBody requestBody) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal principal;
        try {
            principal = (UserPrincipal) authentication.getPrincipal();
            //todo: withdraw
        } catch (ClassCastException e) {
            log.info("authentication {}", authentication.getPrincipal());
            return (String) authentication.getPrincipal();
        }

        log.info("Username {} has following authorities {} and parameters {}",
                principal.getUsername(),
                principal.getAuthorities(),
                principal.getParameters());

        return "Token was removed from user " + requestBody.getUsername();
    }


    @PostMapping(value = "/withdraw")
    public String tryWithdrawAny(@RequestBody UsernameRequestBody requestBody) {
        return withdraw(requestBody);
    }
}
