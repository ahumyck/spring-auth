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
import com.auth.framework.core.jwt.Token;
import com.auth.framework.core.jwt.factory.TokenFactory;
import com.auth.framework.core.jwt.filter.TokenFilter;
import com.auth.framework.core.jwt.identity.IdentityProvider;
import com.auth.framework.core.jwt.identity.IdentityProviderImpl;
import com.auth.framework.core.jwt.manager.TokenManager;
import com.auth.framework.core.jwt.manager.TokenManagerImpl;
import com.auth.framework.core.jwt.redis.RedisTokenFactory;
import com.auth.framework.core.jwt.repository.RedisTokenStorage;
import com.auth.framework.core.jwt.repository.low.TokenRedisRepository;
import com.auth.framework.core.jwt.repository.low.TokenRedisRepositoryImpl;
import com.auth.framework.core.jwt.repository.TokenStorage;
import com.auth.framework.core.jwt.transport.CookieTransport;
import com.auth.framework.core.jwt.transport.TokenTransport;
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

@Configuration
@EnableConfigurationProperties({AuthenticationFrameworkProperties.class,
        TokenStorageConfigurationProperties.class,
        AuthenticationFrameworkProperties.IdentityProviderProperties.class})
@Slf4j
public class AuthenticationFrameworkConfiguration {

    //redis configuration
    @Bean
    @ConditionalOnProperty({"authentication.enable-tokens", "token-storage.use-default-redis-storage"})
    @ConditionalOnMissingBean(LettuceConnectionFactory.class)
    public LettuceConnectionFactory lettuceConnectionFactory(TokenStorageConfigurationProperties properties) {
        String host = properties.getHost();
        Integer port = properties.getPort();
        host = ValidationCenter.isValidString(host) ? host : "localhost";
        port = ValidationCenter.isValidPort(port) ? port : 6379;
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(host, port);
        return new LettuceConnectionFactory(config);
    }

    @Bean
    @ConditionalOnProperty({"authentication.enable-tokens", "token-storage.use-default-redis-storage"})
    @ConditionalOnMissingBean(RedisTemplate.class)
    public RedisTemplate<String, Token> redisTemplate(LettuceConnectionFactory connectionFactory) {
        RedisTemplate<String, Token> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        return template;
    }


    @Bean
    @ConditionalOnProperty({"authentication.enable-tokens", "token-storage.use-default-redis-storage"})
    @ConditionalOnBean(RedisTemplate.class)
    @ConditionalOnMissingBean(TokenRedisRepository.class)
    public TokenRedisRepository tokenRedisRepository(RedisTemplate<String, Token> redisTemplate) {
        return new TokenRedisRepositoryImpl(redisTemplate);
    }

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
        return new RedisTokenFactory(service);
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
    @ConditionalOnProperty({"authentication.enable-tokens", "token-storage.use-default-redis-storage"})
    @ConditionalOnMissingBean(TokenStorage.class)
    public TokenStorage storage(TokenRedisRepository redisRepository) {
        return new RedisTokenStorage(redisRepository);
    }


    @Bean
    @ConditionalOnProperty("authentication.enable-tokens")
    @ConditionalOnMissingBean(TokenManager.class)
    @ConditionalOnBean(TokenStorage.class)
    public TokenManager tokenManager(IdentityProvider identityProvider,
                                     TokenStorage storage,
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
}
