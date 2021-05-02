package com.auth.framework.core.tokens.jwt.keys.asymmetric.rsa.reader;

import com.auth.framework.core.exceptions.ProviderException;
import com.auth.framework.core.tokens.jwt.keys.provider.KeyPairProvider;
import com.auth.framework.core.tokens.jwt.keys.provider.jwk.AsymmetricJsonWebKeyProvider;
import org.jose4j.jwk.JsonWebKey;
import org.jose4j.jwk.PublicJsonWebKey;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.lang.JoseException;

import java.security.KeyPair;

public class RsaJsonWebKeyReaderProvider implements AsymmetricJsonWebKeyProvider {


    private final KeyPairProvider keyPairProvider;
    private final String algorithmIdentifier;
    private final String keyId;

    public RsaJsonWebKeyReaderProvider(KeyPairProvider keyPairProvider,
                                       String algorithmIdentifier,
                                       String keyId) {
        this.keyPairProvider = keyPairProvider;
        this.algorithmIdentifier = algorithmIdentifier;
        this.keyId = keyId;
    }


    @Override
    public JsonWebKey provide() throws ProviderException {
        try {
            KeyPair keyPair = keyPairProvider.provide();
            RsaJsonWebKey rsaJwk = (RsaJsonWebKey) PublicJsonWebKey.Factory.newPublicJwk(keyPair.getPublic());
            rsaJwk.setPrivateKey(keyPair.getPrivate());
            rsaJwk.setKeyId(keyId);
            rsaJwk.setAlgorithm(algorithmIdentifier);
            return rsaJwk;
        } catch (JoseException e) {
            throw new ProviderException(e);
        }

    }
}
