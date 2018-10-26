package com.olacabs.jackhammer.service;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.olacabs.jackhammer.common.Constants;
import com.olacabs.jackhammer.db.AnalyticsDAO;
import com.olacabs.jackhammer.db.RepoDAO;
import com.olacabs.jackhammer.models.*;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class AnalyticsDataService extends AbstractDataService<Analytics> {

    @Inject
    @Named(Constants.ANALYTICS_DAO)
    AnalyticsDAO analyticsDAO;

    @Inject
    @Named(Constants.REPO_DAO)
    RepoDAO repoDAO;

    @Override
    public PagedResponse<Analytics> getAllRecords(Analytics analytics) {
        List<SeverityCountChart> severityCountCharts = new ArrayList();
        List<TopVulnerableRepo> topVulnerabilityRepos = new ArrayList();
        List<TopVulnerableType> topVulnerableTypes = new ArrayList();
        long runningScans = 0;
        long completedScans = 0;
        long queuedScans = 0;
        long totalScans = 0;
        long newFindings = 0;

        List<Group> userGroups = analytics.getUser().getGroups();
        List<Long> groupIds = new ArrayList();
        for (Group group : userGroups) {
            groupIds.add(group.getId());
        }

        if (isCorporateRequest(analytics.getOwnerTypeId())) {
            //vul count
            severityCountCharts = analyticsDAO.getCorporateSeverityCount(analytics);

            //scan statuses
            runningScans = analyticsDAO.getCorporateRunningScanCount(analytics);
            completedScans = analyticsDAO.getCorporateCompletedScanCount(analytics);
            queuedScans = analyticsDAO.getCorporateQueuedScanCount(analytics);
            totalScans = analyticsDAO.getCorporateTotalScanCount(analytics);
            newFindings = analyticsDAO.getCorporateNewFindingCount(analytics);

            //top vul repos
            topVulnerabilityRepos = analyticsDAO.getCorporateTopVulnerabilityRepos(analytics);

            //top vul types
            topVulnerableTypes = analyticsDAO.getCorporateTopVulnerableTypes(analytics);
        }

        if (isGroupRequest(analytics.getOwnerTypeId()) && groupIds.size() > 0) {

            //vul count
            severityCountCharts = analyticsDAO.getGroupSeverityCount(analytics, groupIds);

            //scan statuses
            runningScans = analyticsDAO.getGroupRunningScanCount(analytics, groupIds);
            completedScans = analyticsDAO.getGroupCompletedScanCount(analytics, groupIds);
            queuedScans = analyticsDAO.getGroupQueuedScanCount(analytics, groupIds);
            totalScans = analyticsDAO.getGroupTotalScanCount(analytics, groupIds);
            newFindings = analyticsDAO.getGroupNewFindingCount(analytics, groupIds);

            //top vul repos
            topVulnerabilityRepos = analyticsDAO.getGroupTopVulnerabilityRepos(analytics, groupIds);

            //top vul types
            topVulnerableTypes = analyticsDAO.getGroupTopVulnerableTypes(analytics, groupIds);

        }

        if (isPersonalRequest(analytics.getOwnerTypeId())) {

            analytics.setUserId(analytics.getUser().getId());
            //vul count
            severityCountCharts = analyticsDAO.getPersonalSeverityCount(analytics);

            //scan statuses
            runningScans = analyticsDAO.getPersonalRunningScanCount(analytics);
            completedScans = analyticsDAO.getPersonalCompletedScanCount(analytics);
            queuedScans = analyticsDAO.getPersonalQueuedScanCount(analytics);
            totalScans = analyticsDAO.getPersonalTotalScanCount(analytics);
            newFindings = analyticsDAO.getPersonalNewFindingCount(analytics);

            //top vul repos
            topVulnerabilityRepos = analyticsDAO.getPersonalTopVulnerabilityRepos(analytics);

            //top vul types
            topVulnerableTypes = analyticsDAO.getPersonalTopVulnerableTypes(analytics);

        }


        SeverityCount severityCountValues = fetchSeverityCountValues(severityCountCharts);
        List<Repo> repoList = fetchTopVulnerabilityRepos(topVulnerabilityRepos, analytics.getOwnerTypeId(), groupIds,analytics.getUser().getId());

        //build analytics

        Analytics uiAnalytics = new Analytics();
        uiAnalytics.setSeverityCount(severityCountValues);
        uiAnalytics.setRunningScans(runningScans);
        uiAnalytics.setCompletedScans(completedScans);
        uiAnalytics.setQueuedScans(queuedScans);
        uiAnalytics.setTotalScans(totalScans);
        uiAnalytics.setNewFindings(newFindings);
        uiAnalytics.setRepoList(repoList);
        uiAnalytics.setTopVulnerableTypes(topVulnerableTypes);

        List<Analytics> analyticsList = new ArrayList<Analytics>();
        analyticsList.add(uiAnalytics);
        paginationRecords.setItems(analyticsList);

        setOwnerAndScanType(paginationRecords, analytics);
        return paginationRecords;
    }

    private List<Repo> fetchTopVulnerabilityRepos(List<TopVulnerableRepo> topVulnerabilityRepos, long ownerTypeId, List<Long> groupIds,long userId) {
        List<Repo> repos = new ArrayList<Repo>();
        for (TopVulnerableRepo topVulnerabilityRepo : topVulnerabilityRepos) {
            List<SeverityCountChart> repoSeveritiesCount = new ArrayList();
            Repo repo = repoDAO.findRepoById(topVulnerabilityRepo.getRepoId());
            if (isCorporateRequest(ownerTypeId)) {
                repoSeveritiesCount = analyticsDAO.getCorporateSeverityCountByRepo(repo);
            }
            if (isGroupRequest(ownerTypeId) && groupIds.size() > 0) {
                repoSeveritiesCount = analyticsDAO.getGroupSeverityCountByRepo(repo, groupIds);
            }
            if (isPersonalRequest(ownerTypeId)) {
                repo.setUserId(userId);
                repoSeveritiesCount = analyticsDAO.getPersonalSeverityCountByRepo(repo);
            }
            SeverityCount severityCount = fetchSeverityCountValues(repoSeveritiesCount);
            severityCount.setTotalCount(topVulnerabilityRepo.getCount());
            repo.setSeverityCount(severityCount);
            repos.add(repo);
        }
        return repos;
    }

    public Analytics createRecord(Analytics analytics) {
        return null;
    }

    public Analytics fetchRecordByname(Analytics analytics) {
        return null;
    }

    public Analytics fetchRecordById(long id) {
        return null;
    }

    public void updateRecord(Analytics analytics) {

    }

    public void deleteRecord(long id) {

    }
}
