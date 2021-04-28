package com.diplom.impl.controller;


import com.auth.framework.core.action.Action;
import com.auth.framework.core.action.executor.ActionExecutor;
import com.auth.framework.core.exceptions.ActionExecutionException;
import com.auth.framework.core.exceptions.UserHasNoAccessException;
import com.auth.framework.core.users.UserPrincipal;
import com.diplom.impl.factory.actions.ActionFactory;
import com.diplom.impl.requestBody.UsernameRequestBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class TokenWithdrawControllerExample {

    @Autowired
    private ActionExecutor actionExecutor;

    @Autowired
    private ActionFactory actionFactory;


    public String withdraw(UsernameRequestBody requestBody) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal principal;
        try {
            principal = (UserPrincipal) authentication.getPrincipal();
        } catch (ClassCastException e) {
            log.info("authentication {}", authentication.getPrincipal());
            return (String) authentication.getPrincipal();
        }

        log.info("Username {} has following authorities {} and attributes {}",
                principal.getUsername(),
                principal.getAuthorities(),
                principal.getAttributes());

        try {
            Action<Object> withdrawAction = actionFactory.getWithdrawAction(requestBody.getUsername());
            actionExecutor.executeAs(principal, withdrawAction);
        } catch (UserHasNoAccessException | ActionExecutionException e) {
            return e.getMessage();
        }
        return "Token was removed from user " + requestBody.getUsername();
    }


    @PostMapping(value = "/withdraw")
    public String tryWithdrawAny(@RequestBody UsernameRequestBody requestBody) {
        return withdraw(requestBody);
    }
}
