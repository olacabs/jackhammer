package com.olacabs.jackhammer.db;

import com.olacabs.jackhammer.models.Tool;
import com.olacabs.jackhammer.models.mapper.ToolMapper;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.Define;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import org.skife.jdbi.v2.sqlobject.stringtemplate.UseStringTemplate3StatementLocator;

import java.util.List;

@UseStringTemplate3StatementLocator
@RegisterMapper(ToolMapper.class)
public interface ToolDAO {

    @SqlUpdate("insert into tools(id,name,languageId,scanTypeId,manifestJson,status) values(:id, :name,:languageId,:scanTypeId,:manifestJson,:status)")
    @GetGeneratedKeys
    long insert(@BindBean Tool tool);

    @SqlQuery("select * from tools where isDeleted=false order by <sortColumn> <order>  LIMIT :limit OFFSET :offset")
    List<Tool> getAll(@BindBean Tool tool, @Define("sortColumn") String sortColumn, @Define("order") String order);

    @SqlQuery("select * from tools where isDeleted=false")
    List<Tool> getAll();

    @SqlQuery("select * from tools where isDeleted=false and name like concat('%', :searchTerm,'%') order by <sortColumn> <order> LIMIT :limit OFFSET :offset")
    List<Tool> getSearchResults(@BindBean Tool tool, @Define("sortColumn") String sortColumn,@Define("order") String order);

    @SqlQuery("select count(*) from tools where isDeleted=false and name like concat('%', :searchTerm,'%')")
    long totalSearchCount(@BindBean Tool tool);

    @SqlQuery("select count(*) from tools where isDeleted=false")
    long totalCount(@BindBean Tool tool);

    @SqlQuery("select * from tools where id=:id")
    Tool get(@Bind("id") long id);

    @SqlQuery("select * from tools where name=:name and isDeleted=false")
    Tool update(@BindBean Tool tool);

    @SqlQuery("select * from tools where name=:name and isDeleted=false")
    Tool findToolByName(@Bind("name") String name);

    @SqlQuery("select * from tools where languageId=:languageId and isDeleted=false")
    List<Tool> findByLanguageId(@Bind("languageId") long languageId);

    @SqlQuery("select * from tools where scanTypeId=:scanTypeId and isDeleted=false")
    List<Tool> findByScanTypeId(@Bind("scanTypeId") long scanTypeId);

    @SqlUpdate("update tools set status=:status where id=:id")
    void updateStatus(@BindBean Tool tool);

    @SqlUpdate("update tools set isDeleted=true where id=:id")
    void delete(@Bind("id") long id);
}
