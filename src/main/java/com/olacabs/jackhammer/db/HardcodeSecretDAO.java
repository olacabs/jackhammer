package com.olacabs.jackhammer.db;

import com.olacabs.jackhammer.models.HardcodeSecret;
import com.olacabs.jackhammer.models.mapper.HardcodeSecretMapper;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;


@RegisterMapper(HardcodeSecretMapper.class)
public interface HardcodeSecretDAO extends CrudDAO<HardcodeSecret> {

    @SqlUpdate("insert into hardcodeSecrets(commitsDepth,commitsStartDate,regex) " +
            "values(:commitsDepth,:commitsStartDate,:regex)")
    @GetGeneratedKeys
    int insert(@BindBean HardcodeSecret hardcodeSecret);

    @SqlQuery("select * from hardcodeSecrets limit 1")
    HardcodeSecret get();

    @SqlUpdate("update hardcodeSecrets set commitsDepth=:commitsDepth,commitsStartDate=:commitsStartDate,regex=:regex")
    void update(@BindBean HardcodeSecret hardcodeSecret);
}
