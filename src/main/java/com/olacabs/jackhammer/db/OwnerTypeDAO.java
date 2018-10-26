package com.olacabs.jackhammer.db;

import com.olacabs.jackhammer.models.OwnerType;
import com.olacabs.jackhammer.models.mapper.OwnerTypeMapper;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.List;

@RegisterMapper(OwnerTypeMapper.class)
public interface OwnerTypeDAO  extends CrudDAO<OwnerType> {
    @SqlQuery("select * from ownerTypes")
    List<OwnerType> getAll();

    @SqlQuery("select * from ownerTypes where id=:id")
    OwnerType get(@Bind("id") long id);


    @SqlQuery("select * from ownerTypes where name=:name")
    OwnerType getByName(@Bind("name") String name);

    @SqlQuery("select * from ownerTypes where isDefault=true")
    OwnerType getDefaultOwnerType();


}
