package com.auth.framework.core.tokens.jwt.identity;

import com.auth.framework.core.exceptions.WrongTypeSigningKeyException;
import com.auth.framework.core.tokens.jwt.factory.TokenFactory;
import org.jose4j.jwk.JsonWebKey;
import org.jose4j.jwk.OctetSequenceJsonWebKey;

import java.security.Key;

public class SymmetricKeyIdentityProvider extends BaseIdentityProvider {
    public SymmetricKeyIdentityProvider(JsonWebKey jsonWebKey, Integer durationTime, Integer activeBefore, Integer allowedClockSkewInSeconds, TokenFactory factory) {
        super(jsonWebKey, durationTime, activeBefore, allowedClockSkewInSeconds, factory);
    }

    @Override
    protected Key getPrivateKey(JsonWebKey jsonWebKey) throws WrongTypeSigningKeyException {
        if (jsonWebKey instanceof OctetSequenceJsonWebKey) {
            OctetSequenceJsonWebKey key = (OctetSequenceJsonWebKey) jsonWebKey;
            return key.getKey();
        }
        throw new WrongTypeSigningKeyException("Unexpected key type. OctetSequenceJsonWebKey expected");
    }

    @Override
    protected Key getPublicKey(JsonWebKey jsonWebKey) throws WrongTypeSigningKeyException {
        if (jsonWebKey instanceof OctetSequenceJsonWebKey) {
            OctetSequenceJsonWebKey key = (OctetSequenceJsonWebKey) jsonWebKey;
            return key.getKey();
        }
        throw new WrongTypeSigningKeyException("Unexpected key type. OctetSequenceJsonWebKey expected");
    }
}
