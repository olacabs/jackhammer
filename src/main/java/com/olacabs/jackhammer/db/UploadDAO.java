package com.olacabs.jackhammer.db;

import com.olacabs.jackhammer.models.Finding;
import com.olacabs.jackhammer.models.Upload;
import com.olacabs.jackhammer.models.mapper.UploadMapper;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.List;

@RegisterMapper(UploadMapper.class)
public interface UploadDAO extends CrudDAO<Upload> {

    @SqlUpdate("insert into uploads(name,userId,findingId) " +
            "values(:name,:userId,:findingId)")
    int insert(@BindBean Upload upload);

    @SqlQuery("select * from uploads where id=:id")
    Upload get(@Bind("id") long id);

    @SqlQuery("select * from uploads where findingId=:findingId")
    List<Upload> getAll(@BindBean Upload upload);

    @SqlUpdate("update uploads set id=:id where id=:id")
    void update(@BindBean Finding finding);


    @SqlUpdate("delete from uploads where findingId=:findingId")
    void deleteFindingUploads(@Bind("findingId") long findingId);
}
