package com.auth.framework.core.jwt.identity;

import com.auth.framework.core.jwt.Token;
import com.auth.framework.core.jwt.factory.TokenFactory;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jwk.RsaJwkGenerator;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.lang.JoseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class IdentityProviderImplTest {


    @Test
    public void test() throws JoseException {

        TokenFactory factory = (username, rawToken, duration) -> new Token() {
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

        Token token = identityProvider.generateTokenForUser(username);
        String owner = identityProvider.resolveOwner(token.getRawToken());
        Assertions.assertEquals(username, owner);
    }
}