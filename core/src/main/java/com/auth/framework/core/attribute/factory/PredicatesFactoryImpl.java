package com.auth.framework.core.attribute.factory;

import com.auth.framework.core.attribute.Predicates;
import com.auth.framework.core.attribute.PredicatesImpl;

public class PredicatesFactoryImpl<T> implements PredicatesFactory<T> {
    @Override
    public Predicates<T> create() {
        return new PredicatesImpl<>();
    }
}
