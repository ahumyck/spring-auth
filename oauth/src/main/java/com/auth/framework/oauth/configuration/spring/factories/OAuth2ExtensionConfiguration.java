package com.auth.framework.oauth.configuration.spring.factories;

import com.auth.framework.oauth.oauth2.OAuth2UserService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;


@EnableConfigurationProperties
class OAuth2ExtensionConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public OAuth2UserService oAuth2UserService() {
        return new OAuth2UserService();
    }

}
