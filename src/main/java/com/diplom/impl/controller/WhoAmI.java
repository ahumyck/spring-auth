package com.diplom.impl.controller;


import com.auth.framework.core.users.UserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
public class WhoAmI {


    @RequestMapping(value = "/whoami", method = {RequestMethod.GET, RequestMethod.POST})
    public UserPrincipal whoAmI() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (UserPrincipal) authentication.getPrincipal();
    }
}
