package com.olacabs.jackhammer.db;

import com.olacabs.jackhammer.models.Permission;
import com.olacabs.jackhammer.models.mapper.PermissionMapper;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.Define;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import org.skife.jdbi.v2.sqlobject.stringtemplate.UseStringTemplate3StatementLocator;

import java.util.List;

@UseStringTemplate3StatementLocator
@RegisterMapper(PermissionMapper.class)
public interface PermissionDAO {

    @SqlQuery("select * from permissions  order by <sortColumn> <order>  LIMIT :limit OFFSET :offset")
    List<Permission> getAll(@BindBean Permission permission,@Define("sortColumn") String sortColumn,@Define("order") String order);

    @SqlQuery("select * from permissions where name like concat('%', :searchTerm,'%') order by <sortColumn> <order> LIMIT :limit OFFSET :offset")
    List<Permission> getSearchResults(@BindBean Permission permission, @Define("sortColumn") String sortColumn,@Define("order") String order);

    @SqlQuery("select count(*) from permissions where name like concat('%', :searchTerm,'%')")
    long totalSearchCount(@BindBean Permission permission);

    @SqlUpdate("insert into permissions(id,name) values(:id, :name)")
    @GetGeneratedKeys
    int insert(@BindBean Permission permission);

    @SqlQuery("select * from permissions where id=:permissionId")
    Permission get(@Bind("permissionId") long permissionId);

    @SqlUpdate("update permissions set name=:name where id=:id")
    int update(@BindBean Permission permission );

    @SqlQuery("select * from permissions where name=:permissionName")
    Permission findPermissionByName(@Bind("permissionName") String permissionName);

    @SqlQuery("select count(*) from permissions")
    long totalCount();

    @SqlQuery("select * from permissions")
    List<Permission>  getDropdownValues();

    @SqlUpdate("delete from permissions where id=:id")
    void delete(@Bind("id") long id);
}
