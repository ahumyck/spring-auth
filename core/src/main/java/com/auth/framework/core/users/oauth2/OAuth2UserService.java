package com.auth.framework.core.users.oauth2;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;

@Slf4j
public class OAuth2UserService extends DefaultOAuth2UserService {

    @Override
    public OAuth2UserPrincipal loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("loadOAuthUserPrincipal logic invoked");
        try {
            DefaultOAuth2UserPrincipal principal = new DefaultOAuth2UserPrincipal(super.loadUser(userRequest));
            log.info("loaded user principal => {}", principal);
            return principal;

        } catch (OAuth2AuthenticationException e) {
            log.error("error loading oauth user principal", e);
            throw e;
        }
    }

}
