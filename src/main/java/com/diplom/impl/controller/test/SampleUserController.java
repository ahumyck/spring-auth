package com.diplom.impl.controller.test;

import com.diplom.impl.model.entity.User;
import com.diplom.impl.repository.UserRepository;
import com.diplom.impl.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping(value = "/sample")
public class SampleUserController {

    private final UserService userService;
    private final UserRepository userRepository;

    @Autowired
    public SampleUserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @PostMapping(value = "/create")
    public User createSample(@RequestParam String email,
                             @RequestParam String username,
                             @RequestParam String password,
                             @RequestParam Integer type) {
        User user = userService.createUnlockedUser(email, username, password);
        if (type == 0) {
            user.setDate(LocalDate.of(2005, 1, 1));
            userRepository.save(user);
        }
        return user;

    }
}
