package com.olacabs.jackhammer.service;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.olacabs.jackhammer.common.Constants;
import com.olacabs.jackhammer.controllers.FindingsController;
import com.olacabs.jackhammer.db.*;
import com.olacabs.jackhammer.enums.Severities;
import com.olacabs.jackhammer.models.*;
import com.olacabs.jackhammer.security.JwtSecurity;
import com.olacabs.jackhammer.utilities.JiraClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.text.DateFormatSymbols;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class FindingDataService extends AbstractDataService<Finding> {

    @Inject
    @Named(Constants.FINDING_DAO)
    FindingDAO findingDAO;

    @Inject
    @Named(Constants.SCAN_DAO)
    ScanDAO scanDAO;

    @Inject
    @Named(Constants.REPO_DAO)
    RepoDAO repoDAO;

    @Inject
    @Named(Constants.GROUP_DAO)
    GroupDAO groupDAO;

    @Inject
    JiraClient jiraClient;

    @Inject
    @Named(Constants.TASK_DAO)
    private TaskDAO taskDAO;

    @Inject
    JwtSecurity jwtSecurity;

    @Override
    public PagedResponse<Finding> getAllRecords(Finding finding) {
        if (finding.getRepoPage() != null && finding.getRepoPage()) {
            Repo repo = repoDAO.findRepoById(finding.getRepoId());
            finding.setScanTypeId(repo.getScanTypeId());
            //repo findings list page
            if (finding.getRepoFindingsPage() != null && finding.getRepoFindingsPage()) {
                if (finding.getSearchTerm() == null) {
                    List<Finding> findingList = findingDAO.getRepoFindings(finding, finding.getOrderBy(), finding.getSortDirection());
                    paginationRecords.setItems(tagScanAndUser(findingList));
                    paginationRecords.setTotal(findingDAO.totalRepoFindingsCount(finding));
                } else {
                    List<Finding> findingList = findingDAO.getRepoFindingsSearchResults(finding, finding.getOrderBy(), finding.getSortDirection());
                    paginationRecords.setItems(tagScanAndUser(findingList));
                    paginationRecords.setTotal(findingDAO.totalRepoFindingsSearchCount(finding));
                }
            }


            Repo dbRepo = repoDAO.findRepoById(finding.getRepoId());
            if (finding.getRepoToolResults() != null && finding.getRepoToolResults()) {
                List<RepoToolResult> toolResultList = findingDAO.getRepoToolResults(finding);
                dbRepo.setRepoToolResults(toolResultList);
            }
            if (finding.getRepoChartSummary() != null && finding.getRepoChartSummary()) {
                //pie chart values
                List<SeverityCountChart> severityCountCharts = findingDAO.getRepoSeverityCount(finding);
                SeverityCount severityCountValues = fetchSeverityCountValues(severityCountCharts);

                //vul trend  values
                List<VulnerabilityTrend> vulnerabilityTrends = findingDAO.getRepoVulnerabilityTrend(finding);
                Map<String, Map<String, Long>> vulnerabilityTrendResult = fetchVulnerabilityTrendValues(vulnerabilityTrends);
                VulnerabilityTrend uiVulnerabilityTrend = new VulnerabilityTrend();
                uiVulnerabilityTrend.setVulnerabilityTrendResult(vulnerabilityTrendResult);

                dbRepo.setSeverityCount(severityCountValues);
                dbRepo.setVulnerabilityTrend(uiVulnerabilityTrend);
            }
//            Group group = groupDAO.get(dbRepo.getGroupId());
//            dbRepo.setGroup(group);
            paginationRecords.setItem(dbRepo);
        } else {
            if (finding.getExportCSV() != null && finding.getExportCSV()) {
                List<Finding> findingList = findingDAO.getCSVResults(finding);
                paginationRecords.setItems(tagScanAndUser(findingList));
            } else {
                if (finding.getSearchTerm() == null) {
                    List<Finding> findingList = findingDAO.getAll(finding, finding.getOrderBy(), finding.getSortDirection());
                    paginationRecords.setItems(tagScanAndUser(findingList));
                    paginationRecords.setTotal(findingDAO.totalCount(finding));
                } else {
                    List<Finding> findingList = findingDAO.getSearchResults(finding, finding.getOrderBy(), finding.getSortDirection());
                    paginationRecords.setItems(tagScanAndUser(findingList));
                    paginationRecords.setTotal(findingDAO.totalSearchCount(finding));
                }
            }
            Scan dbScan = scanDAO.get(finding.getScanId());
            paginationRecords.setItem(dbScan);
        }
        setCRUDPermissions(paginationRecords, finding, getCurrentTask(Constants.TASK_FINDINGS, 0));
        return paginationRecords;
    }

    @Override
    public Finding fetchRecordByname(Finding finding) {
        return null;
    }

    @Override
    public Finding fetchRecordById(long id) {
        Finding finding = findingDAO.get(id);
        Scan scan = scanDAO.get(finding.getScanId());
//        User user = userDAOJdbi.get(finding.getUserId());
        finding.setApplicationName(scan.getName());
        finding.setRepoUrl(scan.getTarget());
        if (FindingsController.repoPage) {
            finding.setTotalSize(findingDAO.totalCount(finding));
        } else {
            finding.setTotalSize(findingDAO.totalRepoFindingsCount(finding));
        }
        User user = jwtSecurity.getCurrentUser(FindingsController.userToken);
        finding.setUser(user);
        addFindingPermissions(finding, Constants.COMMENTS);
        addFindingPermissions(finding, Constants.UPLOADS);
        addFindingPermissions(finding, Constants.TAGS);
        addFindingPermissions(finding, Constants.TASK_FINDINGS);
//        if(user!=null) finding.setModifiedBy(user.getName());
        return finding;
    }

    @Override
    public Finding createRecord(Finding finding) {
        long id = findingDAO.insert(finding);
        return findingDAO.get(id);
    }

    @Override
    public void updateRecord(Finding finding) {
        finding.setModifiedBy(finding.getUser().getName());
        if (!StringUtils.equals(finding.getStatus(), null)) findingDAO.updateStatus(finding);
        if (finding.getNotExploitable() != null && finding.getNotExploitable() == true && (finding.getIds() == null || finding.getIds().size() == 0)) {
            findingDAO.updateNotExploitable(finding);
            decreaseSeverityCount(finding);
        } else if (finding.getNotExploitable() != null && finding.getNotExploitable() == true && finding.getIds().size() > 0) {
            findingDAO.bulkUpdateNotExploitable(finding.getIds());
            updateFindingsSeverity(finding.getIds());
        }
        if (finding.getIsFalsePositive() != null && finding.getIsFalsePositive() == true && (finding.getIds() == null || finding.getIds().size() == 0)) {
            findingDAO.updateFalsePositive(finding);
            decreaseSeverityCount(finding);
        } else if (finding.getIsFalsePositive() != null && finding.getIsFalsePositive() == true && finding.getIds().size() > 0) {
            findingDAO.bulkUpdateFalsePositive(finding.getIds());
            updateFindingsSeverity(finding.getIds());
        }
        if (finding.getPushedToJira() != null && finding.getPushedToJira()) {
            Finding dbFinding = findingDAO.get(finding.getId());
            jiraClient.createIssue(dbFinding);
            findingDAO.updateJiraPublishedStatus(finding);
        }
    }

    @Override
    public void deleteRecord(long id) {
        if (FindingsController.toolName == null) {
            findingDAO.delete(id);
        } else {
            findingDAO.deleteToolFindings(FindingsController.repoId, FindingsController.ownerType, FindingsController.scanType, FindingsController.toolName);
        }
    }

    private List<Finding> tagScanAndUser(List<Finding> findingList) {
        if (findingList.size() == 0) return findingList;
        for (Finding eachFinding : findingList) {
            Scan scan = scanDAO.get(eachFinding.getScanId());
            eachFinding.setApplicationName(scan.getName());
        }
        return findingList;
    }


    private Map<String, Map<String, Long>> fetchVulnerabilityTrendValues(List<VulnerabilityTrend> vulnerabilityTrends) {
        Map<String, Map<String, Long>> vulnerabilityTrendResult = new HashMap<String, Map<String, Long>>();
        LocalDate now = LocalDate.now();
        //last six months
        for (int month = 6; month >= 0; month--) {
            LocalDate earlierMonth = now.minusMonths(month);
            Map<String, Long> severities = new HashMap<String, Long>();
            severities.put(Constants.CRITICAL, 0L);
            severities.put(Constants.HIGH, 0L);
            severities.put(Constants.MEDIUM, 0L);
            severities.put(Constants.LOW, 0L);
            severities.put(Constants.INFO, 0L);
            vulnerabilityTrendResult.put(earlierMonth.getMonth().toString(), severities);
        }

        for (VulnerabilityTrend vulnerabilityTrend : vulnerabilityTrends) {
            String month = new DateFormatSymbols().getMonths()[Integer.valueOf(vulnerabilityTrend.getMonth()) - 1];
            month = StringUtils.upperCase(month);
            switch (Severities.valueOf(vulnerabilityTrend.getSeverity().toUpperCase())) {
                case CRITICAL:
                    updateSeverityCount(vulnerabilityTrendResult, vulnerabilityTrend, Constants.CRITICAL, month);
                    break;
                case HIGH:
                    updateSeverityCount(vulnerabilityTrendResult, vulnerabilityTrend, Constants.HIGH, month);
                    break;
                case MEDIUM:
                    updateSeverityCount(vulnerabilityTrendResult, vulnerabilityTrend, Constants.MEDIUM, month);
                    break;
                case LOW:
                    updateSeverityCount(vulnerabilityTrendResult, vulnerabilityTrend, Constants.LOW, month);
                    break;
                case INFO:
                    updateSeverityCount(vulnerabilityTrendResult, vulnerabilityTrend, Constants.INFO, month);
                    break;
                default:
                    log.info("Invalid severity {} {}", vulnerabilityTrend.getSeverity());
            }
        }
        return vulnerabilityTrendResult;
    }

    private void updateSeverityCount(Map<String, Map<String, Long>> vulnerabilityTrendResult, VulnerabilityTrend vulnerabilityTrend, String severity, String month) {
        if (vulnerabilityTrendResult.get(month) != null) {
            long count = vulnerabilityTrendResult.get(month).get(severity);
            count = count + vulnerabilityTrend.getCount();
            vulnerabilityTrendResult.get(month).put(severity, count);
        }
    }

    private void addFindingPermissions(Finding model, String taskName) {
        List<Task> childTasks = getChildTasks(taskName, 0);
        List<RoleTask> roleTaskList = getRoleTasks(model.getUser().getId());
        assignPermissions(model, roleTaskList, childTasks, taskName);
    }


    private void assignPermissions(Finding finding, List<RoleTask> roleTasks, List<Task> childTasks, String taskName) {
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
                if (StringUtils.equals(task.getName(), Constants.READ) && StringUtils.equals(taskName, Constants.COMMENTS))
                    finding.setReadComments(true);
                if (StringUtils.equals(task.getName(), Constants.READ) && StringUtils.equals(taskName, Constants.UPLOADS))
                    finding.setReadUploads(true);
                if (StringUtils.equals(task.getName(), Constants.READ) && StringUtils.equals(taskName, Constants.TAGS))
                    finding.setReadTags(true);

                if (StringUtils.equals(task.getName(), Constants.CREATE) && StringUtils.equals(taskName, Constants.COMMENTS))
                    finding.setCreateComments(true);
                if (StringUtils.equals(task.getName(), Constants.CREATE) && StringUtils.equals(taskName, Constants.UPLOADS))
                    finding.setCreateUploads(true);
                if (StringUtils.equals(task.getName(), Constants.CREATE) && StringUtils.equals(taskName, Constants.TAGS))
                    finding.setCreateTags(true);

                if (StringUtils.equals(task.getName(), Constants.UPDATE) && StringUtils.equals(taskName, Constants.TASK_FINDINGS))
                    finding.setUpdateFinding(true);
            }
        }
    }

    private void updateFindingsSeverity(List<Long> ids) {
        for (long id : ids) {
            Finding dbFinding = findingDAO.get(id);
            decreaseSeverityCount(dbFinding);
        }
    }

    private void decreaseSeverityCount(Finding finding) {
        Finding dbFinding = findingDAO.get(finding.getId());
        Scan scan = scanDAO.get(dbFinding.getScanId());
        switch (Severities.valueOf(dbFinding.getSeverity().toUpperCase())) {
            case CRITICAL:
                long criticalCount = findingDAO.getSeverityCount(scan.getId(),Constants.CRITICAL);
                scanDAO.updateCriticalSeverityCount(scan.getId(),criticalCount);
                break;
            case HIGH:
                long highCount = findingDAO.getSeverityCount(scan.getId(),Constants.HIGH);
                scanDAO.updateHighSeverityCount(scan.getId(),highCount);
                break;
            case MEDIUM:
                long mediumCount = findingDAO.getSeverityCount(scan.getId(),Constants.MEDIUM);
                scanDAO.updateMediumSeverityCount(scan.getId(),mediumCount);
                break;
            case LOW:
                long lowCount = findingDAO.getSeverityCount(scan.getId(),Constants.LOW);
                scanDAO.updateLowSeverityCount(scan.getId(),lowCount);
                break;
            case INFO:
                long infoCount = findingDAO.getSeverityCount(scan.getId(),Constants.INFO);
                scanDAO.updateInfoSeverityCount(scan.getId(),infoCount);
                break;
            default:
                log.info("Invalid severity {} {}", dbFinding.getSeverity());
        }
    }
}
