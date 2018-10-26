package com.olacabs.jackhammer.service;

import com.google.common.collect.Iterables;

import java.util.Collection;

import static java.util.Arrays.asList;

public class SqlArray<T> {
    private final Object[] elements;
    private final Class<T> type;

    private SqlArray(Class<T> type, Collection<T> elements) {
        this.elements = Iterables.toArray(elements, Object.class);
        this.type = type;
    }

    @SafeVarargs
    static <T> SqlArray<T> arrayOf(Class<T> type, T... elements) {
        return new SqlArray(type, asList(elements));
    }

    Object[] getElements() {
        return elements;
    }

    Class<T> getType() {
        return type;
    }
}
