package com.olacabs.jackhammer.db;

import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.Define;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import com.olacabs.jackhammer.models.User;
import com.olacabs.jackhammer.models.mapper.UserMapper;
import org.skife.jdbi.v2.sqlobject.stringtemplate.UseStringTemplate3StatementLocator;

import java.util.List;

@UseStringTemplate3StatementLocator
@RegisterMapper(UserMapper.class)
public interface UserDAO extends CrudDAO<User>  {

    @SqlUpdate("insert into users (name,email,password) " +
            "values (:name,:email,:password)")
    @GetGeneratedKeys
    int insert(@BindBean User user);

    @SqlQuery("select * from users where email=:email and isDeleted=false")
    User findByEmail(@Bind("email") String email);

    @SqlQuery("select * from users where id=:id")
    User get(@Bind("id") long id);

    @SqlUpdate("update users set name=:name where id=:id ")
    void update(@BindBean User user);

    @SqlUpdate("update users set password=:newPassword where id=:id ")
    void updatePassword(@BindBean User user);

    @SqlUpdate("update users set resetPasswordToken=:resetPasswordToken,resetPasswordSentAt=:resetPasswordSentAt where id=:id ")
    void updateResetPasswordToken(@BindBean User user);

    @SqlQuery("select count(*) from users where isDeleted=false")
    long totalCount();

    @SqlQuery("select * from users where isDeleted=false order by <sortColumn> <order>  LIMIT :limit OFFSET :offset")
    List<User> getAll(@BindBean User user,@Define("sortColumn") String sortColumn,@Define("order") String order);

    @SqlQuery("select * from users where isDeleted=false and email like concat('%', :searchTerm,'%') order by <sortColumn> <order> LIMIT :limit OFFSET :offset")
    List<User> getSearchResults(@BindBean User user, @Define("sortColumn") String sortColumn,@Define("order") String order);

    @SqlQuery("select count(*) from users where isDeleted=false and email like concat('%', :searchTerm,'%')")
    long totalSearchCount(@BindBean User user);

    @SqlUpdate("update users set isDeleted=true where id=:id")
    void delete(@Bind("id") long id);
}
