package com.olacabs.jackhammer.db;

import com.olacabs.jackhammer.models.JiraDetail;

import com.olacabs.jackhammer.models.mapper.JiraDetailMapper;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

@RegisterMapper(JiraDetailMapper.class)
public interface JiraDetailDAO extends CrudDAO<JiraDetail>  {

    @SqlUpdate("insert into jira(host,userName,password,defaultProject) " +
            "values(:host,:userName,:password,:defaultProject)")
    @GetGeneratedKeys
    int insert(@BindBean JiraDetail jiraDetail);

    @SqlQuery("select * from jira limit 1")
    JiraDetail get();

    @SqlUpdate("update jira set host=:host,userName=:userName," +
            "password=:password,defaultProject=:defaultProject")
    void update(@BindBean JiraDetail jiraDetail);
}
