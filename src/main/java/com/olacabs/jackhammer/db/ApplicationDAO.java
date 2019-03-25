package com.olacabs.jackhammer.db;

import com.olacabs.jackhammer.models.Application;
import com.olacabs.jackhammer.models.Group;
import com.olacabs.jackhammer.models.mapper.ApplicationMapper;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.Define;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import org.skife.jdbi.v2.sqlobject.stringtemplate.UseStringTemplate3StatementLocator;

import java.util.List;

@UseStringTemplate3StatementLocator
public interface ApplicationDAO {
    @SqlQuery("select grpId as id, name,sum(case when severity = 'Critical'" +
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
            "(select g.id as grpId ,g.name,f.severity,count(*) as count from groups g " +
            "left outer join findings f on g.ID=f.groupid " +
            "and f.status not in('Closed') " +
            " and f.isFalsePositive=false " +
            " and f.isDeleted=false " +
            " and f.notExploitable=false " +
            "and f.ownerTypeId=:ownerTypeId " +
            "and f.scanTypeId=:scanTypeId " +
            "group by g.name,grpId,f.severity) f " +
            "group by name,id " +
            " order by <sortColumn> <order> LIMIT :limit OFFSET :offset")
    @RegisterMapper(ApplicationMapper.class)
    List<Application> getApplications(@BindBean Group group, @Define("sortColumn") String sortColumn, @Define("order") String order);


    @SqlQuery("select grpId as id,name,sum(case when severity = 'Critical'" +
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
            "(select g.id as grpId,g.name,f.severity,count(*) as count from groups g " +
            "left outer join findings f on g.id=f.groupid " +
            "and f.status not in('Closed') " +
            " and f.isFalsePositive=false " +
            " and f.isDeleted=false " +
            " and f.notExploitable=false " +
            "and f.ownerTypeId=:ownerTypeId " +
            "and f.scanTypeId=:scanTypeId " +
            "group by g.name,grpId,f.severity) f " +
            "where name like concat('%', :searchTerm,'%') " +
            "group by name,id " +
            " order by <sortColumn> <order> LIMIT :limit OFFSET :offset")
    @RegisterMapper(ApplicationMapper.class)
    List<Application> getSearchResults(@BindBean Group group, @Define("sortColumn") String sortColumn, @Define("order") String order);
}
