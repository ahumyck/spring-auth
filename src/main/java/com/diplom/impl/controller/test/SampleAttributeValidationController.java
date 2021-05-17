package com.diplom.impl.controller.test;

import com.auth.framework.core.users.UserPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping(value = "/attribute")
public class SampleAttributeValidationController {

    @PostMapping(value = "/date")
    public Optional<UserPrincipal> haveAccess(@AuthenticationPrincipal UserPrincipal principal) {
        return Optional.ofNullable(principal);
    }
}
