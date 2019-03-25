package com.olacabs.jackhammer.db;

import com.olacabs.jackhammer.models.Group;
import com.olacabs.jackhammer.models.mapper.GroupMapper;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.Define;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import org.skife.jdbi.v2.sqlobject.stringtemplate.UseStringTemplate3StatementLocator;

import java.util.List;

@UseStringTemplate3StatementLocator
@RegisterMapper(GroupMapper.class)
public interface GroupDAO extends CrudDAO<Group> {

    @SqlUpdate("insert into groups(name) " +
            "values(:name)")
    @GetGeneratedKeys
    int insert(@BindBean Group group );

    @SqlQuery("select * from groups where name=:name and isDeleted=false")
    Group findGroupByName(@Bind("name") String name);


    @SqlQuery("select * from groups where isDeleted=false")
    List<Group> getAll();


    @SqlQuery("select * from groups where isDeleted=false order by :orderBy :sortDirection LIMIT :limit OFFSET :offset")
    List<Group> getAll(@BindBean Group group ,@Define("sortColumn") String sortColumn, @Define("order") String order);

    @SqlQuery("select * from groups where isDeleted=false and name like concat('%', :searchTerm,'%') order by <sortColumn> <order> LIMIT :limit OFFSET :offset")
    List<Group> getSearchResults(@BindBean Group group, @Define("sortColumn") String sortColumn, @Define("order") String order);

    @SqlQuery("select count(*) from groups where isDeleted=false and name like concat('%', :searchTerm,'%')")
    long totalSearchCount(@BindBean Group group);

    @SqlQuery("select * from groups where id=:id")
    Group get(@Bind("id") long id);

    @SqlQuery("select count(*) from groups where isDeleted=false")
    long totalCount();

    @SqlQuery("select * from groups where scanTypeId=:scanTypeId and isDeleted=false")
    Group getByScanTypeId(@Bind("scanTypeId") long scanTypeId);


    @SqlUpdate("update groups set name=:name where id=:id ")
    void update(@BindBean Group group);

    @SqlUpdate("update groups set isDeleted=true where id=:id")
    void delete(@Bind("id") long id);
}
