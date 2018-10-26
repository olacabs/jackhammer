package com.olacabs.jackhammer.db;

import com.olacabs.jackhammer.models.Git;
import com.olacabs.jackhammer.models.mapper.GitMapper;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;


@RegisterMapper(GitMapper.class)
public interface GitDAO extends CrudDAO<Git> {

    @SqlUpdate("insert into git(userName,gitEndPoint,apiAccessToken,gitType,organizationName) " +
            "values(:userName,:gitEndPoint,:apiAccessToken,:gitType,:organizationName)")
    @GetGeneratedKeys
    int insert(@BindBean Git git);

    @SqlQuery("select * from git limit 1")
    Git get();

    @SqlUpdate("update git set gitType=:gitType,organizationName=:organizationName," +
            "userName=:userName,gitEndPoint=:gitEndPoint,apiAccessToken=:apiAccessToken")
    void update(@BindBean Git git);
}
