package com.olacabs.jackhammer.db;

import java.util.List;
import java.util.Set;

import com.google.inject.Inject;
import com.google.inject.name.Named;


import lombok.extern.slf4j.Slf4j;

import org.skife.jdbi.v2.TransactionCallback;
import org.skife.jdbi.v2.TransactionStatus;
import org.skife.jdbi.v2.Handle;

import com.olacabs.jackhammer.models.*;
import com.olacabs.jackhammer.application.DBFactory;
import com.olacabs.jackhammer.common.Constants;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class RoleDAOJdbi implements RoleDAO {

    @Inject
    @Named(Constants.ROLE_DAO)
    private RoleDAO roleDAO;

    @Inject
    @Named(Constants.ROLE_TASK_DAO)
    private RoleTaskDAO roleTaskDAO;

    @Inject
    @Named(Constants.TASK_DAO)
    private TaskDAO taskDAO;
    public int insert(Role role) {
        return roleDAO.insert(role);
    }
    public Role save(final Role role) {
        long id;
        id = insertWithTxCallback(role);
        return get(id);
    }

    public void update(Role role){
        getRolTasks(role);
        for (Task t : role.getTasks()) {
            roleTaskDAO.delete(new RoleTask(role.getId(), t.getId()));
        }
        updatetWithTxCallback(role);
    }

    public List<Role> getAll(Role role, String orderBy, String direction) {
        List<Role> roles = roleDAO.getAll(role,orderBy,direction);
        for (Role eachRole : roles) {
            getRolTasks(eachRole);
        }
        return roles;
    }

    public List<Role> getSearchResults(Role role,String orderBy,String direction){
        List<Role> roles = roleDAO.getSearchResults(role,orderBy,direction);
        for (Role eachRole : roles) {
            getRolTasks(eachRole);
        }
        return roles;
    }

    public long totalSearchCount(Role role){
        return roleDAO.totalSearchCount(role);
    }

    public long totalCount(){
        return roleDAO.totalCount();
    }
    public List<Role> getDropdownValues() {
        return roleDAO.getDropdownValues();
    }
    public Role findRoleByName(String name) {
      return roleDAO.findRoleByName(name);

    }

    public Set<String> getCurrentUserRoles(long userId) {
        return roleDAO.getCurrentUserRoles(userId);
    }

    public Role get(long pk) {
        Role role = roleDAO.get(pk);
        getRolTasks(role);
        return role;
    }

    public void delete(long id) {
        Role role = get(id);
        getRolTasks(role);
        for (Task t : role.getTasks()) {
            roleTaskDAO.delete(new RoleTask(role.getId(), t.getId()));
        }
        roleDAO.delete(role.getId());
    }

    public long insertWithTxCallback(final Role role) {
        return DBFactory.getDBI().inTransaction(new TransactionCallback<Long>() {
            public Long inTransaction(Handle handle, TransactionStatus status) throws Exception {
                long roleId;
                RoleDAO roleDAO = handle.attach(RoleDAO.class);
                roleId = roleDAO.insert(role);
                RoleTaskDAO roleTaskDAO = handle.attach(RoleTaskDAO.class);
                for (Task t : role.getTasks()) {
                    // update the role->task mapping table
                    roleTaskDAO.insert(new RoleTask(roleId, t.getId()));
                }
                checkIfTxShouldBeRolledBack(role);
                return roleId;
            }
        });
    }
    public long updatetWithTxCallback(final Role role) {
        return DBFactory.getDBI().inTransaction(new TransactionCallback<Long>() {
            public Long inTransaction(Handle handle, TransactionStatus status) throws Exception {
                RoleDAO roleDAO = handle.attach(RoleDAO.class);
                roleDAO.update(role);
                RoleTaskDAO roleTaskDAO = handle.attach(RoleTaskDAO.class);
                for (long pid : role.getTaskIds()) {
                    // update the role->task mapping table
                    Task task = taskDAO.getTask(pid);
                    roleTaskDAO.insert(new RoleTask(role.getId(), task.getId()));
                }
//                checkIfTxShouldBeRolledBack(role);
                return role.getId();
            }
        });
    }

    static void checkIfTxShouldBeRolledBack(Role role) {
        if (role.getName().equals("FAIL")) {
            throw new IllegalStateException("Name of role was FAIL");
        }
    }

    private void getRolTasks(Role role) {
        List<RoleTask> roleTasks = roleTaskDAO.findByRoleId(role.getId());
        for (RoleTask roleTask : roleTasks) {
            role.addTask(taskDAO.getTask(roleTask.getTaskId()));
        }
    }
}
