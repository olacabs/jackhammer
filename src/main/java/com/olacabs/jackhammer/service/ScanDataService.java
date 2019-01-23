package com.olacabs.jackhammer.service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.olacabs.jackhammer.application.DBFactory;
import com.olacabs.jackhammer.common.Constants;
import com.olacabs.jackhammer.configuration.JackhammerConfiguration;
import com.olacabs.jackhammer.db.*;
import com.olacabs.jackhammer.models.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.skife.jdbi.v2.Handle;
import org.skife.jdbi.v2.TransactionCallback;
import org.skife.jdbi.v2.TransactionStatus;
import org.skife.jdbi.v2.unstable.BindIn;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Slf4j
public class ScanDataService extends AbstractDataService<Scan> {

    @Inject
    @Named(Constants.SCAN_DAO)
    ScanDAO scanDAO;

    @Inject
    @Named(Constants.SCAN_TYPE_DAO)
    ScanTypeDAO scanTypeDAO;

    @Inject
    @Named(Constants.REPO_DAO)
    RepoDAO repoDAO;

    @Inject
    @Named(Constants.FINDING_DAO)
    FindingDAO findingDAO;

    @Inject
    @Named(Constants.COMMENT_DAO)
    CommentDAO commentDAO;

    @Inject
    @Named(Constants.UPLOAD_DAO)
    UploadDAO uploadDAO;

    @Inject
    @Named(Constants.FINDING_TAG_DAO)
    FindingTagDAO findingTagDAO;

    @Inject
    @Named(Constants.TASK_DAO)
    private TaskDAO taskDAO;

    @Inject
    JackhammerConfiguration jackhammerConfiguration;

    @Inject
    @Named(Constants.GROUP_DAO)
    GroupDAO groupDAO;

    @Override
    public PagedResponse<Scan> getAllRecords(Scan scan) {
        if (scan.getSearchTerm() == null) {
            if (isCorporateRequest(scan.getOwnerTypeId())) {
                paginationRecords.setItems(scanDAO.getCorporateScans(scan, scan.getOrderBy(), scan.getSortDirection()));
                paginationRecords.setTotal(scanDAO.getCorporateTotalScanCount(scan));
            }
            if (isGroupRequest(scan.getOwnerTypeId())) {
                List<Group> userGroups = scan.getUser().getGroups();
                List<Long> groupIds = new ArrayList();
                for (Group group : userGroups) {
                    groupIds.add(group.getId());
                }
                Group additionalGroup = groupDAO.getByScanTypeId(scan.getScanTypeId());
                if(additionalGroup!=null) groupIds.add(additionalGroup.getId());
                List<Scan> scans = new ArrayList();
                if (groupIds.size() > 0)
                    scans = scanDAO.getTeamScans(scan, scan.getOrderBy(), scan.getSortDirection(), groupIds);
                paginationRecords.setItems(scans);
                paginationRecords.setTotal(scanDAO.getGroupTotalScanCount(scan, groupIds));
            }
            if (isPersonalRequest(scan.getOwnerTypeId())) {
                scan.setUserId(scan.getUser().getId());
                paginationRecords.setItems(scanDAO.getPersonalScans(scan, scan.getOrderBy(), scan.getSortDirection()));
                paginationRecords.setTotal(scanDAO.getPersonalTotalScanCount(scan));
            }
        } else {
            if (isCorporateRequest(scan.getOwnerTypeId())) {
                paginationRecords.setItems(scanDAO.getCorporateSearchResults(scan, scan.getOrderBy(), scan.getSortDirection()));
                paginationRecords.setTotal(scanDAO.getCorporateTotalSearchCount(scan));
            }
            if (isGroupRequest(scan.getOwnerTypeId())) {
                List<Group> userGroups = scan.getUser().getGroups();
                List<Long> groupIds = new ArrayList();
                for (Group group : userGroups) {
                    groupIds.add(group.getId());
                }
                List<Scan> scans = new ArrayList();
                if (groupIds.size() > 0)
                    scans = scanDAO.getGroupSearchResults(scan, scan.getOrderBy(), scan.getSortDirection(), groupIds);
                paginationRecords.setItems(scans);
                paginationRecords.setTotal(scanDAO.getGroupTotalSearchCount(scan, groupIds));
            }
            if (isPersonalRequest(scan.getOwnerTypeId())) {
                scan.setUserId(scan.getUser().getId());
                paginationRecords.setItems(scanDAO.getPersonalSearchResults(scan, scan.getOrderBy(), scan.getSortDirection()));
                paginationRecords.setTotal(scanDAO.getPersonalTotalSearchCount(scan));
            }
        }
        setCRUDPermissions(paginationRecords, scan, getCurrentTask(Constants.SCANS, scan.getOwnerTypeId()));
        addFindingPermissions(paginationRecords, scan);
        setOwnerAndScanType(paginationRecords, scan);
        return paginationRecords;
    }

    @Override
    public Scan fetchRecordByname(Scan scan) {
        return scanDAO.findScanByName(scan.getName());
    }

    @Override
    public Scan fetchRecordById(long id) {
        return scanDAO.get(id);
    }

    @Override
    public Scan createRecord(Scan scan) {
        scan.setUserId(scan.getUser().getId());
        List<Long> repoIds = scan.getRepoIds();
        if (repoIds.size() > 0) {
            scanDAO.get(insertWithTxCallback(scan));
        } else {
            ScanType scanType = scanTypeDAO.get(scan.getScanTypeId());
            if (scanType.getIsMobile()) {
                try {
                    Path destinationPath = getTempFile();
                    scan.setTarget(destinationPath.getFileName().toString());
                    scan.setApkTempFile(destinationPath.getFileName().toString());
                    Files.copy(scan.getApkFile(), destinationPath);
                    pushToS3(getS3Client(), destinationPath);
                    scan.getApkFile().close();
                } catch (IOException io) {
                    log.error("Error while creating tmp file");
                }
            }
            if (scan.getGroupIds().size() == 1) scan.setGroupId(scan.getGroupIds().get(0));
            Repo repo = new Repo();
            repo.setTarget(scan.getTarget());
            repo.setUserId(scan.getUserId());
            repo.setOwnerTypeId(scan.getOwnerTypeId());
            repo.setScanTypeId(scan.getScanTypeId());
            repo.setGroupId(scan.getGroupId());
            Repo dbRepo = repoDAO.getRepoByTarget(repo);
            if (dbRepo == null) {
                repo.setName(scan.getName());
                int repoId = repoDAO.insert(repo);
                scan.setRepoId(repoId);
            } else {
                scan.setRepoId(dbRepo.getId());
            }
            scanDAO.get(scanDAO.insert(scan));
        }
        Scan newScan = new Scan();
        return newScan;
    }

    @Override
    public void updateRecord(Scan scan) {
        scanDAO.update(scan);
    }

    @Override
    public void deleteRecord(long id) {
        List<Finding> findingList = findingDAO.getAllScanFindings(id);
        for (Finding f : findingList) {
            commentDAO.deleteFindingComments(f.getId());
            uploadDAO.deleteFindingUploads(f.getId());
            findingTagDAO.deleteByFindingId(f.getId());
        }
        findingDAO.deleteScanFindings(id);
        scanDAO.delete(id);
    }

    private long insertWithTxCallback(final Scan scan) {
        return DBFactory.getDBI().inTransaction(new TransactionCallback<Long>() {
            public Long inTransaction(Handle handle, TransactionStatus status) throws Exception {
                List<Long> repoIds = scan.getRepoIds();
                long scanId = 0;
                ScanDAO scanDAO = handle.attach(ScanDAO.class);
                for (long repoId : repoIds) {
                    long groupId = repoDAO.getGroupId(repoId);
                    Repo repo = repoDAO.findRepoById(repoId);
                    Scan newScan = new Scan();
                    newScan.setName(repo.getName());
                    newScan.setTarget(repo.getTarget());
                    newScan.setRepoId(repoId);
                    newScan.setGroupId(groupId);
                    newScan.setScheduleTypeId(scan.getScheduleTypeId());
                    newScan.setOwnerTypeId(scan.getOwnerTypeId());
                    newScan.setScanTypeId(scan.getScanTypeId());
                    newScan.setStatus(scan.getStatus());
                    newScan.setUserId(scan.getUserId());
                    scanId = scanDAO.insert(newScan);
                }
                return scanId;
            }
        });
    }

    private Path getTempFile() {
        String fileExtension = Constants.APK_FILE_FORMAT;
        long millis = System.currentTimeMillis();
        String apkVolumeDir = System.getProperty("java.io.tmpdir");
        String tmpFile = millis + fileExtension;
        Path destinationPath = FileSystems.getDefault().getPath(apkVolumeDir, tmpFile);
        return destinationPath;
    }

    private void pushToS3(AmazonS3 client, Path destinationPath) {
        File apkFile = new File(destinationPath.toAbsolutePath().toString());
        String fileFormat = Constants.TEMP_DIR_PREFIX + Constants.URL_SEPARATOR + destinationPath.getFileName();
        createS3BucketFolder(getBucketName(), client);
        client.putObject(new PutObjectRequest(
                getBucketName(), fileFormat, apkFile));
        apkFile.delete(); //deleting local temp file
    }

    private AmazonS3 getS3Client() {
        String accessKey = jackhammerConfiguration.getS3Configuration().getAccessKey();
        String secretKey = jackhammerConfiguration.getS3Configuration().getSecretKey();
        String region = jackhammerConfiguration.getS3Configuration().getRegion();
        AWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
        AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withRegion(region).withCredentials(new AWSStaticCredentialsProvider(awsCredentials)).build();
        return s3Client;
    }

    private String getBucketName() {
        return jackhammerConfiguration.getS3Configuration().getBucketName();
    }

    public void createS3BucketFolder(String bucketName, AmazonS3 client) {
        String folderName = Constants.TEMP_DIR_PREFIX;
        Boolean folderExist = false;
        ListObjectsV2Request req = new ListObjectsV2Request().withBucketName(bucketName).
                withPrefix(folderName).withDelimiter(Constants.URL_SEPARATOR);
        ListObjectsV2Result listing = client.listObjectsV2(req);
        for (String commonPrefix : listing.getCommonPrefixes()) {
            if (StringUtils.equals(commonPrefix, folderName)) {
                folderExist = true;
                break;
            }
        }
        if (folderExist == false) {
            // create meta-data for your folder and set content-length to 0
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(0);
            // create empty content
            InputStream emptyContent = new ByteArrayInputStream(new byte[0]);
            // create a PutObjectRequest passing the folder name suffixed by /
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName,
                    folderName, emptyContent, metadata);
            // send request to S3 to create folder
            client.putObject(putObjectRequest);
        }
    }

    private void addFindingPermissions(PagedResponse pagedResponse, Scan model) {
        List<Task> childTasks = getChildTasks(Constants.TASK_FINDINGS, 0);
        List<RoleTask> roleTaskList = getRoleTasks(model.getUser().getId());
        assignReadPermission(pagedResponse, roleTaskList, childTasks);
    }

    private void assignReadPermission(PagedResponse pagedResponse, List<RoleTask> roleTasks, List<Task> childTasks) {
        for (RoleTask roleTask : roleTasks) {
            final Task task = taskDAO.getTask(roleTask.getTaskId());
            Boolean taskAssigned = false;
            for (Task eachTask : childTasks) {
                if (eachTask.getId() == task.getId()) {
                    taskAssigned = true;
                    break;
                }
            }
            if (taskAssigned) {
                if (StringUtils.equals(task.getName(), Constants.LIST))
                    pagedResponse.setReadFindingsAllowed(true);
            }
        }
    }
}
