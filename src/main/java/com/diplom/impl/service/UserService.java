package com.diplom.impl.service;

import com.diplom.impl.exceptions.UserCreationException;
import com.diplom.impl.model.entity.Role;
import com.diplom.impl.model.entity.User;
import com.diplom.impl.repository.UserRepository;
import com.diplom.impl.requestBody.RegistrationDataRequestBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.diplom.impl.ImplApplication.ADMIN_ROLE_NAME;
import static com.diplom.impl.ImplApplication.USER_ROLE_NAME;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleService roleService;

    public User createUser(RegistrationDataRequestBody body) {
        String email = body.getEmail();
        String username = body.getUsername();
        String password = body.getPassword();
        return createUser(email, username, password);
    }

    public User createUser(String email, String username, String password) {
        return createUserCommon(email, username, password, true, roleService.findRole(USER_ROLE_NAME));
    }

    public User createUnlockedAdmin(String email, String username, String password) {
        return createUserCommon(email, username, password, false, roleService.findRole(ADMIN_ROLE_NAME));
    }

    public User createUnlockedUser(String email, String username, String password) {
        return createUserCommon(email, username, password, false, roleService.findRole(USER_ROLE_NAME));
    }

    public User createUserCommon(String email, String username, String password, boolean isLocked, Role... roles) {
        Set<Role> filteredRoles = filterAuthorities(roles);
        isUserExists(email, username);
        return userRepository.save(new User(email, username, password, isLocked, filteredRoles));
    }

    public User createUserCommon(String email,
                                 String username,
                                 String password,
                                 boolean isLocked,
                                 Collection<? extends GrantedAuthority> authorities) {
        Set<Role> filteredRoles = filterAuthorities(authorities);
        isUserExists(email, username);
        return userRepository.save(new User(email, username, password, isLocked, filteredRoles));
    }

    private Set<Role> filterAuthorities(Role[] roles) {
        return Arrays.stream(roles)
                .filter(role -> !role.getRoleName().startsWith("SCOPE_"))
                .collect(Collectors.toSet());
    }

    private Set<Role> filterAuthorities(Collection<? extends GrantedAuthority> authorities) {
        return authorities
                .stream()
                .filter(authority -> !authority.getAuthority().startsWith("SCOPE_"))
                .map(authority -> roleService.findRole(authority.getAuthority()))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

    }

    private void isUserExists(String email, String username) throws UserCreationException {
        if (userRepository.findByEmail(email) != null)
            throw new UserCreationException("Username with such email already exists");
        if (userRepository.findByUsername(username) != null)
            throw new UserCreationException("Username with such email already exists");
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
