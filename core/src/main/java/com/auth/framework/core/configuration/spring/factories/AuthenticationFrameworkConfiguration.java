package com.auth.framework.core.configuration.spring.factories;


import com.auth.framework.core.access.admin.AdminUserValidator;
import com.auth.framework.core.access.admin.AdminUserValidatorDefault;
import com.auth.framework.core.access.admin.AdminValidatorWithInjectedAdminUserRoleName;
import com.auth.framework.core.action.executor.ActionExecutor;
import com.auth.framework.core.action.executor.ActionExecutorImpl;
import com.auth.framework.core.attribute.AttributeConfigurer;
import com.auth.framework.core.encryption.AESEncryptionService;
import com.auth.framework.core.encryption.EncryptionService;
import com.auth.framework.core.encryption.generator.RandomPasswordGenerator;
import com.auth.framework.core.encryption.generator.RandomPasswordGeneratorImpl;
import com.auth.framework.core.tokens.jwt.factory.TokenFactory;
import com.auth.framework.core.tokens.jwt.factory.TokenFactoryImpl;
import com.auth.framework.core.tokens.jwt.filter.TokenFilter;
import com.auth.framework.core.tokens.jwt.identity.IdentityProvider;
import com.auth.framework.core.tokens.jwt.identity.PublicKeyIdentityProvider;
import com.auth.framework.core.tokens.jwt.managers.TokenManager;
import com.auth.framework.core.tokens.jwt.managers.TokenManagerImpl;
import com.auth.framework.core.tokens.jwt.managers.session.SessionManager;
import com.auth.framework.core.tokens.jwt.managers.session.SessionManagerImpl;
import com.auth.framework.core.tokens.jwt.repository.InMemoryTokenRepository;
import com.auth.framework.core.tokens.jwt.repository.TokenRepository;
import com.auth.framework.core.tokens.jwt.transport.CookieTransport;
import com.auth.framework.core.tokens.jwt.transport.TokenTransport;
import com.auth.framework.core.users.UserPrincipalService;
import com.auth.framework.core.utils.ValidationCenter;
import lombok.extern.slf4j.Slf4j;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jwk.RsaJwkGenerator;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.lang.JoseException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableConfigurationProperties({
        AuthenticationFrameworkProperties.class,
        RedisJwtTokenConfigurationProperties.class,
        IdentityProviderProperties.class
})
@Slf4j
public class AuthenticationFrameworkConfiguration {

    //random password generator
    @Bean
    @ConditionalOnMissingBean(RandomPasswordGenerator.class)
    public RandomPasswordGenerator randomPasswordGenerator() {
        return new RandomPasswordGeneratorImpl();
    }


    //encryption service
    @Bean
    @ConditionalOnMissingBean(EncryptionService.class)
    public EncryptionService encryptionService(RandomPasswordGenerator randomPasswordGenerator) {
        return new AESEncryptionService(randomPasswordGenerator.generatePasswordAsRawBytes());
    }

    //Token part
    @Bean
    @ConditionalOnProperty("authentication.enable-tokens")
    @ConditionalOnMissingBean(TokenFactory.class)
    public TokenFactory tokenFactory(EncryptionService service) {
        return new TokenFactoryImpl(service);
    }


    @Bean
    @ConditionalOnProperty("authentication.enable-tokens")
    @ConditionalOnMissingBean(IdentityProvider.class)
    @ConditionalOnBean(TokenFactory.class)
    public IdentityProvider identityProvider(IdentityProviderProperties identityProviderProperties,
                                             TokenFactory factory) throws JoseException {
        RsaJsonWebKey rsaJsonWebKey = RsaJwkGenerator.generateJwk(2048);
        rsaJsonWebKey.setAlgorithm(AlgorithmIdentifiers.RSA_USING_SHA256);
        rsaJsonWebKey.setKeyId("k1");
        return new PublicKeyIdentityProvider(rsaJsonWebKey,
                AlgorithmIdentifiers.RSA_USING_SHA256,
                300,
                2,
                30,
                factory
        );
    }

    @Bean
    @ConditionalOnProperty("authentication.enable-tokens")
    @ConditionalOnMissingBean(TokenTransport.class)
    public TokenTransport transport() {
        return new CookieTransport();
    }

    @Bean
    @ConditionalOnProperty({"authentication.enable-tokens"})
    @ConditionalOnMissingBean(TokenRepository.class)
    public TokenRepository storage() {
        return new InMemoryTokenRepository(300, TimeUnit.MINUTES);
    }


    @Bean
    @ConditionalOnProperty("authentication.enable-tokens")
    @ConditionalOnMissingBean(TokenManager.class)
    @ConditionalOnBean(TokenRepository.class)
    public TokenManager tokenManager(IdentityProvider identityProvider,
                                     TokenRepository storage,
                                     TokenTransport transport,
                                     EncryptionService encryptionService) {
        return new TokenManagerImpl(identityProvider, storage, transport, encryptionService);
    }


    @Bean
    @ConditionalOnProperty("authentication.enable-tokens")
    @ConditionalOnBean(UserPrincipalService.class)
    public TokenFilter tokenFilter(TokenManager manager,
                                   UserPrincipalService principalService) {
        return new TokenFilter(manager, principalService);
    }


    @Bean
    @ConditionalOnMissingBean(AdminUserValidator.class)
    public AdminUserValidator validator(AuthenticationFrameworkProperties properties) {
        String adminRoleName = properties.getAdminRoleName();
        if (adminRoleName == null || "".equals(adminRoleName)) {
            return new AdminUserValidatorDefault();
        }
        return new AdminValidatorWithInjectedAdminUserRoleName(adminRoleName);
    }

    //action executor
    @Bean
    @ConditionalOnMissingBean(ActionExecutor.class)
    public ActionExecutor actionExecutor(AdminUserValidator validator) {
        return new ActionExecutorImpl(validator);
    }


    //password tokens
    //redis configuration
    @Bean
    @ConditionalOnProperty("authentication.enable-tokens")
    @ConditionalOnMissingBean(LettuceConnectionFactory.class)
    public LettuceConnectionFactory lettuceConnectionFactory(RedisJwtTokenConfigurationProperties properties) {
        String host = properties.getHost();
        Integer port = properties.getPort();
        host = ValidationCenter.isValidString(host) ? host : "localhost";
        port = ValidationCenter.isValidPort(port) ? port : 6379;
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(host, port);
        return new LettuceConnectionFactory(config);
    }

    //Session manager
    @Bean
    @ConditionalOnProperty("authentication.enable-tokens")
    @ConditionalOnMissingBean(SessionManager.class)
    public SessionManager sessionManager(TokenRepository repository, TokenManager manager) {
        return new SessionManagerImpl(repository, manager);
    }

    //attributes
    @Bean
    @ConditionalOnMissingBean(AttributeConfigurer.class)
    public AttributeConfigurer attributeConfigurer() {
        return new AttributeConfigurer();
    }
}
