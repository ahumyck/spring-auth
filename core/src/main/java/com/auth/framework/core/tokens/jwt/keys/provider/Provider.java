package com.auth.framework.core.tokens.jwt.keys.provider;

import com.auth.framework.exceptions.ProviderException;

public interface Provider<T> {

    T provide() throws ProviderException;
}
