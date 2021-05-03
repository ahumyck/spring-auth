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
import com.auth.framework.core.exceptions.ProviderException;
import com.auth.framework.core.tokens.jwt.factory.TokenFactory;
import com.auth.framework.core.tokens.jwt.factory.TokenFactoryImpl;
import com.auth.framework.core.tokens.jwt.filter.TokenFilter;
import com.auth.framework.core.tokens.jwt.identity.AsymmetricKeyIdentityProvider;
import com.auth.framework.core.tokens.jwt.identity.IdentityProvider;
import com.auth.framework.core.tokens.jwt.identity.SymmetricKeyIdentityProvider;
import com.auth.framework.core.tokens.jwt.keys.asymmetric.rsa.random.RandomRsaJsonWebKeyProvider;
import com.auth.framework.core.tokens.jwt.keys.asymmetric.rsa.reader.PrivateRsaKeyReaderProvider;
import com.auth.framework.core.tokens.jwt.keys.asymmetric.rsa.reader.PublicRsaKeyReaderProvider;
import com.auth.framework.core.tokens.jwt.keys.asymmetric.rsa.reader.RsaJsonWebKeyReaderProvider;
import com.auth.framework.core.tokens.jwt.keys.provider.BaseKeyPairProvider;
import com.auth.framework.core.tokens.jwt.keys.provider.KeyPairProvider;
import com.auth.framework.core.tokens.jwt.keys.provider.PrivateKeyProvider;
import com.auth.framework.core.tokens.jwt.keys.provider.PublicKeyProvider;
import com.auth.framework.core.tokens.jwt.keys.provider.jwk.AsymmetricJsonWebKeyProvider;
import com.auth.framework.core.tokens.jwt.keys.provider.jwk.SymmetricJsonWebKeyProvider;
import com.auth.framework.core.tokens.jwt.keys.symmetric.hmac.HmacJsonWebKeyProvider;
import com.auth.framework.core.tokens.jwt.managers.TokenManager;
import com.auth.framework.core.tokens.jwt.managers.TokenManagerImpl;
import com.auth.framework.core.tokens.jwt.managers.session.SessionManager;
import com.auth.framework.core.tokens.jwt.managers.session.SessionManagerImpl;
import com.auth.framework.core.tokens.jwt.repository.InMemoryTokenRepository;
import com.auth.framework.core.tokens.jwt.repository.TokenRepository;
import com.auth.framework.core.tokens.jwt.transport.CookieTransport;
import com.auth.framework.core.tokens.jwt.transport.TokenTransport;
import com.auth.framework.core.users.UserPrincipalService;
import com.auth.framework.core.users.oauth2.OAuth2UserService;
import com.auth.framework.core.utils.ValidationCenter;
import lombok.extern.slf4j.Slf4j;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;

import java.nio.charset.StandardCharsets;

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
    public EncryptionService encryptionService(IdentityProviderProperties properties,
                                               RandomPasswordGenerator randomPasswordGenerator) {
        String encryptionPassword = properties.getEncryptionPassword();
        if (ValidationCenter.isValidString(encryptionPassword)) {
            return new AESEncryptionService(encryptionPassword.getBytes(StandardCharsets.UTF_8));
        } else {
            return new AESEncryptionService(randomPasswordGenerator.generatePasswordAsRawBytes());
        }
    }

    //Token part
    @Bean
    @ConditionalOnMissingBean(TokenFactory.class)
    public TokenFactory tokenFactory(EncryptionService service) {
        return new TokenFactoryImpl(service);
    }


    //Token identity provider part
    @Bean
    @ConditionalOnMissingBean(SymmetricJsonWebKeyProvider.class)
    public SymmetricJsonWebKeyProvider jsonWebKeyProvider(IdentityProviderProperties properties,
                                                          RandomPasswordGenerator randomPasswordGenerator) {
        String jwtSignPassword = properties.getJwtSignPassword();
        if (!ValidationCenter.isValidString(jwtSignPassword)) {
            jwtSignPassword = randomPasswordGenerator.generatePasswordThenEncodeAsBase64();
        }
        return new HmacJsonWebKeyProvider(
                jwtSignPassword,
                AlgorithmIdentifiers.HMAC_SHA512,
                "hmac_sha512"
        );
    }

    @Bean
    @ConditionalOnMissingBean(PrivateKeyProvider.class)
    public PrivateKeyProvider privateKeyProvider(IdentityProviderProperties properties) {
        String privateKeyPath = properties.getPrivateKeyPath();
        String publicKeyPath = properties.getPublicKeyPath();
        if (ValidationCenter.isValidString(privateKeyPath) && ValidationCenter.isValidString(publicKeyPath)) {
            return new PrivateRsaKeyReaderProvider(properties.getPrivateKeyPath());
        }
        return () -> null;
    }

    @Bean
    @ConditionalOnMissingBean(PublicKeyProvider.class)
    public PublicKeyProvider publicKeyProvider(IdentityProviderProperties properties) {
        String privateKeyPath = properties.getPrivateKeyPath();
        String publicKeyPath = properties.getPublicKeyPath();
        if (ValidationCenter.isValidString(privateKeyPath) && ValidationCenter.isValidString(publicKeyPath)) {
            return new PublicRsaKeyReaderProvider(properties.getPublicKeyPath());
        }
        return () -> null;

    }

    @Bean
    @ConditionalOnMissingBean(KeyPairProvider.class)
    public KeyPairProvider keyPairProvider(IdentityProviderProperties properties,
                                           PrivateKeyProvider privateKeyProvider,
                                           PublicKeyProvider publicKeyProvider) {
        String privateKeyPath = properties.getPrivateKeyPath();
        String publicKeyPath = properties.getPublicKeyPath();
        if (ValidationCenter.isValidString(privateKeyPath) && ValidationCenter.isValidString(publicKeyPath)) {
            return new BaseKeyPairProvider(privateKeyProvider, publicKeyProvider);
        }
        return () -> null;
    }

    @Bean
    @ConditionalOnMissingBean(AsymmetricJsonWebKeyProvider.class)
    public AsymmetricJsonWebKeyProvider asymmetricJsonWebKeyProvider(IdentityProviderProperties properties,
                                                                     KeyPairProvider provider) {
        String privateKeyPath = properties.getPrivateKeyPath();
        String publicKeyPath = properties.getPublicKeyPath();
        if (ValidationCenter.isValidString(privateKeyPath) && ValidationCenter.isValidString(publicKeyPath)) {
            return new RsaJsonWebKeyReaderProvider(
                    provider,
                    AlgorithmIdentifiers.RSA_USING_SHA256,
                    "rsa_sha256"
            );
        }
        return new RandomRsaJsonWebKeyProvider();
    }

    @Bean
    @ConditionalOnMissingBean(IdentityProvider.class)
    @ConditionalOnBean(TokenFactory.class)
    public IdentityProvider identityProvider(AsymmetricJsonWebKeyProvider asymmetricJsonWebKeyProvider,
                                             SymmetricJsonWebKeyProvider symmetricJsonWebKeyProvider,
                                             TokenFactory factory,
                                             IdentityProviderProperties properties) throws ProviderException {
        Integer timeToLive = ValidationCenter.validatedNumberOrDefault(properties.getTimeToLive(), 300);
        Integer activeBefore = ValidationCenter.validatedNumberOrDefault(properties.getActiveBefore(), 2);
        Integer allowedClockSkew = ValidationCenter.validatedNumberOrDefault(properties.getAllowedClockSkew(), 30);


        if (properties.isAsymmetric()) {
            return new AsymmetricKeyIdentityProvider(
                    asymmetricJsonWebKeyProvider.provide(),
                    timeToLive,
                    activeBefore,
                    allowedClockSkew,
                    factory
            );
        } else {
            return new SymmetricKeyIdentityProvider(
                    symmetricJsonWebKeyProvider.provide(),
                    timeToLive,
                    activeBefore,
                    allowedClockSkew,
                    factory
            );
        }
    }

    @Bean
    @ConditionalOnMissingBean(TokenTransport.class)
    public TokenTransport transport() {
        return new CookieTransport();
    }

    @Bean
    @ConditionalOnMissingBean(TokenRepository.class)
    public TokenRepository storage() {
        return new InMemoryTokenRepository();
    }


    @Bean
    @ConditionalOnMissingBean(TokenManager.class)
    @ConditionalOnBean(TokenRepository.class)
    public TokenManager tokenManager(IdentityProvider identityProvider,
                                     TokenRepository storage,
                                     TokenTransport transport,
                                     EncryptionService encryptionService) {
        return new TokenManagerImpl(identityProvider, storage, transport, encryptionService);
    }


    @Bean
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


    //oauth2
    @Bean
    @ConditionalOnMissingBean(DefaultOAuth2UserService.class)
    public DefaultOAuth2UserService oAuth2UserService() {
        log.warn("OAuth2Service");
        return new OAuth2UserService();
    }


    //action executor
    @Bean
    @ConditionalOnMissingBean(ActionExecutor.class)
    public ActionExecutor actionExecutor(AdminUserValidator validator) {
        return new ActionExecutorImpl(validator);
    }


    //Session manager
    @Bean
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
