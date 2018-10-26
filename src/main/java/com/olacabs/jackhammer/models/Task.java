package com.olacabs.jackhammer.models;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.olacabs.jackhammer.common.Constants;
import com.olacabs.jackhammer.db.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Task extends AbstractModel {
    private String taskRoute;
    private long actionId;
    private long parentId;
    private long ownerTypeId;
    private String apiUrl;
    private String method;
    private Boolean accessToAll;
    private List<Task> subTasks;

    @Inject
    @Named(Constants.TASK_DAO)
    TaskDAO taskDAO;

    @Inject
    @Named(Constants.ROLE_USER_DAO)
    RoleUserDAO roleUserDAO;

    @Inject
    @Named(Constants.GROUP_ROLE_DAO)
    GroupRoleDAO groupRoleDAO;

    @Inject
    @Named(Constants.GROUP_USER_DAO)
    GroupUserDAO groupUserDAO;

    @Inject
    @Named(Constants.ROLE_TASK_DAO)
    RoleTaskDAO roleTaskDAO;

    public Boolean canAccess(User user, String apiUrl, String method) {
        List<Task> taskList = new ArrayList<Task>();
        List<RoleUser> roleUserList = getRoleUserList(user);
        List<GroupUser> groupUserList = getGroupUserList(user);

        //user roles
        for (RoleUser roleUser : roleUserList) {
            List<RoleTask> roleTasks = getRoleTaskList(roleUser.getRoleId());
            addRoleTasks(taskList, roleTasks);
        }

        //user group roles
        for (GroupUser groupUser : groupUserList) {
            List<GroupRole> groupRoleList = getGroupRoleList(groupUser);
            for (GroupRole groupRole : groupRoleList) {
                List<RoleTask> roleTasks = getRoleTaskList(groupRole.getRoleId());
                addRoleTasks(taskList, roleTasks);
            }
        }

        //default tasks
        addDefaultTasks(taskList);

        for (Task eachTask : taskList) {
            if (StringUtils.equals(eachTask.getMethod(), method) && StringUtils.equals(eachTask.getApiUrl(), apiUrl))
                return true;
        }
        return false;
    }

    private void addRoleTasks(List<Task> taskList, List<RoleTask> roleTasks) {
        for (RoleTask roleTask : roleTasks) {
            Task dbTask = taskDAO.getTask(roleTask.getTaskId());
            taskList.add(dbTask);
        }
    }

    private void addDefaultTasks(List<Task> taskList) {
        List<Task> defaultTasks = taskDAO.defaultTasks();
        for (Task task : defaultTasks) {
            taskList.add(task);
        }
    }

    private List<RoleUser> getRoleUserList(User user) {
        return roleUserDAO.findByUserId(user.getId());
    }

    private List<GroupUser> getGroupUserList(User user) {
        return groupUserDAO.findByUserId(user.getId());
    }

    private List<GroupRole> getGroupRoleList(GroupUser groupUser) {
        return groupRoleDAO.findByGroupId(groupUser.getGroupId());
    }

    private List<RoleTask> getRoleTaskList(long roleId) {
        return roleTaskDAO.findByRoleId(roleId);
    }
}
