package com.auth.framework.core.tokens.jwt.identity;

import com.auth.framework.exceptions.WrongTypeSigningKeyException;
import com.auth.framework.core.tokens.jwt.factory.TokenFactory;
import org.jose4j.jwk.JsonWebKey;
import org.jose4j.jwk.PublicJsonWebKey;

import java.security.Key;

public class AsymmetricKeyIdentityProvider extends BaseIdentityProvider {

    public AsymmetricKeyIdentityProvider(JsonWebKey jsonWebKey,
                                         Integer durationTime,
                                         Integer activeBefore,
                                         Integer allowedClockSkewInSeconds,
                                         TokenFactory factory) {
        super(jsonWebKey, durationTime, activeBefore, allowedClockSkewInSeconds, factory);
    }

    @Override
    protected Key getPrivateKey(JsonWebKey jsonWebKey) throws WrongTypeSigningKeyException {
        if (jsonWebKey instanceof PublicJsonWebKey) {
            PublicJsonWebKey publicJsonWebKey = (PublicJsonWebKey) jsonWebKey;
            return publicJsonWebKey.getPrivateKey();
        }
        throw new WrongTypeSigningKeyException("Unexpected key type. PublicJsonWebKey expected");
    }

    @Override
    protected Key getPublicKey(JsonWebKey jsonWebKey) throws WrongTypeSigningKeyException {
        if (jsonWebKey instanceof PublicJsonWebKey) {
            PublicJsonWebKey publicJsonWebKey = (PublicJsonWebKey) jsonWebKey;
            return publicJsonWebKey.getPublicKey();
        }
        throw new WrongTypeSigningKeyException("Unexpected key type. PublicJsonWebKey expected");
    }
}
