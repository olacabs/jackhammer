package com.olacabs.jackhammer.db;

import com.olacabs.jackhammer.models.Comment;
import com.olacabs.jackhammer.models.mapper.CommentMapper;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.Define;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import org.skife.jdbi.v2.sqlobject.stringtemplate.UseStringTemplate3StatementLocator;

import java.util.List;
import java.util.Set;

@UseStringTemplate3StatementLocator
@RegisterMapper(CommentMapper.class)
public interface CommentDAO extends CrudDAO<Comment> {

    @SqlUpdate("insert into comments(name,findingId,userId) " +
            "values(:name,:findingId,:userId)")
    @GetGeneratedKeys
    int insert(@BindBean Comment comment);

    @SqlQuery("select * from comments where findingId=:findingId and isDeleted=false order by createdAt")
    List<Comment> getAll(@BindBean Comment comment);

    @SqlQuery("select * from comments where name like concat('%', :searchTerm,'%') order by <sortColumn> <order> LIMIT :limit OFFSET :offset")
    List<Comment> getSearchResults(@BindBean Comment comment, @Define("sortColumn") String sortColumn, @Define("order") String order);

    @SqlQuery("select count(*) from comments  where name like concat('%', :searchTerm,'%')")
    long totalSearchCount(@BindBean Comment comment);

    @SqlQuery("select count(*) from comments")
    long totalCount();

    @SqlQuery("select * from comments where id=:id")
    Comment get(@Bind("id") long id);

    @SqlUpdate("update comments set name=:name where id=:id ")
    void update(@BindBean Comment comment);

    @SqlUpdate("delete from comments where id=:id")
    void delete(@Bind("id") long id);

    @SqlUpdate("update comments set isDeleted=true where findingId=:findingId")
    void deleteFindingComments(@Bind("findingId") long findingId);
}
