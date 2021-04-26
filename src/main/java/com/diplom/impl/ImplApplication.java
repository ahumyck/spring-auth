package com.diplom.impl;

import com.diplom.impl.service.AttributeService;
import com.diplom.impl.service.RoleService;
import com.diplom.impl.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.annotation.PostConstruct;

@SpringBootApplication
@EnableJpaRepositories
public class ImplApplication {

    @Autowired
    private RoleService roleService;

    public static final String USER_ROLE_NAME = "USER";
    public static final String ADMIN_ROLE_NAME = "ADMIN";

    @Autowired
    private AttributeService attributeService;

    public static final String PROJECT1_ATTRIBUTE_NAME = "project_1";
    public static final String PROJECT2_ATTRIBUTE_NAME = "project_2";


    @Autowired
    private UserService userService;

    public static void main(String[] args) {
        SpringApplication.run(ImplApplication.class, args);
    }


    @PostConstruct
    public void init() {
        roleService.createRole(USER_ROLE_NAME);
        roleService.createRole(ADMIN_ROLE_NAME);

        attributeService.createAttribute(PROJECT1_ATTRIBUTE_NAME);
        attributeService.createAttribute(PROJECT2_ATTRIBUTE_NAME);

        userService.createUser("user", "user");
        userService.createAdmin("admin", "admin");
    }
}
