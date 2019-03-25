package com.olacabs.jackhammer.db;

import com.olacabs.jackhammer.models.FindingTag;
import com.olacabs.jackhammer.models.mapper.FindingTagMapper;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.List;


@RegisterMapper(FindingTagMapper.class)
public interface FindingTagDAO {

    @SqlUpdate("insert into findingsTags (findingId, tagId) values (:ft.findingId, :ft.tagId)")
    long insert(@BindBean("ft") FindingTag findingTag);

    @SqlQuery("select * from findingsTags where findingId=:findingId and isDeleted=false")
    List<FindingTag> findByFindingId(@Bind("findingId") long findingId);

    @SqlUpdate("update findingsTags set isDeleted=true where findingId = :ft.findingId and tagId = :ft.tagId")
    void delete(@BindBean("ft") FindingTag findingTag);

    @SqlUpdate("update findingsTags set isDeleted=true where findingId= :findingId")
    void deleteByFindingId(@Bind("findingId") long findingId);
}
