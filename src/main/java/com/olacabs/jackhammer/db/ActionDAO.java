package com.olacabs.jackhammer.db;

import com.olacabs.jackhammer.models.Action;
import com.olacabs.jackhammer.models.mapper.ActionMapper;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.List;

@RegisterMapper(ActionMapper.class)
public interface ActionDAO extends CrudDAO<Action> {
    @SqlQuery("select * from actions")
    List<Action> getAll();

    @SqlQuery("select * from actions where id=:id")
    Action get(@Bind("id") long id);
}
