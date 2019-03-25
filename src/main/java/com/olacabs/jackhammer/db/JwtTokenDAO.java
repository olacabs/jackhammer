package com.olacabs.jackhammer.db;

import com.olacabs.jackhammer.models.JwtToken;

import com.olacabs.jackhammer.models.mapper.JwtTokenMapper;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;


@RegisterMapper(JwtTokenMapper.class)
public interface JwtTokenDAO extends CrudDAO<JwtToken> {

    @SqlUpdate("insert into jwtTokens (id,tokenValidFrom,tokenValidUntil,userId,createdAt,updatedAt,deleted,version) " +
            "values (:id, :tokenValidFrom,:tokenValidUntil,:userId,:createdAt,:updatedAt,:deleted,:version)")
    @GetGeneratedKeys
    int insert(@BindBean JwtToken jwtToken);

    @SqlQuery("select * from jwttokens where id=:id")
    JwtToken get(@Bind("id") long id);

    @SqlUpdate("update jwtTokens set deleted=true where userId=:userId")
    void delete(@Bind("userId") long userId);

}
