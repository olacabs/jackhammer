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

    @SqlQuery("select * from toolInstances where toolId=:toolId and isDeleted=false order by id desc")
    List<ToolInstance> getByToolId(@Bind("toolId") long toolId);

    @SqlQuery("select * from toolInstances where isDeleted=false")
    List<ToolInstance> getAll();

    @SqlQuery("select * from toolInstances where sessionId=:sessionId and isDeleted=false")
    ToolInstance getBySessionId(@Bind("sessionId") String sessionId);

    @SqlUpdate("update toolInstances set isDeleted=true where id=:id")
    void deleteById(@Bind("id") long id);

    @SqlUpdate("update toolInstances set isDeleted=true where toolId=:toolId")
    void deleteByToolId(@Bind("toolId") long toolId);

    @SqlUpdate("update toolInstances set isDeleted=true")
    void deleteAll();

    @SqlQuery("select * from toolInstances where isDeleted=false and maxAllowedScans=currentRunningScans and updatedAt > NOW() - INTERVAL 30 MINUTE and currentRunningScans <> 0 and platform <> 'Web'")
    List<ToolInstance> hangInstances();
}
