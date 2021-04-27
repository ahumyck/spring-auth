package com.auth.framework.core.tokens.jwt.identity;

import com.auth.framework.core.tokens.jwt.JsonWebToken;
import com.auth.framework.core.tokens.jwt.factory.TokenFactory;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jwk.RsaJwkGenerator;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.lang.JoseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class IdentityProviderImplTest {


    @Test
    public void test() throws JoseException {

        TokenFactory factory = (username, rawToken, duration, session) -> new JsonWebToken() {
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
            public String getSessionName() {
                return session;
            }
        };

        RsaJsonWebKey rsaJsonWebKey = RsaJwkGenerator.generateJwk(2048);
        rsaJsonWebKey.setAlgorithm(AlgorithmIdentifiers.RSA_USING_SHA256);
        rsaJsonWebKey.setKeyId("k1");

        IdentityProviderImpl identityProvider = new IdentityProviderImpl(rsaJsonWebKey,
                AlgorithmIdentifiers.RSA_USING_SHA256,
                300,
                2,
                30,
                factory
        );

        String username = "username";

        JsonWebToken jsonWebToken = identityProvider.generateTokenForUser(username, "");
        String owner = identityProvider.resolveOwner(jsonWebToken.getRawToken());
        Assertions.assertEquals(username, owner);
    }
}