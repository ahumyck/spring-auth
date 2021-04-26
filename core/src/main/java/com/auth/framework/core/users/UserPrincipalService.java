package com.auth.framework.core.users;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Расширение интерфейса
 * При использовании фреймворка мы должны наследоваться от этого класса, а не от UserDetails
 * @see org.springframework.security.core.userdetails.UserDetailsService UserDetailsService
 */
public interface UserPrincipalService extends UserDetailsService {

    @Override
    UserPrincipal loadUserByUsername(String s) throws UsernameNotFoundException;
}
