package com.olacabs.jackhammer.db;

import com.olacabs.jackhammer.models.RoleTask;
import com.olacabs.jackhammer.models.mapper.RoleTaskMapper;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.List;

@RegisterMapper(RoleTaskMapper.class)
public interface RoleTaskDAO {
    @SqlQuery("select * from rolesTasks where roleId=:roleId and isDeleted=false")
    List<RoleTask> findByRoleId(@Bind("roleId") long roleId);

    @SqlUpdate("insert into rolesTasks(roleId,taskId) values(:rt.roleId,:rt.taskId)")
    void insert(@BindBean("rt") RoleTask roleTask);

    @SqlUpdate("update rolesTasks set isDeleted=true where roleId = :rt.roleId and taskId = :rt.taskId")
    void delete(@BindBean("rt") RoleTask roleTask);
}