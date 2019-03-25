package com.olacabs.jackhammer.db;

import com.olacabs.jackhammer.models.ScanTool;
import com.olacabs.jackhammer.models.mapper.ScanToolMapper;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.List;

@RegisterMapper(ScanToolMapper.class)
public interface ScanToolDAO extends CrudDAO<ScanTool> {

    @SqlQuery("select * from scanTools where scanId=:scanId and status = 'Queued' and isDeleted=false")
    List<ScanTool> getQueuedScanTools(@Bind("scanId") long scanId);

    @SqlQuery("select * from scanTools where status = 'Progress' and isDeleted=false")
    List<ScanTool> getAllProgressScans();

    @SqlQuery("select * from scanTools where toolInstanceId=:toolInstanceId and status = 'Progress' and isDeleted=false")
    List<ScanTool> getProgressScanTools(@Bind("toolInstanceId") long toolInstanceId);

    @SqlQuery("select * from scanTools where scanId=:scanId and status = 'Failed' and isDeleted=false")
    List<ScanTool> getFailedScanTools(@Bind("scanId") long scanId);

    @SqlUpdate("update scanTools set status=:status, toolInstanceId=:toolInstanceId where scanId=:scanId and toolId=:toolId and isDeleted=false")
    void assignedToolInstance(@Bind("scanId") long scanId,@Bind("toolId") long toolId,@Bind("status") String status,@Bind("toolInstanceId") long toolInstanceId);

    @SqlUpdate("update scanTools set status=:status where toolInstanceId=:toolInstanceId and status='Progress' and isDeleted=false")
    void setToolInstanceScanStatusToQueue(@Bind("toolInstanceId") long toolInstanceId,@Bind("status") String status);


    @SqlUpdate("update scanTools set status=:status where toolInstanceId=:toolInstanceId and scanId=:scanId and isDeleted=false")
    void updateStatusPostScan(@Bind("toolInstanceId") long toolInstanceId,@Bind("status") String status,@Bind("scanId") long scanId);

    @SqlUpdate("update scanTools set isDeleted=true where scanId=:scanId")
    void deleteScanTools(@Bind("scanId") long scanId);

    @SqlUpdate("update scanTools set isDeleted=true where toolId=:toolId")
    void deleteToolScans(@Bind("toolId") long toolId);


    @SqlUpdate("insert into scanTools(scanId, toolId) values (:st.scanId, :st.toolId)")
    long insert(@BindBean("st") ScanTool scanTool);

    @SqlQuery("select * from scanTools where isDeleted=false and toolInstanceId=:toolInstanceId and status = 'Progress'")
    List<ScanTool> getByInstanceId(@Bind("toolInstanceId") long toolInstanceId);

    @SqlUpdate("update scanTools set status=:status where toolInstanceId=:toolInstanceId and scanId=:scanId and isDeleted=false")
    void pushScanToQueued(@Bind("toolInstanceId") long toolInstanceId,@Bind("status") String status,@Bind("scanId") long scanId);
}
