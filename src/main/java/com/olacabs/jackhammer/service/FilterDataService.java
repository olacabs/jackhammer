package com.olacabs.jackhammer.service;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import com.olacabs.jackhammer.common.Constants;
import com.olacabs.jackhammer.db.*;
import com.olacabs.jackhammer.models.*;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class FilterDataService extends AbstractDataService<Filter> {

    @Inject
    @Named(Constants.TAG_DAO)
    TagDAO tagDAO;

    @Inject
    @Named(Constants.GROUP_DAO)
    GroupDAO groupDAO;

    @Inject
    @Named(Constants.REPO_DAO)
    RepoDAO repoDAO;

    @Inject
    @Named(Constants.TOOL_DAO)
    ToolDAO toolDAO;

    @Inject
    @Named(Constants.FILTER_DAO)
    FilterDAO filterDAO;

    @Inject
    @Named(Constants.TASK_DAO)
    TaskDAO taskDAO;


    @Override
    public PagedResponse getAllRecords(Filter filter) {
        setOwnerAndScanType(paginationRecords, filter);
        if (filter.getLimit() == -1) {
            List<Filter> filters = new ArrayList();
            Filter newFilter = new Filter();
            newFilter.setTags(tagDAO.getAll());
            newFilter.setGroups(groupDAO.getAll());
            newFilter.setRepos(repoDAO.getAll());
            newFilter.setTools(toolDAO.getAll());
            newFilter.setVulnerableTypes(filterDAO.getVulnerableTypes(filter));
            filters.add(newFilter);
            paginationRecords.setItems(filters);
            return paginationRecords;
        }
        String whereClause = getFilterQuery(filter);
        if (filter.getSearchTerm() == null) {
            List<Finding> findingList = filterDAO.getFilterResults(filter, whereClause, filter.getOrderBy(), filter.getSortDirection());
            paginationRecords.setItems(findingList);
            paginationRecords.setTotal(filterDAO.totalFilterCount(filter, whereClause));
        } else {
            List<Finding> findingList = filterDAO.getFilterSearchResults(filter, whereClause, filter.getOrderBy(), filter.getSortDirection());
            paginationRecords.setItems(findingList);
            paginationRecords.setTotal(filterDAO.totalFilterSearchCount(filter, whereClause));
        }

        setCRUDPermissions(paginationRecords, filter, taskDAO.getCurrentTask(Constants.TASK_FINDINGS, 0));
        return paginationRecords;
    }

    public Filter createRecord(Filter model) {
        return null;
    }

    public Filter fetchRecordByname(Filter model) {
        return null;
    }

    public Filter fetchRecordById(long id) {
        return null;
    }

    public void updateRecord(Filter model) {

    }

    public void deleteRecord(long id) {

    }

    private String getFilterQuery(Filter filter) {
        String whereClause = " isFalsePositive = false and ownerTypeId=:ownerTypeId ";
        if (isGroupRequest(filter.getOwnerTypeId())) {
            List<Group> userGroups = filter.getUser().getGroups();
            List<Long> groupIds = new ArrayList();
            for (Group group : userGroups) {
                groupIds.add(group.getId());
            }
            if (groupIds.size() > 0) whereClause += " and groupId in(" + getIntegerColumnValues(groupIds) + ") ";
        }

        if (isPersonalRequest(filter.getOwnerTypeId())) {
            filter.setUserId(filter.getUser().getId());
            whereClause += " and userId=:userId ";
        }

        if (filter.getScanTypeId() != 0) {
            whereClause += " and scanTypeId=:scanTypeId ";
        }

        if (filter.getTagIds() != null && filter.getTagIds().size() > 0) {
            whereClause += " and id in(select findingId from findingsTags where tagId in (" + getIntegerColumnValues(filter.getTagIds()) + ")) ";
        }
        if (filter.getStatus() != null) {
            whereClause += "and status=:status ";
        }
        if (filter.getGroupIds() != null && filter.getGroupIds().size() > 0) {
            whereClause += "and groupId in (" + getIntegerColumnValues(filter.getGroupIds()) + ") ";
        }

        if (filter.getRepoIds() != null && filter.getRepoIds().size() > 0) {
            whereClause += "and repoId in (" + getIntegerColumnValues(filter.getRepoIds()) + ") ";
        }

        if (filter.getVulnerabilities() != null && filter.getVulnerabilities().size() > 0) {
            whereClause += "and name in (" + getStringColumnValues(filter.getVulnerabilities()) + ") ";
        }

        if (filter.getSeverity() != null) {
            whereClause += "and severity=:severity ";
        }
        if (filter.getToolNames() != null && filter.getToolNames().size() > 0) {
            List<String> toolNames = new ArrayList();
            for (String toolName : filter.getToolNames()) {
                toolNames.add(toolName.toLowerCase());
            }
            whereClause += "and lower(toolName) in (" + getStringColumnValues(toolNames) + ") ";
        }

        if (filter.getAging() != 0) {
            whereClause += "and createdAt > (CURDATE() + INTERVAL - :aging DAY) ";
        }

        if (filter.getFromDate() != null) {
            whereClause += "and createdAt > :fromDate ";
        }

        if (filter.getToDate() != null) {
            whereClause += " and createdAt < :toDate ";
        }
        return whereClause;
    }

    private String getStringColumnValues(List<String> list) {
        StringBuilder builder = new StringBuilder();
        for (String value : list) {
            builder.append("'" + value + "'" + ",");
        }
        return builder.deleteCharAt(builder.length() - 1).toString();
    }

    private String getIntegerColumnValues(List<Long> list) {
        StringBuilder builder = new StringBuilder();
        for (Long value : list) {
            builder.append(value + ",");
        }
        return builder.deleteCharAt(builder.length() - 1).toString();
    }
}
