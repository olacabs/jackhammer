package com.olacabs.jackhammer.db;

import com.olacabs.jackhammer.models.Finding;
import com.olacabs.jackhammer.models.RepoToolResult;
import com.olacabs.jackhammer.models.SeverityCountChart;
import com.olacabs.jackhammer.models.VulnerabilityTrend;
import com.olacabs.jackhammer.models.mapper.*;

import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.Define;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import org.skife.jdbi.v2.sqlobject.stringtemplate.UseStringTemplate3StatementLocator;
import org.skife.jdbi.v2.unstable.BindIn;

import java.util.List;

@UseStringTemplate3StatementLocator
@RegisterMapper(FindingMapper.class)
public interface FindingDAO extends CrudDAO<Finding> {

    @SqlUpdate("insert into findings(name,severity,description,toolName,fileName,lineNumber,code," +
            "externalLink,solution,cvssScore,location,userInput,advisory,port,protocol,state,product,scripts," +
            "version,host,request,response,fingerprint,repoId,scanId,cveCode,cweCode,scanTypeId,ownerTypeId,userId,groupId) " +
            "values(:name,:severity,:description,:toolName,:fileName,:lineNumber,:code,:externalLink,:solution," +
            ":cvssScore,:location,:userInput,:advisory,:port,:protocol,:state,:product,:scripts,:version,:host," +
            ":request,:response,:fingerprint,:repoId,:scanId,:cveCode,:cweCode,:scanTypeId,:ownerTypeId,:userId,:groupId)")
    @GetGeneratedKeys
    int insert(@BindBean Finding finding);

    @SqlQuery("select * from findings where scanId=:scanId")
    List<Finding> findByScanId(@Bind("scanId") long scanId);

    //scan results
    @SqlQuery("select * from findings where scanId=:scanId")
    List<Finding> getCSVResults(@BindBean Finding finding);

    @SqlQuery("select * from findings where scanId=:scanId order by <sortColumn> <order>  LIMIT :limit OFFSET :offset")
    List<Finding> getAll(@BindBean Finding finding, @Define("sortColumn") String sortColumn, @Define("order") String order);

    @SqlQuery("select count(*) from findings where scanId=:scanId")
    long totalCount(@BindBean Finding finding);

    @SqlQuery("select * from findings " +
            "where scanId=:scanId " +
            "and (name like concat('%', :searchTerm,'%') " +
            "or severity like concat('%', :searchTerm,'%') " +
            "or toolName like concat('%', :searchTerm,'%'))  " +
            "order by <sortColumn> <order> LIMIT :limit OFFSET :offset")
    List<Finding> getSearchResults(@BindBean Finding finding, @Define("sortColumn") String sortColumn, @Define("order") String order);


    @SqlQuery("select count(*) from findings " +
            "where scanId=:scanId " +
            "and (name like concat('%', :searchTerm,'%') " +
            "or severity like concat('%', :searchTerm,'%') " +
            "or toolName like concat('%', :searchTerm,'%'))  " )
    long totalSearchCount(@BindBean Finding finding);

    //reo findings

    @SqlQuery("select * from findings where repoId=:repoId and severity=:severity order by <sortColumn> <order>  LIMIT :limit OFFSET :offset")
    List<Finding> getRepoFindings(@BindBean Finding finding, @Define("sortColumn") String sortColumn, @Define("order") String order);

    @SqlQuery("select count(*) from findings where repoId=:repoId and severity=:severity")
    long totalRepoFindingsCount(@BindBean Finding finding);

    @SqlQuery("select * from findings " +
            "where status not in('Closed') " +
            " and isFalsePositive=false " +
            " and notExploitable=false " +
            "and repoId=:repoId " +
            "and severity=:severity " +
            "and (name like concat('%', :searchTerm,'%') " +
            "or severity like concat('%', :searchTerm,'%') " +
            "or toolName like concat('%', :searchTerm,'%'))  " +
            "order by <sortColumn> <order> LIMIT :limit OFFSET :offset")
    List<Finding> getRepoFindingsSearchResults(@BindBean Finding finding, @Define("sortColumn") String sortColumn, @Define("order") String order);


    @SqlQuery("select count(*) from findings " +
            "where repoId=:repoId " +
            "and severity=:severity " +
            "and (name like concat('%', :searchTerm,'%') " +
            "or severity like concat('%', :searchTerm,'%') " +
            "or toolName like concat('%', :searchTerm,'%'))  " )
    long totalRepoFindingsSearchCount(@BindBean Finding finding);

    @SqlQuery("select count(*) as count,severity from findings" +
            " where status not in('Closed') " +
            " and isFalsePositive=false " +
            " and notExploitable=false " +
            "and scanTypeId=:scanTypeId " +
            "and ownerTypeId=:ownerTypeId " +
            "and repoId=:repoId group by severity ")
    @RegisterMapper(SeverityCountChartMapper.class)
    List<SeverityCountChart> getRepoSeverityCount(@BindBean Finding finding);

    @SqlQuery("select count(*) as count,severity,MONTH(createdAt) as month FROM findings " +
            "where status not in('Closed') " +
            " and isFalsePositive=false " +
            " and notExploitable=false " +
            "and scanTypeId=:scanTypeId " +
            "and ownerTypeId=:ownerTypeId  " +
            "and repoId=:repoId  " +
            "and date_format(createdAt,'%Y-%m') >=  date_format(now() - interval 6 month,'%Y-%m') " +
            "group by MONTH(createdAt),severity")
    @RegisterMapper(VulnerabilityTrendMapper.class)
    List<VulnerabilityTrend> getRepoVulnerabilityTrend(@BindBean Finding finding);

    @SqlQuery("select count(*) as count ,toolName from findings where repoId=:repoId " +
            " and isFalsePositive=false " +
            " and notExploitable=false " +
            " and status not in('Closed') " +
            " group by toolName")
    @RegisterMapper({RepoToolResultMapper.class})
    List<RepoToolResult> getRepoToolResults(@BindBean Finding finding);

    //filters


    @SqlQuery("select * from findings where id=:id")
    Finding get(@Bind("id") long id);

    @SqlUpdate("update findings set status=:status,modifiedBy=:modifiedBy where id=:id")
    void updateStatus(@BindBean Finding finding);

    @SqlUpdate("update findings set notExploitable=:notExploitable,modifiedBy=:modifiedBy where id=:id")
    void updateNotExploitable(@BindBean Finding finding);

    @SqlUpdate("update findings set notExploitable=true,modifiedBy=:modifiedBy where id in (<findingIds>)")
    void bulkUpdateNotExploitable(@BindIn("findingIds") List<Long> findingIds,@Bind("modifiedBy") String modifiedBy);

    @SqlUpdate("update findings set isFalsePositive=:isFalsePositive,modifiedBy=:modifiedBy where id=:id")
    void updateFalsePositive(@BindBean Finding finding);

    @SqlUpdate("update findings set isFalsePositive=true,modifiedBy=:modifiedBy where id in (<findingIds>)")
    void bulkUpdateFalsePositive(@BindIn("findingIds") List<Long> findingIds,@Bind("modifiedBy") String modifiedBy);

    @SqlUpdate("update findings set pushedToJira=:pushedToJira,modifiedBy=:modifiedBy where id=:id")
    void updateJiraPublishedStatus(@BindBean Finding finding);

    @SqlQuery("select * from findings where scanId=:scanId")
    List<Finding> getAllScanFindings(@Bind("scanId") long scanId);

    @SqlUpdate("delete from findings where scanId=:scanId")
    void deleteScanFindings(@Bind("scanId") long scanId);

    @SqlUpdate("delete from findings " +
            "where repoId=:repoId" +
            " and toolName=:toolName " +
            "and scanType=:scanType " +
            "and ownerType=:ownerType limit 1")
    void deleteToolFindings(@Bind("repoId") long repoId,@Bind("ownerType") String ownerType,
                            @Bind("scanType") String scanType,@Bind("toolName") String toolName);

    @SqlQuery("select count(*) from findings where scanId=:scanId and severity=:severity and isFalsePositive=false and notExploitable=false")
    long getSeverityCount(@Bind("scanId") long scanId,@Bind("severity") String severity);

}
