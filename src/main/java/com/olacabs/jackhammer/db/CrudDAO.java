package com.olacabs.jackhammer.db;

import com.olacabs.jackhammer.models.AbstractModel;

public interface CrudDAO<T extends AbstractModel> {

    T get(long id);

    T save(T instance);

    void delete(long id);
}
