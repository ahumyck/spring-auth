package com.diplom.impl.controller;

import com.auth.framework.core.jwt.Token;
import com.auth.framework.core.jwt.manager.TokenManager;
import com.auth.framework.core.jwt.repository.TokenStorage;
import com.diplom.impl.repository.AttributeRepository;
import com.diplom.impl.repository.RoleRepository;
import com.diplom.impl.repository.UserRepository;
import com.diplom.impl.requestBody.UsernamePasswordRequestBody;
import com.diplom.impl.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
public class AuthenticationController {

    @Autowired
    private AttributeRepository attributeRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private TokenStorage tokenStorage;

    @Autowired
    private UserService userService;

    @Autowired
    private TokenManager manager;

    @PostMapping(value = "/register")
    public String singIn(@RequestBody UsernamePasswordRequestBody body) {
        String username = body.getUsername();
        String password = body.getPassword();
        userService.createUser(username, password);
        return "User " + username + " was registered";
    }

    @PostMapping(value = "/login")
    public String postMe(HttpServletResponse response, @RequestBody UsernamePasswordRequestBody body) {
        String username = body.getUsername();
        String password = body.getPassword();
        if (userService.checkUser(username, password)) {
            manager.createTokenForUsername(response, username);
        }
        return "check cookie";
    }

    @PostMapping(value = "/db")
    public void db() {
        log.info(String.valueOf(attributeRepository.findAll()));
        log.info(String.valueOf(userRepository.findAll()));
        log.info(String.valueOf(roleRepository.findAll()));
        List<String> usersWithToken = tokenStorage.findAll()
                .stream()
                .map(Token::getOwner)
                .collect(Collectors.toList());
        log.info(String.valueOf(usersWithToken));
    }
}
