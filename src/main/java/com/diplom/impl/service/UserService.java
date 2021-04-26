package com.diplom.impl.service;

import com.diplom.impl.model.entity.User;
import com.diplom.impl.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.diplom.impl.ImplApplication.ADMIN_ROLE_NAME;
import static com.diplom.impl.ImplApplication.USER_ROLE_NAME;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleService roleService;

    public User createUser(String username, String password) {
        return userRepository.save(new User(username, password, roleService.findRole(USER_ROLE_NAME)));
    }

    public User createAdmin(String username, String password) {
        return userRepository.save(new User(username, password, roleService.findRole(ADMIN_ROLE_NAME)));
    }

    public boolean checkUser(String username, String password) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return user.getPassword().equals(password);
        }
        return false;
    }

}
