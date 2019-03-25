package com.olacabs.jackhammer.service;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.olacabs.jackhammer.common.Constants;
import com.olacabs.jackhammer.db.*;
import com.olacabs.jackhammer.enums.Severities;
import com.olacabs.jackhammer.models.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.text.DateFormatSymbols;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
public class DashboardDataService extends AbstractDataService<Dashboard> {

    @Inject
    @Named(Constants.DASHBOARD_DAO)
    DashboardDAO dashboardDAO;

    @Inject
    @Named(Constants.REPO_DAO)
    RepoDAO repoDAO;

    @Inject
    @Named(Constants.SCAN_TYPE_DAO)
    ScanTypeDAO scanTypeDAO;

    @Inject
    @Named(Constants.OWNER_TYPE_DAO)
    OwnerTypeDAO ownerTypeDAO;

    @Inject
    @Named(Constants.GROUP_DAO)
    GroupDAO groupDAO;

    @Override
    public PagedResponse<Dashboard> getAllRecords(Dashboard dashboard) {

        if (dashboard.getIsExecutiveDashboard() != null && dashboard.getIsExecutiveDashboard()) {
            paginationRecords.setItems(getExecutiveDashboard(dashboard));
        } else {
            paginationRecords.setItems(getNonExecutiveDashboard(dashboard));
        }
        //set owner and scan types
        setOwnerAndScanType(paginationRecords, dashboard);

        return paginationRecords;
    }

    private Map<String, Map<String, Long>> fetchVulnerabilityTrendValues(List<VulnerabilityTrend> vulnerabilityTrends, int months) {
        Map<String, Map<String, Long>> vulnerabilityTrendResult = new HashMap<String, Map<String, Long>>();
        LocalDate now = LocalDate.now();
        //last six months
        for (int month = months; month >= 0; month--) {
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

    private List<Repo> fetchTopVulnerabilityRepos(List<TopVulnerableRepo> topVulnerabilityRepos, long ownerTypeId, List<Long> groupIds, long userId) {
        List<Repo> repos = new ArrayList<Repo>();
        for (TopVulnerableRepo topVulnerabilityRepo : topVulnerabilityRepos) {
            List<SeverityCountChart> repoSeveritiesCount = new ArrayList();
            Repo repo = repoDAO.findRepoById(topVulnerabilityRepo.getRepoId());

            if (isCorporateRequest(ownerTypeId)) {
                repoSeveritiesCount = dashboardDAO.getCorporateSeverityCountByRepo(repo);
            }
            if (isGroupRequest(ownerTypeId) && groupIds.size() > 0) {
                repoSeveritiesCount = dashboardDAO.getGroupSeverityCountByRepo(repo, groupIds);
            }
            if (isPersonalRequest(ownerTypeId)) {
                repo.setUserId(userId);
                repoSeveritiesCount = dashboardDAO.getPersonalSeverityCountByRepo(repo);
            }
            SeverityCount severityCount = fetchSeverityCountValues(repoSeveritiesCount);
            severityCount.setTotalCount(topVulnerabilityRepo.getCount());
            repo.setSeverityCount(severityCount);
            repos.add(repo);
        }
        return repos;
    }

    private List<Group> fetchTopVulnerabilityApplications(List<TopVulnerableApplication> topVulnerableApplications) {
        List<Group> groups = new ArrayList<Group>();
        OwnerType ownerType = ownerTypeDAO.getDefaultOwnerType();
        for (TopVulnerableApplication topVulnerableApplication : topVulnerableApplications) {
            Group group = groupDAO.get(topVulnerableApplication.getGroupId());
            group.setOwnerTypeId(ownerType.getId());
            List<SeverityCountChart> groupSeveritiesCount = dashboardDAO.getSeverityCountByGroup(group);
            SeverityCount severityCount = fetchSeverityCountValues(groupSeveritiesCount);
            severityCount.setTotalCount(topVulnerableApplication.getCount());
            group.setSeverityCount(severityCount);
            groups.add(group);
        }
        return groups;
    }

    private void updateSeverityCount(Map<String, Map<String, Long>> vulnerabilityTrendResult, VulnerabilityTrend vulnerabilityTrend, String severity, String month) {
        if (vulnerabilityTrendResult.get(month) != null) {
            long count = vulnerabilityTrendResult.get(month).get(severity);
            count = count + vulnerabilityTrend.getCount();
            vulnerabilityTrendResult.get(month).put(severity, count);
        }
    }

    public Dashboard createRecord(Dashboard model) {
        return null;
    }

    public Dashboard fetchRecordByname(Dashboard model) {
        return null;
    }

    public Dashboard fetchRecordById(long id) {
        return null;
    }

    public void updateRecord(Dashboard model) {

    }

    public void deleteRecord(long id) {

    }

    private List<ExecutiveDashboard> getExecutiveDashboard(Dashboard dashboard) {
        List<ExecutiveDashboard> executiveDashboardList = new ArrayList();

        List<SeverityCountChart> sourceCodeCount = new ArrayList();
        List<SeverityCountChart> wordpressCount = new ArrayList();
        List<SeverityCountChart> networkCount = new ArrayList();
        List<SeverityCountChart> mobileCount = new ArrayList();
        List<SeverityCountChart> webCount = new ArrayList();

        List<ScanType> scanTypeList = scanTypeDAO.getScanTypes();

        //severity summary chart
        List<SeverityCountChart> severityCountCharts = dashboardDAO.getExecutiveSeverityCount(dashboard);
        SeverityCount severityCountValues = fetchSeverityCountValues(severityCountCharts);

        //scan types summary chart
        for (ScanType scanType : scanTypeList) {
            dashboard.setScanTypeId(scanType.getId());
            if (scanType.getIsStatic()) sourceCodeCount = dashboardDAO.getExecutiveScanTypeCount(dashboard);
            if (scanType.getIsWordpress()) wordpressCount = dashboardDAO.getExecutiveScanTypeCount(dashboard);
            if (scanType.getIsNetwork()) networkCount = dashboardDAO.getExecutiveScanTypeCount(dashboard);
            if (scanType.getIsMobile()) mobileCount = dashboardDAO.getExecutiveScanTypeCount(dashboard);
            if (scanType.getIsWeb()) webCount = dashboardDAO.getExecutiveScanTypeCount(dashboard);
        }

        SeverityCount sourceCodeSeverityCountValues = fetchSeverityCountValues(sourceCodeCount);
        SeverityCount wordpressSeverityCountValues = fetchSeverityCountValues(wordpressCount);
        SeverityCount networkSeverityCountValues = fetchSeverityCountValues(networkCount);
        SeverityCount mobileSeverityCountValues = fetchSeverityCountValues(mobileCount);
        SeverityCount webSeverityCountValues = fetchSeverityCountValues(webCount);

        ScanTypeCount scanTypeCount = new ScanTypeCount();
        scanTypeCount.setSourceCodeCount(sourceCodeSeverityCountValues);
        scanTypeCount.setWordpressCount(wordpressSeverityCountValues);
        scanTypeCount.setNetworkCount(networkSeverityCountValues);
        scanTypeCount.setMobileCount(mobileSeverityCountValues);
        scanTypeCount.setWebCount(webSeverityCountValues);

        //top critical apps vul trend chart
        List<TopVulnerableApplication> topCriticalApps = dashboardDAO.getCurrentMonthCriticalApps(dashboard);
        List<VulnerabilityTrend> criticalAppsTrendList = new ArrayList();

        for (TopVulnerableApplication topVulnerableApplication : topCriticalApps) {
            Group group = groupDAO.get(topVulnerableApplication.getGroupId());
            group.setOwnerTypeId(ownerTypeDAO.getDefaultOwnerType().getId());
            group.setId(topVulnerableApplication.getGroupId());

            List<VulnerabilityTrend> vulnerabilityTrends = dashboardDAO.getCriticalGroupTrend(group);
            Map<String, Map<String, Long>> vulnerabilityTrendResult = fetchVulnerabilityTrendValues(vulnerabilityTrends, 12);
            VulnerabilityTrend criticalVulnerabilityTrend = new VulnerabilityTrend();
            criticalVulnerabilityTrend.setVulnerabilityTrendResult(vulnerabilityTrendResult);
            criticalVulnerabilityTrend.setGroupName(group.getName());
            criticalAppsTrendList.add(criticalVulnerabilityTrend);
        }


        //top high apps vul trend chart
        List<TopVulnerableApplication> topHighApps = dashboardDAO.getCurrentMonthHighApps(dashboard);
        List<VulnerabilityTrend> highAppsTrendList = new ArrayList();

        for (TopVulnerableApplication topVulnerableApplication : topHighApps) {
            Group group = groupDAO.get(topVulnerableApplication.getGroupId());
            group.setOwnerTypeId(ownerTypeDAO.getDefaultOwnerType().getId());

            List<VulnerabilityTrend> vulnerabilityTrends = dashboardDAO.getHighGroupTrend(group);
            Map<String, Map<String, Long>> vulnerabilityTrendResult = fetchVulnerabilityTrendValues(vulnerabilityTrends, 12);
            VulnerabilityTrend highVulnerabilityTrend = new VulnerabilityTrend();
            highVulnerabilityTrend.setVulnerabilityTrendResult(vulnerabilityTrendResult);
            highVulnerabilityTrend.setGroupName(group.getName());
            highAppsTrendList.add(highVulnerabilityTrend);
        }

        //bugs closing trend
        List<VulnerabilityTrend> closingVulnerabilityTrend = dashboardDAO.getBugsClosingTrend(dashboard);
        Map<String, Map<String, Long>> closingVulnerabilityTrendResult = fetchVulnerabilityTrendValues(closingVulnerabilityTrend, 12);
        VulnerabilityTrend uiClosingVulnerabilityTrend = new VulnerabilityTrend();
        uiClosingVulnerabilityTrend.setVulnerabilityTrendResult(closingVulnerabilityTrendResult);

        //top five applications chart
        List<TopVulnerableApplication> topVulnerableApplications = dashboardDAO.getExecutiveTopFiveApps(dashboard);
        List<Group> groupList = fetchTopVulnerabilityApplications(topVulnerableApplications);

        //building executive dashboard
        ExecutiveDashboard executiveDashboard = new ExecutiveDashboard();
        executiveDashboard.setScanTypeCount(scanTypeCount);
        executiveDashboard.setSeverityCount(severityCountValues);
        executiveDashboard.setGroups(groupList);
        executiveDashboard.setCriticalVulnerabilityTrend(criticalAppsTrendList);
        executiveDashboard.setHighVulnerabilityTrend(highAppsTrendList);
        executiveDashboard.setBugsClosingTrend(uiClosingVulnerabilityTrend);

        executiveDashboardList.add(executiveDashboard);
        return executiveDashboardList;
    }

    private List<Dashboard> getNonExecutiveDashboard(Dashboard dashboard) {

        List<Dashboard> dashboardList = new ArrayList<Dashboard>();
        List<SeverityCountChart> severityCountCharts = new ArrayList();
        List<VulnerabilityTrend> vulnerabilityTrends = new ArrayList();
        List<TopVulnerableType> topVulnerableTypes = new ArrayList();
        List<TopVulnerableRepo> topVulnerabilityRepos = new ArrayList();
        VulnerabilityTrend uiVulnerabilityTrend = new VulnerabilityTrend();
        SeverityCount severityCountValues;
        Map<String, Map<String, Long>> vulnerabilityTrendResult;

        List<Group> userGroups = dashboard.getUser().getGroups();
        List<Long> groupIds = new ArrayList();
        for (Group group : userGroups) {
            groupIds.add(group.getId());
        }

        if (isCorporateRequest(dashboard.getOwnerTypeId())) {
            severityCountCharts = dashboardDAO.getCorporateSeverityCount(dashboard);
            vulnerabilityTrends = dashboardDAO.getCorporateVulnerabilityTrend(dashboard);

            //top vul types
            topVulnerableTypes = dashboardDAO.getCorporateTopVulnerableTypes(dashboard);

            //top vul repos chart
            topVulnerabilityRepos = dashboardDAO.getCorporateTopVulnerabilityRepos(dashboard);
        }

        if (isGroupRequest(dashboard.getOwnerTypeId()) && groupIds.size() > 0) {
            severityCountCharts = dashboardDAO.getGroupSeverityCount(dashboard, groupIds);
            vulnerabilityTrends = dashboardDAO.getGroupVulnerabilityTrend(dashboard, groupIds);

            //top vul types
            topVulnerableTypes = dashboardDAO.getGroupTopVulnerableTypes(dashboard, groupIds);

            //top vul repos chart
            topVulnerabilityRepos = dashboardDAO.getGroupTopVulnerabilityRepos(dashboard, groupIds);
        }

        if (isPersonalRequest(dashboard.getOwnerTypeId())) {
            dashboard.setUserId(dashboard.getUser().getId());
            severityCountCharts = dashboardDAO.getPersonalSeverityCount(dashboard);
            vulnerabilityTrends = dashboardDAO.getPersonalVulnerabilityTrend(dashboard);

            //top vul types
            topVulnerableTypes = dashboardDAO.getPersonalTopVulnerableTypes(dashboard);

            //top vul repos chart
            topVulnerabilityRepos = dashboardDAO.getPersonalTopVulnerabilityRepos(dashboard);
        }

        //vul trend chart
        vulnerabilityTrendResult = fetchVulnerabilityTrendValues(vulnerabilityTrends, 6);
        uiVulnerabilityTrend.setVulnerabilityTrendResult(vulnerabilityTrendResult);

        //vul count chart
        severityCountValues = fetchSeverityCountValues(severityCountCharts);

        List<Repo> repoList = fetchTopVulnerabilityRepos(topVulnerabilityRepos, dashboard.getOwnerTypeId(), groupIds, dashboard.getUser().getId());

        //build dashboard
        Dashboard uiDashboard = new Dashboard();

        uiDashboard.setSeverityCount(severityCountValues);
        uiDashboard.setVulnerabilityTrend(uiVulnerabilityTrend);
        uiDashboard.setTopVulnerableRepos(repoList);
        uiDashboard.setTopVulnerableTypes(topVulnerableTypes);
        dashboardList.add(uiDashboard);
        return dashboardList;
    }
}
