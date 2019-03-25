package com.olacabs.jackhammer.db;

import com.olacabs.jackhammer.models.Branch;
import com.olacabs.jackhammer.models.mapper.BranchMapper;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.List;

@RegisterMapper(BranchMapper.class)
public interface BranchDAO extends CrudDAO<Branch> {

    @SqlUpdate("insert into branches(name,repoId) " +
            "values(:name,:userId,:groupId,:repoId,:branchId,:scanType)")
    @GetGeneratedKeys
    int insert(@BindBean Branch branch);

    @SqlQuery("select * from branches")
    List<Branch> getAll();

    @SqlQuery("select * from branches where id=:id")
    Branch get(@Bind("id") long id);

    @SqlUpdate("delete from branches where id=:id")
    void delete(@Bind("id") long id);
}
