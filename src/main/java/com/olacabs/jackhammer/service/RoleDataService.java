package com.olacabs.jackhammer.service;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.olacabs.jackhammer.common.Constants;
import com.olacabs.jackhammer.db.PermissionDAO;
import com.olacabs.jackhammer.db.TaskDAO;
import com.olacabs.jackhammer.models.PagedResponse;
import com.olacabs.jackhammer.models.Permission;
import com.olacabs.jackhammer.models.Role;
import com.olacabs.jackhammer.models.Task;

import java.util.List;

public class RoleDataService  extends AbstractDataService<Role> {

    @Inject
    @Named(Constants.TASK_DAO)
    TaskDAO taskDAO;

    @Override
    public PagedResponse<Role> getAllRecords(Role role) {
        setCRUDPermissions(paginationRecords, role,getCurrentTask(Constants.ROLES,role.getOwnerTypeId()));
        if(role.getLimit() == -1){
            paginationRecords.setItems(roleDAOJdbi.getDropdownValues());
        }
        else if(role.getSearchTerm() == null){
            paginationRecords.setItems(roleDAOJdbi.getAll(role,role.getOrderBy(),role.getSortDirection()));
            paginationRecords.setTotal(roleDAOJdbi.totalCount());
        }
        else {
            paginationRecords.setItems(roleDAOJdbi.getSearchResults(role,role.getOrderBy(),role.getSortDirection()));
            paginationRecords.setTotal(roleDAOJdbi.totalSearchCount(role));
        }
        return paginationRecords;
    }

    @Override
    public Role fetchRecordByname(Role role){
        return roleDAOJdbi.findRoleByName(role.getName());
    }

    @Override
    public Role fetchRecordById(long id){
        return
                roleDAOJdbi.get(id);
    }

    @Override
    public Role createRecord(Role role) {
        List<Long> taskIds = role.getTaskIds();
        List<Task> taskList = Lists.newArrayList();
        for (Long  taskId : taskIds) {
            Task task = taskDAO.getTask(taskId);
            taskList.add(task);
        }
        role.setTasks(taskList);
       return roleDAOJdbi.save(role);
    }
    @Override
    public void updateRecord(Role role){
        List<Long> taskIds = role.getTaskIds();
        List<Task> taskList = Lists.newArrayList();
        for (Long  taskId : taskIds) {
            Task task = taskDAO.getTask(taskId);
            taskList.add(task);
        }
        role.setTasks(taskList);
        roleDAOJdbi.update(role);
    }
    @Override
    public void deleteRecord(long id){
        roleDAOJdbi.delete(id);
    }
}
