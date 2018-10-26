package com.olacabs.jackhammer.db;

import com.olacabs.jackhammer.models.Application;
import com.olacabs.jackhammer.models.Repo;
import com.olacabs.jackhammer.models.mapper.ApplicationMapper;
import com.olacabs.jackhammer.models.mapper.RepoMapper;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.Define;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.List;

import org.skife.jdbi.v2.sqlobject.stringtemplate.UseStringTemplate3StatementLocator;
import org.skife.jdbi.v2.unstable.BindIn;

@UseStringTemplate3StatementLocator
@RegisterMapper(RepoMapper.class)
public interface RepoDAO extends CrudDAO<Repo> {

    @SqlUpdate("insert into repos(name,userId,groupId,branchId,ownerTypeId,scanTypeId,target) " +
            "values(:name,:userId,:groupId,:branchId,:ownerTypeId,:scanTypeId,:target)")
    @GetGeneratedKeys
    int insert(@BindBean Repo repo);

    @SqlQuery("select groupId from repos where id=:id")
    int getGroupId(@Bind("id") long id);

    @SqlQuery("select * from repos")
    List<Repo> getAll();

    @SqlQuery("select * from repos order by :orderBy :sortDirection LIMIT :limit OFFSET :offset")
    List<Repo> getAll(@BindBean Repo repo);

    @SqlQuery("select count(*) from repos")
    long totalCount();

    @SqlQuery("select count(*) from repos where groupId=:groupId and scanTypeId=:scanTypeId and ownerTypeId=:ownerTypeId")
    long groupRepoCount(@BindBean Repo repo);

    @SqlQuery("select count(*) from repos where userId=:userId and scanTypeId=:scanTypeId and ownerTypeId=:ownerTypeId")
    long getPersonalRepoCount(@BindBean Repo repo);


    @SqlQuery("select * from repos where name=:name")
    Repo findRepoByName(@Bind("name") String name);

    @SqlQuery("select * from repos where id=:id")
    Repo findRepoById(@Bind("id") long id);

    @SqlQuery("select * from repos where groupId in (<groupIds>)")
    List<Repo> getRepos(@BindIn("groupIds") List<Long> groupIds);

    @SqlUpdate("update repos set name=:name where id=:id ")
    void update(@BindBean Repo repo);

    @SqlUpdate("delete from repos where id=:id")
    void delete(@Bind("id") long id);

    @SqlQuery("select * from repos where userId=:userId and scanTypeId=:scanTypeId and ownerTypeId=:ownerTypeId and target=:target")
    Repo getRepoByTarget(@BindBean Repo repo);
    //group repos


    @SqlQuery("select rId as id,name,sum(case when severity = 'Critical'" +
            " or severity = 'High'" +
            " or severity = 'Medium'" +
            " or severity = 'Low'" +
            " or severity = 'Info' then count else 0 end) as totalCount," +
            "sum(case when severity='Critical' then count else 0 end ) as criticalCount," +
            "sum(case when severity='High' then count else 0 end ) as highCount," +
            "sum(case when severity='Medium' then count else 0 end ) as mediumCount," +
            "sum(case when severity='Low' then count else 0 end ) as lowCount," +
            "sum(case when severity='Info' then count else 0 end ) as infoCount " +
            "from " +
            "(select r.id as rId ,r.scanTypeId as scanTypeId,r.ownerTypeId as ownerTypeId ,r.groupId as grpid,r.name,f.severity,count(*) as count from repos r " +
            "left outer join findings f on r.id=f.repoId " +
            "and f.status not in('Closed') " +
            " and f.isFalsePositive=false " +
            " and f.notExploitable=false " +
            "and f.ownerTypeId=:ownerTypeId " +
            "and f.scanTypeId=:scanTypeId " +
            "group by r.name,rId,r.groupId,f.severity) f " +
            "where grpid=:groupId and ownerTypeId=:ownerTypeId and scanTypeId=:scanTypeId " +
            "group by name,id " +
            " order by <sortColumn> <order> LIMIT :limit OFFSET :offset")
    @RegisterMapper(ApplicationMapper.class)
    List<Application> getApplicationRepos(@BindBean Repo repo, @Define("sortColumn") String sortColumn, @Define("order") String order);

    @SqlQuery("select rId as id,name,sum(case when severity = 'Critical'" +
            " or severity = 'High'" +
            " or severity = 'Medium'" +
            " or severity = 'Low'" +
            " or severity = 'Info' then count else 0 end) as totalCount," +
            "sum(case when severity='Critical' then count else 0 end ) as criticalCount," +
            "sum(case when severity='High' then count else 0 end ) as highCount," +
            "sum(case when severity='Medium' then count else 0 end ) as mediumCount," +
            "sum(case when severity='Low' then count else 0 end ) as lowCount," +
            "sum(case when severity='Info' then count else 0 end ) as infoCount " +
            "from " +
            "(select r.id as rId ,r.scanTypeId as scanTypeId,r.ownerTypeId as ownerTypeId,r.groupId as grpid,r.name,f.severity,count(*) as count from repos r " +
            "left outer join findings f on r.id=f.repoId " +
            "and f.status not in('Closed') " +
            " and f.isFalsePositive=false " +
            " and f.notExploitable=false " +
            "and f.ownerTypeId=:ownerTypeId " +
            "and f.scanTypeId=:scanTypeId " +
            "group by r.name,rId,r.groupId,f.severity) f " +
            "where grpid=:groupId and name like concat('%', :searchTerm,'%') and ownerTypeId=:ownerTypeId and scanTypeId=:scanTypeId " +
            "group by name,id " +
            " order by <sortColumn> <order> LIMIT :limit OFFSET :offset")
    @RegisterMapper(ApplicationMapper.class)
    List<Application> getReposSearchResult(@BindBean Repo repo, @Define("sortColumn") String sortColumn, @Define("order") String order);

    @SqlQuery("select count(*) from repos where name like concat('%', :searchTerm,'%') where groupId=:groupId and ownerTypeId=:ownerTypeId")
    long totalSearchCount(@BindBean Repo repo);


    @SqlQuery("select rId as id,name,sum(case when severity = 'Critical'" +
            " or severity = 'High'" +
            " or severity = 'Medium'" +
            " or severity = 'Low'" +
            " or severity = 'Info' then count else 0 end) as totalCount," +
            "sum(case when severity='Critical' then count else 0 end ) as criticalCount," +
            "sum(case when severity='High' then count else 0 end ) as highCount," +
            "sum(case when severity='Medium' then count else 0 end ) as mediumCount," +
            "sum(case when severity='Low' then count else 0 end ) as lowCount," +
            "sum(case when severity='Info' then count else 0 end ) as infoCount " +
            "from " +
            "(select r.id as rId ,r.ownerTypeId as ownerTypeId,r.scanTypeId as scanTypeId,r.name,r.userId as userId,f.severity,count(*) as count from repos r " +
            "left outer join findings f on r.id=f.repoId " +
            "and f.status not in('Closed') " +
            " and f.isFalsePositive=false " +
            " and f.notExploitable=false " +
            "and f.ownerTypeId=:ownerTypeId " +
            "and f.scanTypeId=:scanTypeId " +
            "and r.userId=:userId " +
            "group by r.name,rId,f.severity) f " +
            "where userId=:userId and scanTypeId=:scanTypeId and ownerTypeId=:ownerTypeId " +
            "group by name,id " +
            " order by <sortColumn> <order> LIMIT :limit OFFSET :offset")
    @RegisterMapper(ApplicationMapper.class)
    List<Application> getPersonalRepos(@BindBean Repo repo, @Define("sortColumn") String sortColumn, @Define("order") String order);


    @SqlQuery("select rId as id,name,sum(case when severity = 'Critical'" +
            " or severity = 'High'" +
            " or severity = 'Medium'" +
            " or severity = 'Low'" +
            " or severity = 'Info' then count else 0 end) as totalCount," +
            "sum(case when severity='Critical' then count else 0 end ) as criticalCount," +
            "sum(case when severity='High' then count else 0 end ) as highCount," +
            "sum(case when severity='Medium' then count else 0 end ) as mediumCount," +
            "sum(case when severity='Low' then count else 0 end ) as lowCount," +
            "sum(case when severity='Info' then count else 0 end ) as infoCount " +
            "from " +
            "(select r.id as rId ,r.ownerTypeId as ownerTypeId,r.scanTypeId as scanTypeId,r.name,r.userId as userId,f.severity,count(*) as count from repos r " +
            "left outer join findings f on r.id=f.repoId " +
            "and f.status not in('Closed') " +
            " and f.isFalsePositive=false " +
            " and f.notExploitable=false " +
            "and f.ownerTypeId=:ownerTypeId " +
            "and f.scanTypeId=:scanTypeId " +
            "and r.userId=:userId " +
            "group by r.name,rId,f.severity) f " +
            "where  userId=:userId and name like concat('%', :searchTerm,'%') and scanTypeId=:scanTypeId and ownerTypeId=:ownerTypeId " +
            "group by name,id " +
            " order by <sortColumn> <order> LIMIT :limit OFFSET :offset")
    @RegisterMapper(ApplicationMapper.class)
    List<Application> getPersonalRepoSearchResult(@BindBean Repo repo, @Define("sortColumn") String sortColumn, @Define("order") String order);

    @SqlQuery("select count(*) from repos where name like concat('%', :searchTerm,'%') " +
            " where userId=:userId and scanTypeId=:scanTypeId and ownerTypeId=:ownerTypeId")
    long totalPersonalRepoSearchCount(@BindBean Repo repo);

}
