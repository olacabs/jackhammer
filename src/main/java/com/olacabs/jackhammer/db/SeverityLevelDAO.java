package com.olacabs.jackhammer.db;

import com.olacabs.jackhammer.models.SeverityLevel;
import com.olacabs.jackhammer.models.mapper.SeverityLevelMapper;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.List;


@RegisterMapper(SeverityLevelMapper.class)
public interface SeverityLevelDAO extends CrudDAO<SeverityLevel> {

    @SqlQuery("select * from severityLevels limit 1")
    SeverityLevel get();


    @SqlQuery("select * from severityLevels")
    List<SeverityLevel> getAll();

    @SqlUpdate("update severityLevels set enabled=:enabled where id=:id")
    void updateSeverityStatus(@BindBean SeverityLevel severityLevel);

    @SqlUpdate("update severityLevels set threshHoldCount=:threshHoldCount,mailIds=:mailIds where name=:name")
    void updateSeverityMailConfig(@BindBean SeverityLevel severityLevel);
}
