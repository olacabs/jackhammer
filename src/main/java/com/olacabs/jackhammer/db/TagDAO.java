package com.olacabs.jackhammer.db;

import com.olacabs.jackhammer.models.Tag;
import com.olacabs.jackhammer.models.mapper.TagMapper;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import org.skife.jdbi.v2.sqlobject.stringtemplate.UseStringTemplate3StatementLocator;

import java.util.List;

@UseStringTemplate3StatementLocator
@RegisterMapper(TagMapper.class)
public interface TagDAO extends CrudDAO<Tag> {

    @SqlUpdate("insert into tags(name,userId) " +
            "values(:name,:userId)")
    @GetGeneratedKeys
    int insert(@BindBean Tag tag);

    @SqlQuery("select * from tags order by createdAt")
    List<Tag> getAll();

    @SqlQuery("select * from tags order by createdAt")
    List<Tag> getFindingTags();

    @SqlQuery("select * from tags where id=:id")
    Tag get(@Bind("id") long id);

    @SqlQuery("select * from tags where name=:name")
    Tag findTagByName(@Bind("name") String name);

    @SqlUpdate("update roles set name=:name where id=:id ")
    void update(@BindBean Tag tag);

    @SqlUpdate("delete from roles where id=:id")
    void delete(@Bind("id") long id);
}
