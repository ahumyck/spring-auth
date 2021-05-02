package com.auth.framework.core.tokens.jwt.keys.asymmetric.rsa.random;

import com.auth.framework.core.exceptions.ProviderException;
import com.auth.framework.core.tokens.jwt.keys.provider.jwk.AsymmetricJsonWebKeyProvider;
import org.jose4j.jwk.JsonWebKey;
import org.jose4j.jwk.RsaJwkGenerator;
import org.jose4j.lang.JoseException;

public class RandomRsaJsonWebKeyProvider implements AsymmetricJsonWebKeyProvider {
    @Override
    public JsonWebKey provide() throws ProviderException {
        try {
            return RsaJwkGenerator.generateJwk(2048);
        } catch (JoseException e) {
            throw new RuntimeException(e);
        }
    }
}
