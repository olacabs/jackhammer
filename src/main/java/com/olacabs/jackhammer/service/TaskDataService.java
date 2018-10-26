package com.olacabs.jackhammer.service;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.olacabs.jackhammer.common.Constants;
import com.olacabs.jackhammer.db.*;
import com.olacabs.jackhammer.models.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
public class TaskDataService extends AbstractDataService<Task> {
    @Inject
    @Named(Constants.ACTION_DAO)
    ActionDAO actionDAO;

    @Inject
    @Named(Constants.TASK_DAO)
    TaskDAO taskDAO;

    @Inject
    @Named(Constants.ROLE_USER_DAO)
    RoleUserDAO roleUserDAO;

    @Inject
    @Named(Constants.ROLE_TASK_DAO)
    RoleTaskDAO roleTaskDAO;

    @Inject
    @Named(Constants.SCAN_TYPE_DAO)
    ScanTypeDAO scanTypeDAO;


    public PagedResponse<Task> getAllRecords(Task task) {

        Set<Long> actionSet = new HashSet<Long>();
        Set<Long> taskSet = new HashSet<Long>();
        List<Action> actionList = new ArrayList<Action>();
        List<RoleUser> roleUserList = roleUserDAO.findByUserId(task.getUser().getId());
        for (RoleUser roleUser : roleUserList) {
            List<RoleTask> roleTasks = roleTaskDAO.findByRoleId(roleUser.getRoleId());
            for (RoleTask roleTask : roleTasks) {
                Task dbTask = taskDAO.getTask(roleTask.getTaskId());
                Task parentTask = Long.valueOf(dbTask.getParentId()) == 0 ? dbTask : taskDAO.getParentTask(dbTask.getParentId());
                Action dbAction = actionDAO.get(parentTask.getActionId());
                taskSet.add(parentTask.getId());
                actionSet.add(dbAction.getId());
            }
        }
        for (Long eachAction : actionSet) {
            List<Task> taskList = taskDAO.getParentTasks(eachAction);
            List<Task> actionTasks = new ArrayList<Task>();
            Action currentAction = actionDAO.get(eachAction);
            for (Task eachTask : taskList) {
                setDefaultScanType(eachTask);
                if (taskSet.contains(eachTask.getId())) {
                    List<Task> subTasks = taskDAO.getChildTasks(eachTask.getId());
                    eachTask.setSubTasks(subTasks);
                    actionTasks.add(eachTask);
                }
            }
            currentAction.setTasks(actionTasks);
            actionList.add(currentAction);
        }
        paginationRecords.setItems(actionList);
        return paginationRecords;
    }

    public Task createRecord(Task model) {
        return null;
    }

    public Task fetchRecordByname(Task model) {
        return null;
    }

    public Task fetchRecordById(long id) {
        return null;
    }

    public void updateRecord(Task model) {

    }

    public void deleteRecord(long id) {

    }

    private void setDefaultScanType(Task task) {
            Action action = actionDAO.get(task.getActionId());
            ScanType staticScan = scanTypeDAO.getStaticScanType();
            String routeName = task.getTaskRoute();
//            routeName = routeName + task.getId();
            if (action.getIsScanTypeModule()) routeName = routeName + task.getOwnerTypeId()  + Constants.URL_SEPARATOR + staticScan.getId();
            task.setTaskRoute(routeName);
    }
}
