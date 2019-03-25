package com.olacabs.jackhammer.db;

import com.olacabs.jackhammer.models.GroupUser;
import com.olacabs.jackhammer.models.mapper.GroupUserMapper;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.List;


@RegisterMapper(GroupUserMapper.class)
public interface GroupUserDAO {

    @SqlUpdate("insert into groupsUsers (groupId, userId) values (:gu.groupId, :gu.userId)")
    long insert(@BindBean("gu") GroupUser groupUser);

    @SqlQuery("select * from groupsUsers where userId=:userId and isDeleted=false")
    List<GroupUser> findByUserId(@Bind("userId") long userId);

    @SqlUpdate("update groupsUsers set isDeleted=true where groupId = :gu.groupId and userId = :gu.userId")
    void delete(@BindBean("gu") GroupUser groupUser);
}
