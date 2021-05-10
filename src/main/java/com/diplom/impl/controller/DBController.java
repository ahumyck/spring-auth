package com.diplom.impl.controller;

import com.auth.framework.core.tokens.jwt.JsonWebToken;
import com.auth.framework.core.tokens.jwt.repository.TokenRepository;
import com.auth.framework.registration.token.password.PasswordToken;
import com.auth.framework.registration.token.password.repository.PasswordTokenRepository;
import com.diplom.impl.repository.RoleRepository;
import com.diplom.impl.repository.UserRepository;
import com.diplom.impl.utils.AuthenticationConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;

@RestController
@Slf4j
public class DBController {


    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private PasswordTokenRepository passwordTokenRepository;


    @PostMapping(value = "/db")
    public void db() {
        log.info("users {}", userRepository.findAll());
        log.info("roles {}", roleRepository.findAll());
        log.info("jwt {}",
                tokenRepository.findAll()
                        .stream()
                        .map(JsonWebToken::getOwner)
                        .collect(Collectors.toList()));
        log.info("password tokens {}",
                passwordTokenRepository.findAll()
                        .stream()
                        .map(PasswordToken::getOwner)
                        .collect(Collectors.toList()));
    }

    @GetMapping(value = "/agent")
    public String userAgent(HttpServletRequest request) {
        String header = request.getHeader(AuthenticationConstants.USER_AGENT_HEADER_NAME);
        log.info("user agent => {}", header);
        return header;
    }
}
