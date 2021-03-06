package com.auth.framework.oauth.oauth2;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;

@Slf4j
public class OAuth2UserService extends DefaultOAuth2UserService {

    @Override
    public OAuth2UserPrincipal loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        try {
            return new DefaultOAuth2UserPrincipal(super.loadUser(userRequest));
        } catch (OAuth2AuthenticationException e) {
            log.error("error loading oauth user principal", e);
            throw e;
        }
    }

}
