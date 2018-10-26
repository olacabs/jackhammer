package com.olacabs.jackhammer.service;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.olacabs.jackhammer.common.Constants;
import com.olacabs.jackhammer.db.ActionDAO;
import com.olacabs.jackhammer.db.RoleTaskDAO;
import com.olacabs.jackhammer.db.RoleUserDAO;
import com.olacabs.jackhammer.db.TaskDAO;
import com.olacabs.jackhammer.models.*;
import io.dropwizard.logging.filter.NullLevelFilterFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
public class ActionDataService extends AbstractDataService<Action> {

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


    public PagedResponse<Action> getAllRecords(Action action) {
        List<Action> actionList = actionDAO.getAll();
        for(Action eachAction: actionList) {
            List<Task> tasks = action.getRolesPage()!=null && action.getRolesPage() ? taskDAO.getRolesPageParentTasks(eachAction.getId()) : taskDAO.getParentTasks(eachAction.getId());
            for(Task task: tasks) {
              List<Task> subTasks = taskDAO.getChildTasks(task.getId());
              task.setSubTasks(subTasks);
            }
            eachAction.setTasks(tasks);
        }
        paginationRecords.setItems(actionList);
        return paginationRecords;
    }

    public Action createRecord(Action model) {
        return null;
    }

    public Action fetchRecordByname(Action model) {
        return null;
    }

    public Action fetchRecordById(long id) {
        return null;
    }

    public void updateRecord(Action model) {

    }

    public void deleteRecord(long id) {

    }
}
