package com.auth.framework.core.tokens.jwt.keys.provider;

import com.auth.framework.core.exceptions.ProviderException;

import java.security.KeyPair;

public class BaseKeyPairProvider implements KeyPairProvider {

    private final PrivateKeyProvider privateKeyProvider;
    private final PublicKeyProvider publicKeyProvider;

    public BaseKeyPairProvider(PrivateKeyProvider privateKeyProvider, PublicKeyProvider publicKeyProvider) {
        this.privateKeyProvider = privateKeyProvider;
        this.publicKeyProvider = publicKeyProvider;
    }


    @Override
    public KeyPair provide() throws ProviderException {
        return new KeyPair(publicKeyProvider.provide(), privateKeyProvider.provide());
    }
}
