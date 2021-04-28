package com.auth.framework.core.tokens.jwt.identity;

import com.auth.framework.core.tokens.jwt.JsonWebToken;
import com.auth.framework.core.tokens.jwt.factory.TokenFactory;
import com.auth.framework.core.tokens.jwt.params.TokenParameters;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jwk.RsaJwkGenerator;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.lang.JoseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class IdentityProviderImplTest {


    @Test
    public void test() throws JoseException {

        TokenFactory factory = (username, rawToken, duration, session, parameters) -> new JsonWebToken() {
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

        JsonWebToken jsonWebToken = identityProvider.generateTokenForUser(username, "", null);
        String owner = identityProvider.resolveOwner(jsonWebToken.getRawToken());
        Assertions.assertEquals(username, owner);
    }
}