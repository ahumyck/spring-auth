package com.diplom.impl.oauth2;

import com.auth.framework.core.encryption.generator.RandomPasswordGenerator;
import com.auth.framework.core.users.oauth2.DefaultOAuth2UserPrincipal;
import com.diplom.impl.exceptions.UserCreationException;
import com.diplom.impl.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
//http://localhost:8080/oauth2/authorization/google
public class OAuth2OnSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private UserService userService;

    @Autowired
    private RandomPasswordGenerator passwordGenerator;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        FilterChain chain,
                                        Authentication authentication) throws IOException, ServletException {
        log.info("onAuthenticationSuccess logic called");
        try {
            DefaultOAuth2UserPrincipal principal = (DefaultOAuth2UserPrincipal) authentication.getPrincipal();
            String name = principal.getName();
            String email = principal.getEmail();
            String password = passwordGenerator.generatePasswordThenEncodeAsBase64();
            userService.createUser(email, name, password);
        } catch (ClassCastException e) {
            log.error("Unable to cast authentication to DefaultOAuth2UserPrincipal", e);
        } catch (UserCreationException e) {
            log.error("Unable to create user", e);
        }
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
