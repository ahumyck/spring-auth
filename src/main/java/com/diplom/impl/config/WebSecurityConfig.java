package com.diplom.impl.config;

import com.auth.framework.core.adapter.WebSecurityConfigurableAdapter;
import com.auth.framework.core.attribute.AttributeConfigurer;
import com.auth.framework.core.tokens.jwt.filter.TokenFilter;
import com.diplom.impl.oauth2.OAuth2OnFailureHandler;
import com.diplom.impl.oauth2.OAuth2OnSuccessHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.time.LocalDate;

import static com.diplom.impl.ImplApplication.ADMIN_ROLE_NAME;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true)
@Slf4j
public class WebSecurityConfig extends WebSecurityConfigurableAdapter {

    @Autowired
    private TokenFilter tokenFilter;

    @Autowired
    private DefaultOAuth2UserService oAuth2UserService;

    @Autowired
    private OAuth2OnFailureHandler failureHandler;

    @Autowired
    public OAuth2OnSuccessHandler successHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic()
                .and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/admin/*").hasAnyAuthority(ADMIN_ROLE_NAME)
                .antMatchers("/", "/login", "/oauth/**").permitAll()
                .mvcMatchers("/**").permitAll()
                .and()
                .oauth2Login()
                .userInfoEndpoint()
                .userService(oAuth2UserService)
                .and()
                .successHandler(successHandler)
                .failureHandler(failureHandler)
                .permitAll()
                .and()
                .logout().permitAll()
                .and()
                .addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    protected void configure(AttributeConfigurer configurer) {
        configurer
                .predicatesMatchAll("/attribute/date",
                        user -> {
                            LocalDate date = (LocalDate) user.getParameter("date");
                            LocalDate secondDate = LocalDate.now();
                            LocalDate firstDate = LocalDate.of(2020, 8, 15);
                            return (date.isAfter(firstDate) || date.isEqual(firstDate)) &&
                                    (date.isBefore(secondDate) || date.isEqual(secondDate));
                        });
    }
}