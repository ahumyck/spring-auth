package com.auth.framework.core.conf;


import com.auth.framework.core.access.AccessControlManager;
import com.auth.framework.core.access.XMLAccessControlManager;
import com.auth.framework.core.access.isAdmin.IsUserAdminValidator;
import com.auth.framework.core.access.isAdmin.IsUserAdminValidatorDefault;
import com.auth.framework.core.access.isAdmin.IsUserAdminValidatorWithInjectedAdminRoleName;
import com.auth.framework.core.action.executor.ActionExecutor;
import com.auth.framework.core.action.executor.ActionExecutorImpl;
import com.auth.framework.core.encryption.AESEncryptionService;
import com.auth.framework.core.encryption.EncryptionService;
import com.auth.framework.core.encryption.generator.RandomPasswordGenerator;
import com.auth.framework.core.encryption.generator.RandomPasswordGeneratorImpl;
import com.auth.framework.core.tokens.jwt.factory.TokenFactory;
import com.auth.framework.core.tokens.jwt.factory.TokenFactoryImpl;
import com.auth.framework.core.tokens.jwt.filter.TokenFilter;
import com.auth.framework.core.tokens.jwt.identity.IdentityProvider;
import com.auth.framework.core.tokens.jwt.identity.IdentityProviderImpl;
import com.auth.framework.core.tokens.jwt.manager.SessionManager;
import com.auth.framework.core.tokens.jwt.manager.SessionManagerImpl;
import com.auth.framework.core.tokens.jwt.manager.TokenManager;
import com.auth.framework.core.tokens.jwt.manager.TokenManagerImpl;
import com.auth.framework.core.tokens.jwt.repository.InMemoryTokenRepository;
import com.auth.framework.core.tokens.jwt.repository.TokenRepository;
import com.auth.framework.core.tokens.jwt.transport.CookieTransport;
import com.auth.framework.core.tokens.jwt.transport.TokenTransport;
import com.auth.framework.core.tokens.password.PasswordToken;
import com.auth.framework.core.tokens.password.generator.PasswordTokenGenerator;
import com.auth.framework.core.tokens.password.generator.PasswordTokenGeneratorImpl;
import com.auth.framework.core.tokens.password.manager.PasswordTokenManager;
import com.auth.framework.core.tokens.password.manager.PasswordTokenManagerImpl;
import com.auth.framework.core.tokens.password.repository.PasswordTokenRepository;
import com.auth.framework.core.tokens.password.repository.RedisPasswordTokenRepository;
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
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableConfigurationProperties({AuthenticationFrameworkProperties.class,
        RedisPasswordConfigurationProperties.class,
        AuthenticationFrameworkProperties.IdentityProviderProperties.class})
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
    public IdentityProvider identityProvider(AuthenticationFrameworkProperties.IdentityProviderProperties identityProviderProperties,
                                             TokenFactory factory) throws JoseException {
        RsaJsonWebKey rsaJsonWebKey = RsaJwkGenerator.generateJwk(2048);
        rsaJsonWebKey.setAlgorithm(AlgorithmIdentifiers.RSA_USING_SHA256);
        rsaJsonWebKey.setKeyId("k1");
        return new IdentityProviderImpl(rsaJsonWebKey,
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


    //xml attribute model configuration
    @Bean
    @ConditionalOnMissingBean(AccessControlManager.class)
    @ConditionalOnProperty("authentication.attribute-model-filename")
    public AccessControlManager accessControlManager(AuthenticationFrameworkProperties properties,
                                                     IsUserAdminValidator validator) {
        return new XMLAccessControlManager(properties.getEnableAttributeModel(), validator);
    }

    @Bean
    @ConditionalOnMissingBean(IsUserAdminValidator.class)
//    @ConditionalOnProperty("authentication.attribute-model-filename")
    public IsUserAdminValidator validator(AuthenticationFrameworkProperties properties) {
        String adminRoleName = properties.getAdminRoleName();
        if (adminRoleName == null || "".equals(adminRoleName)) {
            return new IsUserAdminValidatorDefault();
        }
        return new IsUserAdminValidatorWithInjectedAdminRoleName(adminRoleName);
    }

    //action executor
    @Bean
    @ConditionalOnMissingBean(ActionExecutor.class)
    @ConditionalOnBean(IsUserAdminValidator.class)
    public ActionExecutor actionExecutor(IsUserAdminValidator validator) {
        return new ActionExecutorImpl(validator);
    }


    //password tokens
    //redis configuration
    @Bean
    @ConditionalOnProperty("authentication.enable-tokens")
    @ConditionalOnMissingBean(LettuceConnectionFactory.class)
    public LettuceConnectionFactory lettuceConnectionFactory(RedisPasswordConfigurationProperties properties) {
        String host = properties.getHost();
        Integer port = properties.getPort();
        host = ValidationCenter.isValidString(host) ? host : "localhost";
        port = ValidationCenter.isValidPort(port) ? port : 6379;
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(host, port);
        return new LettuceConnectionFactory(config);
    }

    @Bean
    @ConditionalOnProperty("authentication.enable-tokens")
    @ConditionalOnMissingBean(RedisTemplate.class)
    public RedisTemplate<String, PasswordToken> redisTemplate(LettuceConnectionFactory lettuceConnectionFactory) {
        RedisTemplate<String, PasswordToken> template = new RedisTemplate<>();
        template.setConnectionFactory(lettuceConnectionFactory);
        return template;
    }


    @Bean
    @ConditionalOnProperty("authentication.enable-tokens")
    @ConditionalOnMissingBean(PasswordTokenRepository.class)
    public PasswordTokenRepository tokenRedisRepository(RedisTemplate<String, PasswordToken> redisTemplate) {
        return new RedisPasswordTokenRepository(redisTemplate);
    }


    @Bean
    @ConditionalOnProperty("authentication.enable-tokens")
    @ConditionalOnMissingBean(PasswordTokenGenerator.class)
    public PasswordTokenGenerator passwordTokenGenerator() {
        return new PasswordTokenGeneratorImpl();
    }


    @Bean
    @ConditionalOnProperty("authentication.enable-tokens")
    @ConditionalOnMissingBean(PasswordTokenManager.class)
    public PasswordTokenManager manager(PasswordTokenGenerator generator,
                                        PasswordTokenRepository repository,
                                        RedisPasswordConfigurationProperties properties) {
        return new PasswordTokenManagerImpl(generator, repository, properties.getTimeToLive());
    }

    //Session manager
    @Bean
    @ConditionalOnProperty("authentication.enable-tokens")
    @ConditionalOnMissingBean(SessionManager.class)
    public SessionManager sessionManager(TokenRepository repository) {
        return new SessionManagerImpl(repository);
    }
}
