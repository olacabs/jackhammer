package com.olacabs.jackhammer.db;

import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.Define;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.Set;
import java.util.List;

import com.olacabs.jackhammer.models.Role;
import com.olacabs.jackhammer.models.mapper.RoleMapper;

import org.skife.jdbi.v2.sqlobject.stringtemplate.UseStringTemplate3StatementLocator;

@UseStringTemplate3StatementLocator
@RegisterMapper(RoleMapper.class)
public interface RoleDAO extends CrudDAO<Role> {

    @SqlUpdate("insert into roles(name) " +
            "values(:name)")
    @GetGeneratedKeys
    int insert(@BindBean Role role);

    @SqlQuery("select name from roles inner join rolesUsers on rolesUsers.roleId = roles.id and rolesUsers.userId = :userId")
    Set<String> getCurrentUserRoles(@Bind("userId") long userId);

    @SqlQuery("select * from roles order by <sortColumn> <order>  LIMIT :limit OFFSET :offset")
    List<Role> getAll(@BindBean Role role, @Define("sortColumn") String sortColumn, @Define("order") String order);

    @SqlQuery("select * from roles where name like concat('%', :searchTerm,'%') order by <sortColumn> <order> LIMIT :limit OFFSET :offset")
    List<Role> getSearchResults(@BindBean Role role, @Define("sortColumn") String sortColumn,@Define("order") String order);

    @SqlQuery("select count(*) from roles  where name like concat('%', :searchTerm,'%')")
    long totalSearchCount(@BindBean Role role);

    @SqlQuery("select count(*) from roles")
    long totalCount();

    @SqlQuery("select * from roles")
    List<Role>  getDropdownValues();

    @SqlQuery("select * from roles where id=:id")
    Role get(@Bind("id") long id);

    @SqlQuery("select * from roles where name=:name")
    Role findRoleByName(@Bind("name") String name);

    @SqlUpdate("update roles set name=:name where id=:id ")
    void update(@BindBean Role role);

    @SqlUpdate("delete from roles where id=:id")
    void delete(@Bind("id") long id);
}
