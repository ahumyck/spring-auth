package com.diplom.impl.controller.test;

import com.auth.framework.core.tokens.jwt.managers.TokenManager;
import com.diplom.impl.service.UserService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;

import static com.diplom.impl.utils.AuthenticationConstants.USER_AGENT_HEADER_NAME;

@AllArgsConstructor
@RestController
public class CreateSessionController {

    @Autowired
    private final TokenManager manager;
    @Autowired
    private final UserService userService;

    @SneakyThrows
    @GetMapping(value = "/create/session")
    public String createSession(HttpServletResponse response,
                                HttpServletRequest request,
                                @RequestParam String username,
                                @RequestParam String password) {

        userService.isExistingAndNotLocked(username, password);
        manager.createTokenForUsername(response, username,
                Collections.singletonMap(USER_AGENT_HEADER_NAME,
                        request.getHeader(USER_AGENT_HEADER_NAME)));
        return "Сессия для пользователя " + username + " с заданнами параметрами была " +
                "успешно создана";
    }
}
