package com.olacabs.jackhammer.db;

import com.olacabs.jackhammer.models.ToolInstance;
import com.olacabs.jackhammer.models.mapper.ToolInstanceMapper;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.List;

@RegisterMapper(ToolInstanceMapper.class)
public interface ToolInstanceDAO extends CrudDAO<ToolInstance> {

    @SqlUpdate("insert into toolInstances(toolId,sessionId,platform,status,maxAllowedScans) " +
            "values(:toolId,:sessionId,:platform,:status,:maxAllowedScans)")
    @GetGeneratedKeys
    int insert(@BindBean ToolInstance toolInstance);

    @SqlQuery("select * from toolInstances where id=:id")
    ToolInstance get(@Bind("id") long id);

    @SqlUpdate("update toolInstances set currentRunningScans = currentRunningScans + 1 where id=:id")
    void increaseRunningScans(@Bind("id") long id);

    @SqlUpdate("update toolInstances set currentRunningScans = currentRunningScans - 1 where id=:id")
    void decreaseRunningScans(@Bind("id") long id);

    @SqlQuery("select * from toolInstances where toolId=:toolId order by id desc")
    List<ToolInstance> getByToolId(@Bind("toolId") long toolId);

    @SqlQuery("select * from toolInstances")
    List<ToolInstance> getAll();

    @SqlQuery("select * from toolInstances where sessionId=:sessionId")
    ToolInstance getBySessionId(@Bind("sessionId") String sessionId);

    @SqlUpdate("delete from toolInstances where id=:id")
    void deleteById(@Bind("id") long id);

    @SqlUpdate("delete from toolInstances where toolId=:toolId")
    void deleteByToolId(@Bind("toolId") long toolId);

    @SqlUpdate("delete from toolInstances")
    void deleteAll();
}
