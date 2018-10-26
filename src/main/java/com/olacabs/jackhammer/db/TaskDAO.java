package com.olacabs.jackhammer.db;

import com.olacabs.jackhammer.models.Task;
import com.olacabs.jackhammer.models.mapper.TaskMapper;
import org.apache.commons.lang3.StringUtils;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.List;
import java.util.Set;

@RegisterMapper(TaskMapper.class)
public interface TaskDAO extends CrudDAO<Task> {
    @SqlQuery("select * from tasks where actionId=:actionId and parentId is null and hideFromUi=false order by id")
    List<Task> getParentTasks(@Bind("actionId") long actionId);

    @SqlQuery("select * from tasks where actionId=:actionId and parentId is null")
    List<Task> getRolesPageParentTasks(@Bind("actionId") long actionId);

    @SqlQuery("select * from tasks where parentId=:parentId")
    List<Task> getChildTasks(@Bind("parentId") long parentId);

    @SqlQuery("select * from tasks where id=:parentId")
    Task getParentTask(@Bind("parentId") long parentId);

    @SqlQuery("select * from tasks where id=:id")
    Task getTask(@Bind("id") long id);

    @SqlQuery("select * from tasks")
    List<Task> getAll();

    @SqlQuery("select * from tasks where accessToAll=true")
    List<Task> defaultTasks();

    @SqlQuery("select * from tasks where ownerTypeId=:ownerTypeId and name like concat('%', :name,'%')")
    Task getCurrentTask(@Bind("name") String name,@Bind("ownerTypeId") long ownerTypeId);
}
