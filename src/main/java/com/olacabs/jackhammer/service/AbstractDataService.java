package com.olacabs.jackhammer.service;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.olacabs.jackhammer.common.Constants;
import com.olacabs.jackhammer.db.*;
import com.olacabs.jackhammer.enums.Severities;
import com.olacabs.jackhammer.models.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public abstract class AbstractDataService<T extends AbstractModel> {

    @Inject
    @Named(Constants.USER_DAO_JDBI)
    protected UserDAO userDAOJdbi;

    @Inject
    @Named(Constants.ROLE_DAO_JDBI)
    protected RoleDAO roleDAOJdbi;

    @Inject
    @Named(Constants.GROUP_USER_DAO)
    private GroupUserDAO groupUserDAO;

    @Inject
    @Named(Constants.GROUP_ROLE_DAO)
    private GroupRoleDAO groupRoleDAO;

    @Inject
    @Named(Constants.TASK_DAO)
    private TaskDAO taskDAO;

    @Inject
    @Named(Constants.ROLE_USER_DAO)
    private RoleUserDAO roleUserDAO;

    @Inject
    @Named(Constants.ROLE_TASK_DAO)
    private RoleTaskDAO roleTaskDAO;

    @Inject
    @Named(Constants.OWNER_TYPE_DAO)
    private OwnerTypeDAO ownerTypeDAO;

    @Inject
    @Named(Constants.SCAN_TYPE_DAO)
    private ScanTypeDAO scanTypeDAO;


    @Inject
    protected PagedResponse paginationRecords;

    public abstract PagedResponse<T> getAllRecords(T model);

    public abstract T createRecord(T model);

    public abstract T fetchRecordByname(T model);

    public abstract T fetchRecordById(long id);

    public abstract void updateRecord(T model);

    public abstract void deleteRecord(long id);

    public void setCRUDPermissions(PagedResponse<T> pagedResponse, T model, Task task) {
        List<Task> childTasks = taskDAO.getChildTasks(task.getId());
        List<RoleTask> roleTasks = getRoleTasks(model.getUser().getId());
        setCurrentUserTasks(pagedResponse, roleTasks, childTasks);
    }

    public List<RoleTask> getRoleTasks(long userId) {
        List<RoleTask> roleTaskList;
        List<RoleTask> roleTasList = new ArrayList();
        List<RoleUser> roleUserList = roleUserDAO.findByUserId(userId);
        List<GroupUser> groupUserList = groupUserDAO.findByUserId(userId);
        for (GroupUser groupUser : groupUserList) {
            List<GroupRole> groupRoleList = groupRoleDAO.findByGroupId(groupUser.getGroupId());
            for (GroupRole groupRole : groupRoleList) {
                roleTaskList = roleTaskDAO.findByRoleId(groupRole.getRoleId());
                roleTasList.addAll(roleTaskList);
            }
        }
        for (RoleUser roleUser : roleUserList) {
            roleTaskList = roleTaskDAO.findByRoleId(roleUser.getRoleId());
            roleTasList.addAll(roleTaskList);
        }
        return roleTasList;
    }

    public void setOwnerAndScanType(PagedResponse<T> pagedResponse, T model) {
        OwnerType ownerType = null;
        ScanType scanType = null;
        if (model.getOwnerTypeId() != 0) ownerType = ownerTypeDAO.get(model.getOwnerTypeId());
        if (model.getScanTypeId() != 0) scanType = scanTypeDAO.findScanTypeById(model.getScanTypeId());
        pagedResponse.setOwnerType(ownerType);
        pagedResponse.setScanType(scanType);
    }


    public SeverityCount fetchSeverityCountValues(List<SeverityCountChart> severityCountCharts) {
        SeverityCount severityCount = new SeverityCount();
        long totalCount = 0;
        for (SeverityCountChart severityCountChart : severityCountCharts) {
            totalCount += severityCountChart.getCount();
            switch (Severities.valueOf(severityCountChart.getSeverity().toUpperCase())) {
                case CRITICAL:
                    severityCount.setCriticalCount(severityCountChart.getCount());
                    break;
                case HIGH:
                    severityCount.setHighCount(severityCountChart.getCount());
                    break;
                case MEDIUM:
                    severityCount.setMediumCount(severityCountChart.getCount());
                    break;
                case LOW:
                    severityCount.setLowCount(severityCountChart.getCount());
                    break;
                case INFO:
                    severityCount.setInfoCount(severityCountChart.getCount());
                    break;
                default:
                    log.info("Invalid severity {} {}", severityCountChart.getSeverity());
            }
        }
        severityCount.setTotalCount(totalCount);
        return severityCount;
    }

    public Task getCurrentTask(String taskName, long ownerTypeId) {
        return taskDAO.getCurrentTask(taskName, ownerTypeId);
    }

    public List<Task> getChildTasks(String taskName, long ownerTypeId) {
        Task task = taskDAO.getCurrentTask(taskName, ownerTypeId);
        return taskDAO.getChildTasks(task.getId());
    }

    private void setCurrentUserTasks(PagedResponse pagedResponse, List<RoleTask> roleTasks, List<Task> childTasks) {
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
                if (StringUtils.equals(task.getName(), Constants.CREATE))
                    pagedResponse.setCreateAllowed(true);
                if (StringUtils.equals(task.getName(), Constants.READ))
                    pagedResponse.setReadAllowed(true);
                if (StringUtils.equals(task.getName(), Constants.UPDATE))
                    pagedResponse.setUpdateAllowed(true);
                if (StringUtils.equals(task.getName(), Constants.DELETE))
                    pagedResponse.setDeleteAllowed(true);
            }
        }
    }



    public Boolean isCorporateRequest(long ownerTypeId) {
        return getOwnerType(ownerTypeId).getName().contains(Constants.CORPORATE);
    }

    public Boolean isGroupRequest(long ownerTypeId) {
        return getOwnerType(ownerTypeId).getName().contains(Constants.TEAM);
    }

    public Boolean isPersonalRequest(long ownerTypeId) {
        return getOwnerType(ownerTypeId).getName().contains(Constants.PERSONAL);
    }

    private OwnerType getOwnerType(long ownerTypeId) {
        return ownerTypeDAO.get(ownerTypeId);
    }
}
