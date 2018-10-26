package com.olacabs.jackhammer.db;

import com.olacabs.jackhammer.models.DefaultRole;
import com.olacabs.jackhammer.models.mapper.DefaultRoleMapper;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;


@RegisterMapper(DefaultRoleMapper.class)
public interface DefaultRoleDAO extends CrudDAO<DefaultRole> {

    @SqlUpdate("insert into defaultRole(roleId) " +
            "values(:roleId)")
    @GetGeneratedKeys
    int insert(@BindBean DefaultRole defaultRole);

    @SqlQuery("select * from defaultRole limit 1")
    DefaultRole get();

    @SqlUpdate("update defaultRole set roleId=:roleId")
    void update(@BindBean DefaultRole defaultRole);
}
