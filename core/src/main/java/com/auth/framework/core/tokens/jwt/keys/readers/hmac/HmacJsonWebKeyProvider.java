package com.auth.framework.core.tokens.jwt.keys.readers.hmac;

import com.auth.framework.core.exceptions.ProviderException;
import com.auth.framework.core.tokens.jwt.keys.provider.Provider;
import org.jose4j.jwk.JsonWebKey;
import org.jose4j.jwk.OctetSequenceJsonWebKey;
import org.jose4j.keys.HmacKey;

import java.nio.charset.StandardCharsets;

public class HmacJsonWebKeyProvider implements Provider<JsonWebKey> {

    private final String password;
    private final String algorithmIdentifier;
    private final String keyId;

    public HmacJsonWebKeyProvider(String password,
                                  String algorithmIdentifier,
                                  String keyId) {
        this.algorithmIdentifier = algorithmIdentifier;
        this.keyId = keyId;
        this.password = password;
    }

    @Override
    public JsonWebKey provide() throws ProviderException {
        OctetSequenceJsonWebKey jsonWebKey = new OctetSequenceJsonWebKey(new HmacKey(password.getBytes(StandardCharsets.UTF_8)));
        jsonWebKey.setAlgorithm(algorithmIdentifier);
        jsonWebKey.setKeyId(keyId);
        return jsonWebKey;
    }
}
