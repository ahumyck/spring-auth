package com.auth.framework.core.tokens.jwt.keys.provider;

import com.auth.framework.core.exceptions.ProviderException;

public interface Provider<T> {

    T provide() throws ProviderException;
}
