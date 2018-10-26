package com.olacabs.jackhammer.db;

import com.olacabs.jackhammer.models.*;
import com.olacabs.jackhammer.models.mapper.SeverityCountChartMapper;
import com.olacabs.jackhammer.models.mapper.TopVulnerabilityRepoMapper;
import com.olacabs.jackhammer.models.mapper.TopVulnerableTypeMapper;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import org.skife.jdbi.v2.unstable.BindIn;

import java.util.List;

public interface AnalyticsDAO {

    //corporate
    @SqlQuery("select count(*) as count,severity from findings" +
            " where ownerTypeId=:ownerTypeId " +
            "and status <> 'Closed' " +
            " and isFalsePositive=false " +
            " and notExploitable=false " +
            "group by severity")
    @RegisterMapper(SeverityCountChartMapper.class)
    List<SeverityCountChart> getCorporateSeverityCount(@BindBean Analytics analytics);

    @SqlQuery("select count(*) from scans where status='Progress' and ownerTypeId=:ownerTypeId")
    long getCorporateRunningScanCount(@BindBean Analytics analytics);

    @SqlQuery("select count(*) from scans where status='Completed' and ownerTypeId=:ownerTypeId")
    long getCorporateCompletedScanCount(@BindBean Analytics analytics);

    @SqlQuery("select count(*) from scans where status='Queued'  and ownerTypeId=:ownerTypeId")
    long getCorporateQueuedScanCount(@BindBean Analytics analytics);

    @SqlQuery("select count(*) from scans where ownerTypeId=:ownerTypeId")
    long getCorporateTotalScanCount(@BindBean Analytics analytics);

    @SqlQuery("select count(*) from findings where status='New' and isFalsePositive=false and notExploitable=false and ownerTypeId=:ownerTypeId")
    long getCorporateNewFindingCount(@BindBean Analytics analytics);

    @SqlQuery("select count(*) as count,name as vulnerabilityType,severity from findings " +
            " where ownerTypeId=:ownerTypeId " +
            " and status <> 'Closed' " +
            " and isFalsePositive=false " +
            " and notExploitable=false " +
            " and severity in('Critical','High')" +
            "group by name,severity " +
            "order by count desc limit 5")
    @RegisterMapper(TopVulnerableTypeMapper.class)
    List<TopVulnerableType> getCorporateTopVulnerableTypes(@BindBean Analytics analytics);

    @SqlQuery("select count(*) as count,severity from findings" +
            " where ownerTypeId=:ownerTypeId " +
            "and repoId=:id" +
            " and status <> 'Closed'" +
            " and isFalsePositive=false " +
            " and notExploitable=false " +
            " and severity in('Critical','High')" +
            "group by severity")
    @RegisterMapper(SeverityCountChartMapper.class)
    List<SeverityCountChart> getCorporateSeverityCountByRepo(@BindBean Repo repo);

    @SqlQuery("select count(*) as count,repoId from findings" +
            " where ownerTypeId=:ownerTypeId " +
            "and status <> 'Closed' " +
            " and isFalsePositive=false " +
            " and notExploitable=false " +
            "and severity in('Critical','High') and repoId <> 0 " +
            "group by repoId " +
            "order by count desc limit 5")
    @RegisterMapper(TopVulnerabilityRepoMapper.class)
    List<TopVulnerableRepo> getCorporateTopVulnerabilityRepos(@BindBean Analytics analytics);


    //group
    @SqlQuery("select count(*) as count,severity from findings" +
            " where ownerTypeId=:ownerTypeId " +
            " and status <> 'Closed' " +
            " and isFalsePositive=false " +
            " and notExploitable=false " +
            " and groupId in (<groupIds>) " +
            "group by severity")
    @RegisterMapper(SeverityCountChartMapper.class)
    List<SeverityCountChart> getGroupSeverityCount(@BindBean Analytics analytics, @BindIn("groupIds") List<Long> groupIds);

    @SqlQuery("select count(*) from scans where status='Progress' and ownerTypeId=:ownerTypeId and groupId in (<groupIds>)")
    long getGroupRunningScanCount(@BindBean Analytics analytics, @BindIn("groupIds") List<Long> groupIds);

    @SqlQuery("select count(*) from scans where status='Completed' and ownerTypeId=:ownerTypeId and groupId in (<groupIds>)")
    long getGroupCompletedScanCount(@BindBean Analytics analytics, @BindIn("groupIds") List<Long> groupIds);

    @SqlQuery("select count(*) from scans where status='Queued'  and ownerTypeId=:ownerTypeId and groupId in (<groupIds>)")
    long getGroupQueuedScanCount(@BindBean Analytics analytics, @BindIn("groupIds") List<Long> groupIds);

    @SqlQuery("select count(*) from scans where ownerTypeId=:ownerTypeId and groupId in (<groupIds>)")
    long getGroupTotalScanCount(@BindBean Analytics analytics, @BindIn("groupIds") List<Long> groupIds);

    @SqlQuery("select count(*) from findings where status='New' " +
            " and isFalsePositive=false " +
            " and notExploitable=false " +
            " and ownerTypeId=:ownerTypeId " +
            " and groupId in (<groupIds>)")
    long getGroupNewFindingCount(@BindBean Analytics analytics, @BindIn("groupIds") List<Long> groupIds);

    @SqlQuery("select count(*) as count,name as vulnerabilityType,severity from findings " +
            " where ownerTypeId=:ownerTypeId " +
            " and status <> 'Closed' " +
            " and isFalsePositive=false " +
            " and notExploitable=false " +
            " and severity in('Critical','High') " +
            " and groupId in (<groupIds>) " +
            "group by name,severity " +
            "order by count desc limit 5")
    @RegisterMapper(TopVulnerableTypeMapper.class)
    List<TopVulnerableType> getGroupTopVulnerableTypes(@BindBean Analytics analytics, @BindIn("groupIds") List<Long> groupIds);

    @SqlQuery("select count(*) as count,severity from findings" +
            " where ownerTypeId=:ownerTypeId " +
            "and repoId=:id" +
            " and status <> 'Closed' " +
            " and isFalsePositive=false " +
            " and notExploitable=false " +
            " and severity in('Critical','High') " +
            " and groupId in (<groupIds>) " +
            "group by severity")
    @RegisterMapper(SeverityCountChartMapper.class)
    List<SeverityCountChart> getGroupSeverityCountByRepo(@BindBean Repo repo, @BindIn("groupIds") List<Long> groupIds);

    @SqlQuery("select count(*) as count,repoId from findings" +
            " where ownerTypeId=:ownerTypeId " +
            " and status <> 'Closed' " +
            " and isFalsePositive=false " +
            " and notExploitable=false " +
            " and severity in('Critical','High') and repoId <> 0 " +
            " and groupId in (<groupIds>) " +
            "group by repoId " +
            "order by count desc limit 5")
    @RegisterMapper(TopVulnerabilityRepoMapper.class)
    List<TopVulnerableRepo> getGroupTopVulnerabilityRepos(@BindBean Analytics analytics, @BindIn("groupIds") List<Long> groupIds);


    //personal

    @SqlQuery("select count(*) as count,severity from findings" +
            " where ownerTypeId=:ownerTypeId " +
            " and status <> 'Closed' " +
            " and isFalsePositive=false " +
            " and notExploitable=false " +
            " and userId=:userId " +
            "group by severity")
    @RegisterMapper(SeverityCountChartMapper.class)
    List<SeverityCountChart> getPersonalSeverityCount(@BindBean Analytics analytics);

    @SqlQuery("select count(*) from scans where status='Progress' and ownerTypeId=:ownerTypeId and userId=:userId")
    long getPersonalRunningScanCount(@BindBean Analytics analytics);

    @SqlQuery("select count(*) from scans where status='Completed' and ownerTypeId=:ownerTypeId and userId=:userId")
    long getPersonalCompletedScanCount(@BindBean Analytics analytics);

    @SqlQuery("select count(*) from scans where status='Queued'  and ownerTypeId=:ownerTypeId and userId=:userId")
    long getPersonalQueuedScanCount(@BindBean Analytics analytics);

    @SqlQuery("select count(*) from scans where ownerTypeId=:ownerTypeId")
    long getPersonalTotalScanCount(@BindBean Analytics analytics);

    @SqlQuery("select count(*) from findings where status='New' " +
            " and isFalsePositive=false " +
            " and notExploitable=false " +
            " and ownerTypeId=:ownerTypeId " +
            " and userId=:userId")
    long getPersonalNewFindingCount(@BindBean Analytics analytics);

    @SqlQuery("select count(*) as count,name as vulnerabilityType,severity from findings " +
            " where ownerTypeId=:ownerTypeId " +
            " and status <> 'Closed' " +
            " and isFalsePositive=false " +
            " and notExploitable=false " +
            " and severity in('Critical','High') " +
            " and userId=:userId " +
            "group by name,severity " +
            "order by count desc limit 5")
    @RegisterMapper(TopVulnerableTypeMapper.class)
    List<TopVulnerableType> getPersonalTopVulnerableTypes(@BindBean Analytics analytics);

    @SqlQuery("select count(*) as count,severity from findings" +
            " where ownerTypeId=:ownerTypeId " +
            "and repoId=:id" +
            " and status <> 'Closed' " +
            " and isFalsePositive=false " +
            " and notExploitable=false " +
            " and severity in('Critical','High') " +
            " and userId=:userId " +
            "group by severity")
    @RegisterMapper(SeverityCountChartMapper.class)
    List<SeverityCountChart> getPersonalSeverityCountByRepo(@BindBean Repo repo);

    @SqlQuery("select count(*) as count,repoId from findings" +
            " where ownerTypeId=:ownerTypeId " +
            "and status <> 'Closed' " +
            " and isFalsePositive=false " +
            " and notExploitable=false " +
            "and severity in('Critical','High') and repoId <> 0 " +
            " and userId=:userId " +
            "group by repoId " +
            "order by count desc limit 5")
    @RegisterMapper(TopVulnerabilityRepoMapper.class)
    List<TopVulnerableRepo> getPersonalTopVulnerabilityRepos(@BindBean Analytics analytics);

}
