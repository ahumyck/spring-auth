package com.auth.framework.core.attribute;


import com.auth.framework.exceptions.InvalidArgumentException;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class PredicateType {

    public static final PredicateType ANY = new PredicateType(0);
    public static final PredicateType ALL = new PredicateType(1);

    private final Integer value;

    private static final AtomicInteger counter = new AtomicInteger(Integer.MIN_VALUE);

    public static PredicateType createNewValue() {
        return new PredicateType(counter.getAndIncrement());
    }

    protected PredicateType(Integer value) throws InvalidArgumentException {
        this.value = value;
    }

    public Integer getValue() {
        return this.value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PredicateType)) return false;
        PredicateType that = (PredicateType) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
