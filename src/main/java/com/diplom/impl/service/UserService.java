package com.diplom.impl.service;

import com.diplom.impl.model.entity.Role;
import com.diplom.impl.model.entity.User;
import com.diplom.impl.repository.UserRepository;
import com.diplom.impl.requestBody.RegistrationDataRequestBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.diplom.impl.ImplApplication.ADMIN_ROLE_NAME;
import static com.diplom.impl.ImplApplication.USER_ROLE_NAME;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleService roleService;

    public User createUser(RegistrationDataRequestBody body) throws Exception {
        String email = body.getEmail();
        String username = body.getUsername();
        String password = body.getPassword();
        return createUser(email, username, password);
    }

    public User createUser(String email, String username, String password) throws Exception {
        return createUserCommon(email, username, password, roleService.findRole(USER_ROLE_NAME), true);
    }

    public User createUnlockedAdmin(String email, String username, String password) throws Exception {
        return createUserCommon(email, username, password, roleService.findRole(ADMIN_ROLE_NAME), false);
    }

    public User createUnlockedUser(String email, String username, String password) throws Exception {
        return createUserCommon(email, username, password, roleService.findRole(USER_ROLE_NAME), false);
    }

    private User createUserCommon(String email, String username, String password, Role role, boolean isLocked) throws Exception {
        isUserExists(email, username);
        return userRepository.save(new User(email, username, password, role, isLocked));
    }

    private void isUserExists(String email, String username) throws Exception {
        if (userRepository.findByEmail(email) != null)
            throw new Exception("Username with such email already exists");
        if (userRepository.findByUsername(username) != null)
            throw new Exception("Username with such email already exists");
    }

    public boolean checkPassword(User user, String password) {
        return user.getPassword().equals(password);
    }

    public void checkAndUnlockAccount(String username, String password) throws Exception {
        User user = userRepository.findByUsername(username);
        log.info("user checkUser => {}", user);
        if (user != null) {
            if (checkPassword(user, password)) {
                user.setLocked(false);
                userRepository.save(user);
                return;
            }
            throw new Exception("Incorrect login or password");
        }
        throw new Exception("No user found with name " + username);
    }

    public void isExistingAndNotLocked(String username, String password) throws Exception {
        User user = userRepository.findByUsername(username);
        log.info("user isExistingAndLocked => {}", user);
        if (user != null) {
            if (checkPassword(user, password)) {
                if (!user.isLocked()) return;
                throw new Exception("Account is locked");
            }
            throw new Exception("Incorrect login or password");
        }
        throw new Exception("No user found with name " + username);
    }


}
