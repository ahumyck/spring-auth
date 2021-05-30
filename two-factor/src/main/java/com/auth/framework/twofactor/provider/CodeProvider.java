package com.auth.framework.twofactor.provider;


public interface CodeProvider<T> {
    T generateCode();
}
