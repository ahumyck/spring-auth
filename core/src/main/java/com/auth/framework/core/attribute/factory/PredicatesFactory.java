package com.auth.framework.core.attribute.factory;

import com.auth.framework.core.attribute.Predicates;

public interface PredicatesFactory<T> {

    Predicates<T> create();
}
