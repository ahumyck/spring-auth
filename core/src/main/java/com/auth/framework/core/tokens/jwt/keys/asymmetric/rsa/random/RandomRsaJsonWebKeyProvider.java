package com.auth.framework.core.tokens.jwt.keys.asymmetric.rsa.random;

import com.auth.framework.core.exceptions.ProviderException;
import com.auth.framework.core.tokens.jwt.keys.provider.jwk.AsymmetricJsonWebKeyProvider;
import org.jose4j.jwk.JsonWebKey;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jwk.RsaJwkGenerator;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.lang.JoseException;

public class RandomRsaJsonWebKeyProvider implements AsymmetricJsonWebKeyProvider {
    @Override
    public JsonWebKey provide() throws ProviderException {
        try {
            RsaJsonWebKey rsaJsonWebKey = RsaJwkGenerator.generateJwk(2048);
            rsaJsonWebKey.setKeyId("rsa_sha256");
            rsaJsonWebKey.setAlgorithm(AlgorithmIdentifiers.RSA_USING_SHA256);
            return rsaJsonWebKey;
        } catch (JoseException e) {
            throw new RuntimeException(e);
        }
    }
}
