package com.auth.framework.core.attribute;

import java.util.function.Predicate;

public interface Predicates<T> extends Predicate<T> {

    void add(PredicateType type, Predicate<T>... predicatesArgs);
}
