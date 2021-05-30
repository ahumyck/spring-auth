package com.auth.framework.twofactor.configuration.spring.factories;

import com.auth.framework.twofactor.manager.AuthenticationCodeManager;
import com.auth.framework.twofactor.manager.DefaultAuthenticationCodeManager;
import com.auth.framework.twofactor.provider.AuthenticationCodeProvider;
import com.auth.framework.twofactor.provider.DefaultAuthenticationCodeProvider;
import com.auth.framework.twofactor.repository.AuthenticationCodeRepository;
import com.auth.framework.twofactor.repository.DefaultAuthenticationCodeRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;


@EnableConfigurationProperties
class TwoFactorAuthConfiguration {


    @Bean
    @ConditionalOnMissingBean
    public AuthenticationCodeProvider codeProvider() {
        return new DefaultAuthenticationCodeProvider();
    }

    @Bean
    @ConditionalOnMissingBean
    public AuthenticationCodeRepository codeRepository() {
        return new DefaultAuthenticationCodeRepository();
    }

    @Bean
    @ConditionalOnMissingBean
    public AuthenticationCodeManager codeManager(AuthenticationCodeProvider provider,
                                                 AuthenticationCodeRepository repository) {
        return new DefaultAuthenticationCodeManager(repository, provider);
    }

}
