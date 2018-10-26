package com.olacabs.jackhammer.db;

import com.olacabs.jackhammer.models.SMTPDetail;
import com.olacabs.jackhammer.models.mapper.SMTPDetailMapper;

import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;


@RegisterMapper(SMTPDetailMapper.class)
public interface SMTPDetailDAO extends CrudDAO<SMTPDetail> {

    @SqlUpdate("insert into smtp(applicationUrl,smtpHost,smtpUserName,smtpPassword,smtpPort) " +
            "values(:applicationUrl,:smtpHost,:smtpUserName,:smtpPassword,:smtpPort)")
    @GetGeneratedKeys
    int insert(@BindBean SMTPDetail smtpDetail);

    @SqlQuery("select * from smtp limit 1")
    SMTPDetail get();

    @SqlUpdate("update smtp set applicationUrl=:applicationUrl,smtpHost=:smtpHost," +
            "smtpUserName=:smtpUserName,smtpPassword=:smtpPassword,smtpPort=:smtpPort")
    void update(@BindBean SMTPDetail smtpDetail);
}
