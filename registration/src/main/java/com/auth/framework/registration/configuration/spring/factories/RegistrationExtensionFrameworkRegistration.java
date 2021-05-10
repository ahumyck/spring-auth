package com.auth.framework.registration.configuration.spring.factories;

import com.auth.framework.registration.token.password.PasswordToken;
import com.auth.framework.registration.token.password.generator.BasePasswordTokenGenerator;
import com.auth.framework.registration.token.password.generator.PasswordTokenGenerator;
import com.auth.framework.registration.token.password.generator.RedisPasswordTokenGenerator;
import com.auth.framework.registration.token.password.manager.PasswordTokenManager;
import com.auth.framework.registration.token.password.manager.PasswordTokenManagerImpl;
import com.auth.framework.registration.token.password.repository.PasswordTokenRepository;
import com.auth.framework.registration.token.password.repository.RedisPasswordTokenRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@EnableConfigurationProperties(RedisPasswordTokenConfigurationProperties.class)
public class RegistrationExtensionFrameworkRegistration {

    @Bean
    @ConditionalOnMissingBean(RedisTemplate.class)
    public RedisTemplate<String, PasswordToken> redisTemplate(LettuceConnectionFactory lettuceConnectionFactory) {
        RedisTemplate<String, PasswordToken> template = new RedisTemplate<>();
        template.setConnectionFactory(lettuceConnectionFactory);
        return template;
    }


    @Bean
    @ConditionalOnMissingBean(PasswordTokenRepository.class)
    public PasswordTokenRepository tokenRedisRepository(RedisTemplate<String, PasswordToken> redisTemplate) {
        return new RedisPasswordTokenRepository(redisTemplate);
    }


    @Bean
    @ConditionalOnMissingBean(PasswordTokenGenerator.class)
    public PasswordTokenGenerator passwordTokenGenerator() {
        return new RedisPasswordTokenGenerator(
                new BasePasswordTokenGenerator()
        );
    }


    @Bean
    @ConditionalOnMissingBean(PasswordTokenManager.class)
    public PasswordTokenManager manager(PasswordTokenGenerator generator,
                                        PasswordTokenRepository repository,
                                        RedisPasswordTokenConfigurationProperties properties) {
        return new PasswordTokenManagerImpl(generator, repository, properties.getTimeToLive());
    }
}
