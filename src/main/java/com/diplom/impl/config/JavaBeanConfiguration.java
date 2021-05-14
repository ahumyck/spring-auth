package com.diplom.impl.config;


import com.auth.framework.core.tokens.jwt.transport.TokenTransport;
import com.diplom.impl.transport.BearerTokenTransport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class JavaBeanConfiguration {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public TokenTransport bearerTokenTransport() {
        return new BearerTokenTransport();
    }


}
