package com.diplom.impl;

import com.diplom.impl.repository.UserRepository;
import com.diplom.impl.service.RoleService;
import com.diplom.impl.service.UserService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.annotation.PostConstruct;

@SpringBootApplication
@EnableJpaRepositories
@Slf4j
public class ImplApplication {

    @Autowired
    private RoleService roleService;

    public static final String USER_ROLE_NAME = "USER";
    public static final String ADMIN_ROLE_NAME = "ADMIN";

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @SneakyThrows
    public static void main(String[] args) {
        SpringApplication.run(ImplApplication.class, args);
    }


    @PostConstruct
    public void init() {
        roleService.createRole(USER_ROLE_NAME);
        roleService.createRole(ADMIN_ROLE_NAME);

        userService.createUnlockedUser("user@email.com", "user", "user");
        userService.createUnlockedAdmin("admin@admin.com", "admin", "admin");
    }
}
