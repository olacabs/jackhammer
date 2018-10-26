package com.olacabs.jackhammer.service;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.olacabs.jackhammer.common.Constants;
import com.olacabs.jackhammer.db.*;
import com.olacabs.jackhammer.models.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.List;


@Slf4j
public class UserDataService extends AbstractDataService<User> {

    @Inject
    @Named(Constants.TASK_DAO)
    private TaskDAO taskDAO;

    @Inject
    @Named(Constants.OWNER_TYPE_DAO)
    private OwnerTypeDAO ownerTypeDAO;

    @Inject
    @Named(Constants.DEFAULT_ROLE_DAO)
    DefaultRoleDAO defaultRoleDAO;

    @Override
    public PagedResponse getAllRecords(User user) {
        if (user.getSearchTerm() == null) {
            paginationRecords.setItems(userDAOJdbi.getAll(user, user.getOrderBy(), user.getSortDirection()));
            paginationRecords.setTotal(userDAOJdbi.totalCount());
        } else {
            paginationRecords.setItems(userDAOJdbi.getSearchResults(user, user.getOrderBy(), user.getSortDirection()));
            paginationRecords.setTotal(userDAOJdbi.totalSearchCount(user));
        }
        setCRUDPermissions(paginationRecords, user, getCurrentTask(Constants.USERS, user.getOwnerTypeId()));

        return paginationRecords;
    }

    @Override
    public User createRecord(User user) {
        DefaultRole defaultRole = defaultRoleDAO.get();
        Role role = roleDAOJdbi.get(defaultRole.getRoleId());
        List<Role> roles = Lists.newArrayList();
        roles.add(role);
        user.setRoles(roles);
        User dbUser = userDAOJdbi.save(user);
        collectUserInfo(dbUser);
        return dbUser;
    }

    @Override
    public User fetchRecordByname(User user) {
        User dbUser = userDAOJdbi.findByEmail(user.getEmail());
        if(dbUser!=null) collectUserInfo(dbUser);
        return dbUser;
    }

    @Override
    public User fetchRecordById(long id) {
        return userDAOJdbi.get(id);
    }

    @Override
    public void updateRecord(User user) {
        userDAOJdbi.update(user);
    }

    @Override
    public void deleteRecord(long id) {
        userDAOJdbi.delete(id);
    }

    private void collectUserInfo(User user) {
        List<Task> childTasks;
        String taskName;
        List<OwnerType> ownerTypeList = ownerTypeDAO.getAll();
        OwnerType corporateOwnerType = ownerTypeDAO.getByName(Constants.CORPORATE);
        List<RoleTask> roleTaskList = getRoleTasks(user.getId());

        //EXECUTIVE DASHBOARD
        taskName = Constants.EXECUTIVE + Constants.STRING_SPACER + Constants.DASHBOARD;
        childTasks = getChildTasks(taskName, corporateOwnerType.getId());
        assignDashboardReadPermission(user, roleTaskList, childTasks, taskName);

        //OTHER DASHBOARDS
        for (OwnerType ownerType : ownerTypeList) {
            taskName = ownerType.getName() + Constants.STRING_SPACER + Constants.DASHBOARD;
            childTasks = getChildTasks(taskName, ownerType.getId());
            assignDashboardReadPermission(user, roleTaskList, childTasks, taskName);
        }

        if (user != null) user.setOwnerTypes(ownerTypeList);
    }

    private void assignDashboardReadPermission(User user, List<RoleTask> roleTasks, List<Task> childTasks, String taskName) {
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
                if (StringUtils.equals(task.getName(), Constants.READ) && taskName.contains(Constants.EXECUTIVE))
                    user.setAllowedExecutiveDashboard(true);
                if (StringUtils.equals(task.getName(), Constants.READ) && taskName.contains(Constants.CORPORATE))
                    user.setAllowedCorporateDashboard(true);
                if (StringUtils.equals(task.getName(), Constants.READ) && taskName.contains(Constants.TEAM))
                    user.setAllowedTeamDashboard(true);
                if (StringUtils.equals(task.getName(), Constants.READ) && taskName.contains(Constants.PERSONAL))
                    user.setAllowedPersonalDashboard(true);
            }
        }
    }
}
