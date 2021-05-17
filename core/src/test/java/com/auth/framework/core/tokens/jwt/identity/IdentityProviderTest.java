package com.auth.framework.core.tokens.jwt.identity;

import com.auth.framework.core.exceptions.ProviderException;
import com.auth.framework.core.exceptions.ResolveOwnerException;
import com.auth.framework.core.exceptions.TokenGenerationException;
import com.auth.framework.core.tokens.jwt.JsonWebToken;
import com.auth.framework.core.tokens.jwt.factory.TokenFactory;
import com.auth.framework.core.tokens.jwt.keys.asymmetric.rsa.random.RandomRsaJsonWebKeyProvider;
import com.auth.framework.core.tokens.jwt.keys.provider.BaseKeyPairProvider;
import com.auth.framework.core.tokens.jwt.keys.provider.PrivateKeyProvider;
import com.auth.framework.core.tokens.jwt.keys.provider.PublicKeyProvider;
import com.auth.framework.core.tokens.jwt.keys.symmetric.hmac.HmacJsonWebKeyProvider;
import com.auth.framework.core.tokens.jwt.keys.asymmetric.rsa.reader.PrivateRsaKeyReaderProvider;
import com.auth.framework.core.tokens.jwt.keys.asymmetric.rsa.reader.PublicRsaKeyReaderProvider;
import com.auth.framework.core.tokens.jwt.keys.asymmetric.rsa.reader.RsaJsonWebKeyReaderProvider;
import com.auth.framework.core.utils.DateUtils;
import lombok.SneakyThrows;
import org.jose4j.jwk.JsonWebKey;
import org.jose4j.jwk.RsaJwkGenerator;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.lang.JoseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

class IdentityProviderTest {

    private TokenFactory factory;

    @BeforeEach
    public void prepare() {
        factory = (username, rawToken, duration, parameters) -> new JsonWebToken() {
            private static final long serialVersionUID = 2114203656236622532L;

            private final Date expireDate = DateUtils.createDateFromNow(duration, TimeUnit.MINUTES);

            @Override
            public String getOwner() {
                return username;
            }

            @Override
            public String getRawToken() {
                return rawToken;
            }

            @Override
            public Integer getTimeToLive() {
                return duration;
            }

            @Override
            public Date getExpireDate() {
                return expireDate;
            }

            @Override
            public Object getParameter(String parameterName) {
                return null;
            }

            @Override
            public void addParameter(String parameterName, Object parameterValue) {
            }

            @Override
            public Map<String, Object> getTokenParameters() {
                return parameters;
            }
        };
    }

    @Test
    @SneakyThrows
    public void test() {
        JsonWebKey jsonWebKey = new RandomRsaJsonWebKeyProvider().provide();

        IdentityProvider identityProvider = new AsymmetricKeyIdentityProvider(jsonWebKey,
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
    public void rsaJwkTest() throws ProviderException, TokenGenerationException, ResolveOwnerException {
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

        IdentityProvider identityProvider = new AsymmetricKeyIdentityProvider(rsaJsonWebKey,
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
    public void hmacJwkTest() throws ProviderException, TokenGenerationException, ResolveOwnerException {

        JsonWebKey jsonWebKey = new HmacJsonWebKeyProvider(
                UUID.randomUUID().toString(),
                AlgorithmIdentifiers.HMAC_SHA256,
                "hmac"
        ).provide();

        IdentityProvider identityProvider = new SymmetricKeyIdentityProvider(jsonWebKey,
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