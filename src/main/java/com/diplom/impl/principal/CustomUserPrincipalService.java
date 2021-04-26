package com.diplom.impl.principal;

import com.auth.framework.core.users.UserPrincipal;
import com.auth.framework.core.users.UserPrincipalService;
import com.diplom.impl.model.entity.User;
import com.diplom.impl.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class CustomUserPrincipalService implements UserPrincipalService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserPrincipal loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> byUsername = userRepository.findByUsername(username);
        if (byUsername.isPresent()) {
            return new CustomUserPrincipal(byUsername.get());
        }
        log.error("{} was not found", username);
        throw new UsernameNotFoundException(username);
    }
}
