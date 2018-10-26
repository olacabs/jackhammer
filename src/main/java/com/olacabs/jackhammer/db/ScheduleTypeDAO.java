package com.olacabs.jackhammer.db;

import com.olacabs.jackhammer.models.ScheduleType;
import com.olacabs.jackhammer.models.mapper.ScheduleTypeMapper;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.List;


@RegisterMapper(ScheduleTypeMapper.class)
public interface ScheduleTypeDAO extends CrudDAO<ScheduleType> {

    @SqlUpdate("insert into scheduleTypes(id,name,days) " +
            "values(:id, :name,:days)")
    @GetGeneratedKeys
    int insert(@BindBean ScheduleType scheduleType);

    @SqlQuery("select * from scheduleTypes")
    List<ScheduleType> getAll();

    @SqlQuery("select * from scheduleTypes where name=:name")
    ScheduleType findScheduleByName(@Bind("name") String name);

    @SqlUpdate("delete from scheduleTypes where id=:id")
    void update(@BindBean  ScheduleType scheduleType);

    @SqlUpdate("delete from scheduleTypes where id=:id")
    void delete(@Bind("id") long id);

    @SqlQuery("select * from scheduleTypes where id=:id")
    ScheduleType get(@Bind("id") long id);
}
