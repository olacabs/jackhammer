package com.olacabs.jackhammer.db;

import com.olacabs.jackhammer.models.PermissionRole;
import com.olacabs.jackhammer.models.mapper.RolePermissionMapper;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.List;


@RegisterMapper(RolePermissionMapper.class)
public interface PermissionRoleDAO {

    @SqlUpdate("insert into permissionsRoles (roleId, permissionId) values (:rp.roleId, :rp.permissionId)")
    long insert(@BindBean("rp") PermissionRole rolePermission);

    @SqlQuery("select * from permissionsRoles where roleId=:roleId")
    List<PermissionRole> findByRoleId(@Bind("roleId") long roleId);

    @SqlUpdate("delete from permissionsRoles where roleId = :rp.roleId and permissionId = :rp.permissionId")
    void delete(@BindBean("rp") PermissionRole rolePermission);

}
