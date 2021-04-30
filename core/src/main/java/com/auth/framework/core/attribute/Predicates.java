package com.auth.framework.core.attribute;

import java.util.*;
import java.util.function.Predicate;

public class Predicates<T> {

    private final Map<PredicateType, List<Predicate<T>>> predicatesByType = new HashMap<>();

    public void add(PredicateType type, Predicate<T>... predicatesArgs) {
        List<Predicate<T>> predicates = predicatesByType.computeIfAbsent(type, k -> new ArrayList<>(predicatesArgs.length));
        predicates.addAll(Arrays.asList(predicatesArgs));
        predicatesByType.put(type, predicates);
    }

    /**
     * At least ONE of ANY predicates has to be true, otherwise false will be returned
     *
     * @param object объект на котором проверяются предикаты
     * @return результат применения предикатов
     */
    public boolean apply(T object) {
        List<Predicate<T>> predicatesApplyAll = predicatesByType.get(PredicateType.ALL);
        if (predicatesApplyAll != null && !predicatesApplyAll.isEmpty()) {
            for (Predicate<T> predicate : predicatesApplyAll)
                if (!predicate.test(object)) return false;
        }

        //if we are here it means all predicates were applied of there were none of them

        List<Predicate<T>> predicatesApplyAny = predicatesByType.get(PredicateType.ANY);
        if (predicatesApplyAny != null && !predicatesApplyAny.isEmpty()) {
            for (Predicate<T> predicate : predicatesApplyAny)
                if (predicate.test(object)) return true;
            return false; //at lease one of any predicates has to be applied, otherwise return false
        }

        return true;
    }

    @Override
    public String toString() {
        return "Predicates{" +
                "predicatesByType=" + predicatesByType +
                '}';
    }
}
