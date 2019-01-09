package com.olacabs.jackhammer.db;

import com.olacabs.jackhammer.models.Scan;
import com.olacabs.jackhammer.models.mapper.ScanMapper;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.Define;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import org.skife.jdbi.v2.sqlobject.stringtemplate.UseStringTemplate3StatementLocator;
import org.skife.jdbi.v2.unstable.BindIn;

import java.util.List;

@UseStringTemplate3StatementLocator
@RegisterMapper(ScanMapper.class)
public interface ScanDAO extends CrudDAO<Scan> {

    @SqlUpdate("insert into scans(name,userId,groupId,repoId,branch,scanTypeId,ownerTypeId,target,status,apkTempFile,scheduleTypeId) " +
            "values(:name,:userId,:groupId,:repoId,:branch,:scanTypeId,:ownerTypeId,:target,:status,:apkTempFile,:scheduleTypeId)")
    @GetGeneratedKeys
    int insert(@BindBean Scan scan);

    //CORPORATE SCANS
    @SqlQuery("select * from scans where scanTypeId=:scanTypeId and ownerTypeId=:ownerTypeId order by <sortColumn> <order>  LIMIT :limit OFFSET :offset")
    List<Scan> getCorporateScans(@BindBean Scan scan, @Define("sortColumn") String sortColumn, @Define("order") String order);

    @SqlQuery("select count(*) from scans where scanTypeId=:scanTypeId and ownerTypeId=:ownerTypeId")
    long getCorporateTotalScanCount(@BindBean Scan scan);

    @SqlQuery("select * from scans  where scanTypeId=:scanTypeId and ownerTypeId=:ownerTypeId and name like concat('%', :searchTerm,'%') order by <sortColumn> <order> LIMIT :limit OFFSET :offset")
    List<Scan> getCorporateSearchResults(@BindBean Scan scan, @Define("sortColumn") String sortColumn, @Define("order") String order);

    @SqlQuery("select count(*) from scans where scanTypeId=:scanTypeId and ownerTypeId=:ownerTypeId and name like concat('%', :searchTerm,'%')")
    long getCorporateTotalSearchCount(@BindBean Scan scan);

    //GROUP SCANS
    @SqlQuery("select * from scans where scanTypeId=:scanTypeId and ownerTypeId=:ownerTypeId and groupId in (<groupIds>) order by <sortColumn> <order>  LIMIT :limit OFFSET :offset")
    List<Scan> getTeamScans(@BindBean Scan scan, @Define("sortColumn") String sortColumn, @Define("order") String order, @BindIn("groupIds") List<Long> groupIds);

    @SqlQuery("select count(*) from scans where scanTypeId=:scanTypeId and ownerTypeId=:ownerTypeId and groupId in (<groupIds>)")
    long getGroupTotalScanCount(@BindBean Scan scan, @BindIn("groupIds") List<Long> groupIds);

    @SqlQuery("select * from scans  where scanTypeId=:scanTypeId and ownerTypeId=:ownerTypeId and name like concat('%', :searchTerm,'%') and groupId in (<groupIds>) order by <sortColumn> <order> LIMIT :limit OFFSET :offset")
    List<Scan> getGroupSearchResults(@BindBean Scan scan, @Define("sortColumn") String sortColumn, @Define("order") String order, @BindIn("groupIds") List<Long> groupIds);

    @SqlQuery("select count(*) from scans where scanTypeId=:scanTypeId and ownerTypeId=:ownerTypeId and name like concat('%', :searchTerm,'%') and groupId in (<groupIds>)")
    long getGroupTotalSearchCount(@BindBean Scan scan, @BindIn("groupIds") List<Long> groupIds);

    //PERSONAL SCANS
    @SqlQuery("select * from scans where scanTypeId=:scanTypeId and ownerTypeId=:ownerTypeId and userId=:userId order by <sortColumn> <order>  LIMIT :limit OFFSET :offset")
    List<Scan> getPersonalScans(@BindBean Scan scan, @Define("sortColumn") String sortColumn, @Define("order") String order);

    @SqlQuery("select count(*) from scans where scanTypeId=:scanTypeId and ownerTypeId=:ownerTypeId and userId=:userId")
    long getPersonalTotalScanCount(@BindBean Scan scan);

    @SqlQuery("select * from scans  where scanTypeId=:scanTypeId and ownerTypeId=:ownerTypeId and name like concat('%', :searchTerm,'%') and userId=:userId order by <sortColumn> <order> LIMIT :limit OFFSET :offset")
    List<Scan> getPersonalSearchResults(@BindBean Scan scan, @Define("sortColumn") String sortColumn, @Define("order") String order);

    @SqlQuery("select count(*) from scans where scanTypeId=:scanTypeId and ownerTypeId=:ownerTypeId and name like concat('%', :searchTerm,'%') and userId=:userId")
    long getPersonalTotalSearchCount(@BindBean Scan scan);


    //
    @SqlQuery("select * from scans where id=:id")
    Scan get(@Bind("id") long id);

    @SqlUpdate("update scans set startTime=:startTime,endTime=:endTime,status=:status,statusReason=:statusReason," +
            "criticalCount = criticalCount + :criticalCount, highCount = highCount + :highCount," +
            "mediumCount = mediumCount + :mediumCount,lowCount = lowCount + :lowCount,infoCount = infoCount + :infoCount,lastRunDate=:lastRunDate " +
            "where id=:id")
    void update(@BindBean Scan scan);

    @SqlUpdate("update scans set criticalCount = criticalCount + :criticalCount, highCount = highCount + :highCount," +
            "mediumCount = mediumCount + :mediumCount,lowCount = lowCount + :lowCount,infoCount = infoCount + :infoCount " +
            "where id=:id")
    void updateCounts(@BindBean Scan scan);

    @SqlQuery("select * from scans where target=:name")
    Scan findScanByName(@Bind("name") String name);

    @SqlQuery("select * from scans where status='Queued' order by RAND() limit 10")
    List<Scan> getQueuedScans();

    @SqlQuery("select * from scans where scheduleTypeId not in(0) and lastRunDate is not null order by RAND() limit 10")
    List<Scan> getScheduledScans();

    @SqlUpdate("update scans set supported=:supported,isTaggedTools=true," +
            "scanPlatforms=:scanPlatforms,cloneRequired=:cloneRequired where id=:id")
    int updatedScanDetails(@BindBean Scan scan);

    @SqlUpdate("delete from scans where id=:id")
    void delete(@Bind("id") long id);


    @SqlUpdate("update scans set status=:status where id=:id")
    void updateScanStatus(@BindBean Scan scan);

    @SqlUpdate("update scans set status=:status,statusReason=:statusReason where id=:id")
    void updateScanStatusandReason(@BindBean Scan scan);

    @SqlUpdate("update scans set status=:status where id=:id")
    void updateScanStatusToQueue(@Bind("status") String status, @Bind("id") long id);
}
