package com.olacabs.jackhammer.db;

import com.olacabs.jackhammer.models.RoleUser;
import com.olacabs.jackhammer.models.mapper.RoleUserMapper;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.List;

@RegisterMapper(RoleUserMapper.class)
public interface RoleUserDAO {

    @SqlUpdate("insert into rolesUsers(roleId,userId) values(:roleId,:userId)")
    int insert(@BindBean RoleUser roleUser);

    @SqlQuery("select * from rolesUsers where userId=:userId")
    List<RoleUser> findByUserId(@Bind("userId") long userId);

    @SqlUpdate("delete from rolesUsers where roleId = :ru.roleId and userId = :ru.userId")
    void delete(@BindBean("ru") RoleUser roleUser);
}
