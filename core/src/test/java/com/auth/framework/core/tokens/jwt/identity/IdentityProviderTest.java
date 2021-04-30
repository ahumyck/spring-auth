package com.auth.framework.core.tokens.jwt.identity;

import com.auth.framework.core.exceptions.ProviderException;
import com.auth.framework.core.tokens.jwt.JsonWebToken;
import com.auth.framework.core.tokens.jwt.factory.TokenFactory;
import com.auth.framework.core.tokens.jwt.keys.provider.BaseKeyPairProvider;
import com.auth.framework.core.tokens.jwt.keys.provider.PrivateKeyProvider;
import com.auth.framework.core.tokens.jwt.keys.provider.PublicKeyProvider;
import com.auth.framework.core.tokens.jwt.keys.readers.hmac.HmacJsonWebKeyProvider;
import com.auth.framework.core.tokens.jwt.keys.readers.rsa.PrivateRsaKeyReaderProvider;
import com.auth.framework.core.tokens.jwt.keys.readers.rsa.PublicRsaKeyReaderProvider;
import com.auth.framework.core.tokens.jwt.keys.readers.rsa.RsaJsonWebKeyReaderProvider;
import com.auth.framework.core.tokens.jwt.params.TokenParameters;
import lombok.SneakyThrows;
import org.jose4j.jwk.JsonWebKey;
import org.jose4j.jwk.RsaJwkGenerator;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

class IdentityProviderTest {

    private TokenFactory factory;

    @BeforeEach
    public void prepare() {
        factory = (username, rawToken, duration, parameters) -> new JsonWebToken() {
            private static final long serialVersionUID = 2114203656236622532L;

            @Override
            public String getOwner() {
                return username;
            }

            @Override
            public String getRawToken() {
                return rawToken;
            }

            @Override
            public Integer getDuration() {
                return duration;
            }

            @Override
            public Object getParameter(String parameterName) {
                return null;
            }

            @Override
            public void addParameter(String parameterName, Object parameterValue) {

            }

            @Override
            public TokenParameters getTokenParameters() {
                return parameters;
            }
        };

    }

    @SneakyThrows
    @Test
    public void test() {
        JsonWebKey publicJsonWebKey = RsaJwkGenerator.generateJwk(2048);

        publicJsonWebKey.setAlgorithm(AlgorithmIdentifiers.RSA_USING_SHA256);
        publicJsonWebKey.setKeyId("k1");

        IdentityProvider identityProvider = new PublicKeyIdentityProvider(publicJsonWebKey,
                AlgorithmIdentifiers.RSA_USING_SHA256,
                300,
                2,
                30,
                factory
        );

        String username = "username";

        JsonWebToken jsonWebToken = identityProvider.generateTokenForUser(username, null);
        String owner = identityProvider.resolveOwner(jsonWebToken.getRawToken());


        Assertions.assertEquals(username, owner);
    }

    @Test
    public void rsaJwkTest() throws ProviderException {
        String privatePath = "D:\\Program Files (x86)\\java\\java-diplom\\core\\src\\test\\resources\\privateKey.pem";
        String publicPath = "D:\\Program Files (x86)\\java\\java-diplom\\core\\src\\test\\resources\\publicKey.pem";

        PrivateKeyProvider privateKeyProvider = new PrivateRsaKeyReaderProvider(privatePath);
        PublicKeyProvider publicKeyProvider = new PublicRsaKeyReaderProvider(publicPath);

        BaseKeyPairProvider baseKeyPairProvider = new BaseKeyPairProvider(privateKeyProvider, publicKeyProvider);
        JsonWebKey rsaJsonWebKey = new RsaJsonWebKeyReaderProvider(
                baseKeyPairProvider,
                AlgorithmIdentifiers.RSA_USING_SHA256,
                "k1"
        ).provide();

        IdentityProvider identityProvider = new PublicKeyIdentityProvider(rsaJsonWebKey,
                AlgorithmIdentifiers.RSA_USING_SHA256,
                300,
                2,
                30,
                factory
        );

        String username = "username";

        JsonWebToken jsonWebToken = identityProvider.generateTokenForUser(username, null);
        String owner = identityProvider.resolveOwner(jsonWebToken.getRawToken());
        Assertions.assertEquals(username, owner);
    }

    @Test
    public void hmacJwkTest() throws ProviderException {

        JsonWebKey jsonWebKey = new HmacJsonWebKeyProvider(
                UUID.randomUUID().toString(),
                AlgorithmIdentifiers.HMAC_SHA256,
                "hmac"
        ).provide();

        IdentityProvider identityProvider = new HmacKeyIdentityProvider(jsonWebKey,
                AlgorithmIdentifiers.HMAC_SHA256,
                300,
                2,
                30,
                factory
        );

        String username = "username";

        JsonWebToken jsonWebToken = identityProvider.generateTokenForUser(username, null);
        String owner = identityProvider.resolveOwner(jsonWebToken.getRawToken());
        Assertions.assertEquals(username, owner);
    }
}