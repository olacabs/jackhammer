package com.olacabs.jackhammer.db;

import com.olacabs.jackhammer.models.GroupRole;
import com.olacabs.jackhammer.models.mapper.GroupRoleMapper;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

import java.util.List;

import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

@RegisterMapper(GroupRoleMapper.class)
public interface GroupRoleDAO {

    @SqlUpdate("insert into groupsRoles (groupId, roleId) values (:rp.groupId, :rp.roleId)")
    long insert(@BindBean("rp") GroupRole groupRole);

    @SqlQuery("select * from groupsRoles where groupId=:groupId")
    List<GroupRole> findByGroupId(@Bind("groupId") long groupId);

    @SqlUpdate("delete from groupsRoles where roleId = :g.roleId and groupId = :g.groupId")
    void delete(@BindBean("g") GroupRole groupRole);
}
