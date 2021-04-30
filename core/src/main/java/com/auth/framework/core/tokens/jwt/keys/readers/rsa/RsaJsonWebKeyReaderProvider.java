package com.auth.framework.core.tokens.jwt.keys.readers.rsa;

import com.auth.framework.core.exceptions.ProviderException;
import com.auth.framework.core.tokens.jwt.keys.provider.BaseKeyProvider;
import com.auth.framework.core.tokens.jwt.keys.provider.Provider;
import org.jose4j.jwk.JsonWebKey;
import org.jose4j.jwk.PublicJsonWebKey;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.lang.JoseException;

import java.security.KeyPair;

public class RsaJsonWebKeyReaderProvider implements Provider<JsonWebKey> {


    private final BaseKeyProvider baseKeyProvider;
    private final String algorithmIdentifier;
    private final String keyId;

    public RsaJsonWebKeyReaderProvider(BaseKeyProvider baseKeyProvider,
                                       String algorithmIdentifier,
                                       String keyId) {
        this.baseKeyProvider = baseKeyProvider;
        this.algorithmIdentifier = algorithmIdentifier;
        this.keyId = keyId;
    }


    @Override
    public JsonWebKey provide() throws ProviderException {
        try {
            KeyPair keyPair = baseKeyProvider.provide();
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
