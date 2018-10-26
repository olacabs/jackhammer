package com.olacabs.jackhammer.db;

import com.olacabs.jackhammer.models.*;
import com.olacabs.jackhammer.models.mapper.*;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import org.skife.jdbi.v2.unstable.BindIn;

import java.util.List;

public interface DashboardDAO {

    //execute dashboard queries

    @SqlQuery("select count(*) as count,severity from findings" +
            " where ownerTypeId=:ownerTypeId  " +
            "and status <> 'Closed' " +
            " and isFalsePositive=false " +
            " and notExploitable=false " +
            "group by severity")
    @RegisterMapper(SeverityCountChartMapper.class)
    List<SeverityCountChart> getExecutiveSeverityCount(@BindBean Dashboard dashboard);

    @SqlQuery("select count(*) as count,severity from findings" +
            " where ownerTypeId=:ownerTypeId  " +
            " and scanTypeId=:scanTypeId" +
            " and status <> 'Closed' " +
            " and isFalsePositive=false " +
            " and notExploitable=false " +
            " group by severity")
    @RegisterMapper(SeverityCountChartMapper.class)
    List<SeverityCountChart> getExecutiveScanTypeCount(@BindBean Dashboard dashboard);

    @SqlQuery("select count(*) as count, groupId from findings " +
            " where ownerTypeId=:ownerTypeId " +
            " and status <> 'Closed'" +
            " and isFalsePositive=false " +
            " and notExploitable=false " +
            " and severity in('Critical','High') " +
            " and groupId <> 0 " +
            "group by groupId " +
            "order by count desc limit 5")
    @RegisterMapper(TopVulnerabilityApplicationMapper.class)
    List<TopVulnerableApplication> getExecutiveTopFiveApps(@BindBean Dashboard dashboard);


    @SqlQuery("select count(*) as count,severity from findings" +
            " where ownerTypeId=:ownerTypeId " +
            " and groupId=:id " +
            " and status <> 'Closed' " +
            " and isFalsePositive=false " +
            " and notExploitable=false " +
            "group by severity")
    @RegisterMapper(SeverityCountChartMapper.class)
    List<SeverityCountChart> getSeverityCountByGroup(@BindBean Group group);

    @SqlQuery("select count(*) as count,groupId from findings " +
            "where MONTH(createdAt) = MONTH(CURRENT_DATE()) and YEAR(createdAt) = YEAR(CURRENT_DATE()) " +
            "and ownerTypeId=:ownerTypeId" +
            " and severity='Critical' " +
            " and status <> 'Closed' " +
            " and isFalsePositive=false " +
            " and notExploitable=false " +
            " and groupId <> 0 " +
            "group by groupId order by count desc limit 5")
    @RegisterMapper(TopVulnerabilityApplicationMapper.class)
    List<TopVulnerableApplication> getCurrentMonthCriticalApps(@BindBean Dashboard dashboard);

    @SqlQuery("select count(*) as count,groupId from findings " +
            "where MONTH(createdAt) = MONTH(CURRENT_DATE()) and YEAR(createdAt) = YEAR(CURRENT_DATE()) " +
            "and ownerTypeId=:ownerTypeId" +
            " and severity='High' " +
            " and status <> 'Closed' " +
            " and isFalsePositive=false " +
            " and notExploitable=false " +
            " and groupId <> 0 " +
            "group by groupId order by count desc limit 5")
    @RegisterMapper(TopVulnerabilityApplicationMapper.class)
    List<TopVulnerableApplication> getCurrentMonthHighApps(@BindBean Dashboard dashboard);


    @SqlQuery("select count(*) as count,severity,MONTH(createdAt) as month FROM findings " +
            "where ownerTypeId=:ownerTypeId " +
            "and groupId=:id " +
            "and status <> 'Closed' " +
            " and isFalsePositive=false " +
            " and notExploitable=false " +
            " and severity='Critical' " +
            "and date_format(createdAt,'%Y-%m') >=  date_format(now() - interval 12 month,'%Y-%m') " +
            "group by MONTH(createdAt),severity")
    @RegisterMapper(VulnerabilityTrendMapper.class)
    List<VulnerabilityTrend> getCriticalGroupTrend(@BindBean Group group);

    @SqlQuery("select count(*) as count,severity,MONTH(createdAt) as month FROM findings " +
            "where ownerTypeId=:ownerTypeId " +
            "and groupId=:id " +
            "and status <> 'Closed' " +
            " and isFalsePositive=false " +
            " and notExploitable=false " +
            " and severity='High' " +
            "and date_format(createdAt,'%Y-%m') >=  date_format(now() - interval 12 month,'%Y-%m') " +
            "group by MONTH(createdAt),severity")
    @RegisterMapper(VulnerabilityTrendMapper.class)
    List<VulnerabilityTrend> getHighGroupTrend(@BindBean Group group);

    @SqlQuery("select count(*) as count,severity,MONTH(createdAt) as month FROM findings " +
            "where ownerTypeId=:ownerTypeId " +
            "and status = 'Closed' " +
            " and isFalsePositive=false " +
            " and notExploitable=false " +
            "and date_format(createdAt,'%Y-%m') >=  date_format(now() - interval 12 month,'%Y-%m') " +
            "group by MONTH(createdAt),severity")
    @RegisterMapper(VulnerabilityTrendMapper.class)
    List<VulnerabilityTrend> getBugsClosingTrend(@BindBean Dashboard dashboard);

    //other dashboard queries
    // corporate dashboard
    @SqlQuery("select count(*) as count,severity from findings" +
            " where scanTypeId=:scanTypeId " +
            "and ownerTypeId=:ownerTypeId " +
            "and status <> 'Closed' " +
            " and isFalsePositive=false " +
            " and notExploitable=false " +
            "group by severity")
    @RegisterMapper(SeverityCountChartMapper.class)
    List<SeverityCountChart> getCorporateSeverityCount(@BindBean Dashboard dashboard);

    @SqlQuery("select count(*) as count,severity,MONTH(createdAt) as month FROM findings " +
            "where scanTypeId=:scanTypeId " +
            " and ownerTypeId=:ownerTypeId  " +
            " and status <> 'Closed' " +
            " and isFalsePositive=false " +
            " and notExploitable=false " +
            "and date_format(createdAt,'%Y-%m') >=  date_format(now() - interval 6 month,'%Y-%m') " +
            "group by MONTH(createdAt),severity")
    @RegisterMapper(VulnerabilityTrendMapper.class)
    List<VulnerabilityTrend> getCorporateVulnerabilityTrend(@BindBean Dashboard dashboard);

    @SqlQuery("select count(*) as count,repoId from findings" +
            " where scanTypeId=:scanTypeId " +
            "and ownerTypeId=:ownerTypeId " +
            " and status <> 'Closed' " +
            " and isFalsePositive=false " +
            " and notExploitable=false " +
            " and repoId!=0 " +
            "group by repoId " +
            "order by count desc limit 5")
    @RegisterMapper(TopVulnerabilityRepoMapper.class)
    List<TopVulnerableRepo> getCorporateTopVulnerabilityRepos(@BindBean Dashboard dashboard);

    @SqlQuery("select count(*) as count,severity from findings" +
            " where scanTypeId=:scanTypeId " +
            "and ownerTypeId=:ownerTypeId " +
            "and repoId=:id " +
            "and status <> 'Closed' " +
            " and isFalsePositive=false " +
            " and notExploitable=false " +
            "group by severity")
    @RegisterMapper(SeverityCountChartMapper.class)
    List<SeverityCountChart> getCorporateSeverityCountByRepo(@BindBean Repo repo);

    @SqlQuery("select count(*) as count,name as vulnerabilityType,severity from findings " +
            "where scanTypeId=:scanTypeId " +
            " and ownerTypeId=:ownerTypeId " +
            "and status <> 'Closed' " +
            " and isFalsePositive=false " +
            " and notExploitable=false " +
            " and severity in('Critical','High')" +
            "group by name,severity " +
            "order by count desc limit 5")
    @RegisterMapper(TopVulnerableTypeMapper.class)
    List<TopVulnerableType> getCorporateTopVulnerableTypes(@BindBean Dashboard dashboard);


    //group dashboard
    @SqlQuery("select count(*) as count,severity from findings" +
            " where scanTypeId=:scanTypeId " +
            "and ownerTypeId=:ownerTypeId " +
            "and status <> 'Closed' " +
            " and isFalsePositive=false " +
            " and notExploitable=false " +
            " and groupId in (<groupIds>) " +
            "group by severity")
    @RegisterMapper(SeverityCountChartMapper.class)
    List<SeverityCountChart> getGroupSeverityCount(@BindBean Dashboard dashboard,@BindIn("groupIds") List<Long> groupIds);

    @SqlQuery("select count(*) as count,severity,MONTH(createdAt) as month FROM findings " +
            "where scanTypeId=:scanTypeId " +
            " and ownerTypeId=:ownerTypeId  " +
            " and status <> 'Closed' " +
            " and isFalsePositive=false " +
            " and notExploitable=false " +
            " and groupId in (<groupIds>) " +
            " and date_format(createdAt,'%Y-%m') >=  date_format(now() - interval 6 month,'%Y-%m') " +
            "group by MONTH(createdAt),severity")
    @RegisterMapper(VulnerabilityTrendMapper.class)
    List<VulnerabilityTrend> getGroupVulnerabilityTrend(@BindBean Dashboard dashboard,@BindIn("groupIds") List<Long> groupIds);

    @SqlQuery("select count(*) as count,repoId from findings" +
            " where scanTypeId=:scanTypeId " +
            "and ownerTypeId=:ownerTypeId " +
            "and status <> 'Closed' " +
            " and isFalsePositive=false " +
            " and notExploitable=false " +
            " and groupId in (<groupIds>) " +
            " and repoId!=0 " +
            "group by repoId " +
            "order by count desc limit 5")
    @RegisterMapper(TopVulnerabilityRepoMapper.class)
    List<TopVulnerableRepo> getGroupTopVulnerabilityRepos(@BindBean Dashboard dashboard,@BindIn("groupIds") List<Long> groupIds);

    @SqlQuery("select count(*) as count,severity from findings" +
            " where scanTypeId=:scanTypeId " +
            " and ownerTypeId=:ownerTypeId " +
            " and repoId=:id " +
            " and status <> 'Closed' " +
            " and isFalsePositive=false " +
            " and notExploitable=false " +
            " and groupId in (<groupIds>) " +
            "group by severity")
    @RegisterMapper(SeverityCountChartMapper.class)
    List<SeverityCountChart> getGroupSeverityCountByRepo(@BindBean Repo repo,@BindIn("groupIds") List<Long> groupIds);

    @SqlQuery("select count(*) as count,name as vulnerabilityType,severity from findings " +
            "where scanTypeId=:scanTypeId " +
            " and ownerTypeId=:ownerTypeId " +
            "and status <> 'Closed' " +
            " and isFalsePositive=false " +
            " and notExploitable=false " +
            " and severity in('Critical','High') " +
            " and groupId in (<groupIds>) " +
            "group by name,severity " +
            "order by count desc limit 5")
    @RegisterMapper(TopVulnerableTypeMapper.class)
    List<TopVulnerableType> getGroupTopVulnerableTypes(@BindBean Dashboard dashboard,@BindIn("groupIds") List<Long> groupIds);


    // personal dashboard
    @SqlQuery("select count(*) as count,severity from findings" +
            " where scanTypeId=:scanTypeId " +
            " and ownerTypeId=:ownerTypeId " +
            " and userId=:userId " +
            " and status <> 'Closed' " +
            " and isFalsePositive=false " +
            " and notExploitable=false " +
            "group by severity")
    @RegisterMapper(SeverityCountChartMapper.class)
    List<SeverityCountChart> getPersonalSeverityCount(@BindBean Dashboard dashboard);

    @SqlQuery("select count(*) as count,severity,MONTH(createdAt) as month FROM findings " +
            "where scanTypeId=:scanTypeId " +
            "and ownerTypeId=:ownerTypeId  " +
            "and status <> 'Closed' " +
            " and isFalsePositive=false " +
            " and notExploitable=false " +
            "and userId=:userId " +
            "and date_format(createdAt,'%Y-%m') >=  date_format(now() - interval 6 month,'%Y-%m') " +
            "group by MONTH(createdAt),severity")
    @RegisterMapper(VulnerabilityTrendMapper.class)
    List<VulnerabilityTrend> getPersonalVulnerabilityTrend(@BindBean Dashboard dashboard);

    @SqlQuery("select count(*) as count,repoId from findings" +
            " where scanTypeId=:scanTypeId " +
            " and ownerTypeId=:ownerTypeId " +
            " and status <> 'Closed' " +
            " and isFalsePositive=false " +
            " and notExploitable=false " +
            " and repoId!=0 " +
            " and userId=:userId " +
            "group by repoId " +
            "order by count desc limit 5")
    @RegisterMapper(TopVulnerabilityRepoMapper.class)
    List<TopVulnerableRepo> getPersonalTopVulnerabilityRepos(@BindBean Dashboard dashboard);

    @SqlQuery("select count(*) as count,severity from findings" +
            " where scanTypeId=:scanTypeId " +
            " and ownerTypeId=:ownerTypeId " +
            " and repoId=:id " +
            " and userId=:userId " +
            " and status <> 'Closed' " +
            " and isFalsePositive=false " +
            " and notExploitable=false " +
            "group by severity")
    @RegisterMapper(SeverityCountChartMapper.class)
    List<SeverityCountChart> getPersonalSeverityCountByRepo(@BindBean Repo repo);

    @SqlQuery("select count(*) as count,name as vulnerabilityType,severity from findings " +
            "where scanTypeId=:scanTypeId " +
            " and ownerTypeId=:ownerTypeId " +
            " and status <> 'Closed' " +
            " and isFalsePositive=false " +
            " and notExploitable=false " +
            " and userId=:userId " +
            " and severity in('Critical','High')" +
            "group by name,severity " +
            "order by count desc limit 5")
    @RegisterMapper(TopVulnerableTypeMapper.class)
    List<TopVulnerableType> getPersonalTopVulnerableTypes(@BindBean Dashboard dashboard);


}



