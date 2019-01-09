package com.olacabs.jackhammer.db;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.olacabs.jackhammer.application.DBFactory;
import com.olacabs.jackhammer.common.Constants;
import com.olacabs.jackhammer.models.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.skife.jdbi.v2.Handle;
import org.skife.jdbi.v2.TransactionCallback;
import org.skife.jdbi.v2.TransactionStatus;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GroupDAOJdbi implements GroupDAO {

    @Inject
    @Named(Constants.GROUP_DAO)
    private GroupDAO groupDAO;

    @Inject
    @Named(Constants.GROUP_ROLE_DAO)
    private GroupRoleDAO groupRoleDAO;

    @Inject
    @Named(Constants.ROLE_DAO)
    private RoleDAO roleDAO;

    public int insert(Group group) {
        return groupDAO.insert(group);
    }
    public Group save(final Group group) {
        long id;
        id = insertWithTxCallback(group);
        return get(id);
    }

    public void update(Group group){
        getGroupRole(group);
        for (Role r : group.getRoles()) {
            groupRoleDAO.delete(new GroupRole(group.getId(), r.getId()));
        }
        updatetWithTxCallback(group);
    }

    public long totalCount()
    {
        return groupDAO.totalCount();
    }

    public Group getByScanTypeId(long scanTypeId) {
        return groupDAO.getByScanTypeId(scanTypeId);
    }

    public List<Group> getAll(Group group, String orderBy,String sortDirection) {
        List<Group> groups = groupDAO.getAll(group,orderBy,sortDirection);
        for (Group eachGroup : groups) {
            getGroupRole(eachGroup);
        }
        return groups;
    }
    public long totalSearchCount(Group group) {
       return groupDAO.totalSearchCount(group);

    }

    public List<Group> getSearchResults(Group group, String orderBy,String sortDirection) {
        List<Group> groups = groupDAO.getSearchResults(group,orderBy,sortDirection);
        for (Group eachGroup : groups) {
            getGroupRole(eachGroup);
        }
        return groups;
    }
    public List<Group> getAll() {
        return groupDAO.getAll();
    }
    public Group findGroupByName(String name) {
        return groupDAO.findGroupByName(name);
    }

    public Group get(long pk) {
        Group group = groupDAO.get(pk);
        getGroupRole(group);
        return group;
    }

    public void delete(long id) {
        Group group = get(id);
        if (group == null) return;

        for (Role r : group.getRoles()) {
            groupRoleDAO.delete(new GroupRole(group.getId(), r.getId()));
        }
        groupDAO.delete(group.getId());
    }

    public long insertWithTxCallback(final Group group) {
        return DBFactory.getDBI().inTransaction(new TransactionCallback<Long>() {
            public Long inTransaction(Handle handle, TransactionStatus status) throws Exception {
                long groupId;
                GroupDAO GroupDAO = handle.attach(GroupDAO.class);
                groupId = GroupDAO.insert(group);
                GroupRoleDAO groupRoleDAO = handle.attach(GroupRoleDAO.class);
                for (Role r : group.getRoles()) {
                    // update the role->permission mapping table
                    groupRoleDAO.insert(new GroupRole(groupId, r.getId()));
                }
                checkIfTxShouldBeRolledBack(group);
                return groupId;
            }
        });
    }

    public long updatetWithTxCallback(final Group group) {
        return DBFactory.getDBI().inTransaction(new TransactionCallback<Long>() {
            public Long inTransaction(Handle handle, TransactionStatus status) throws Exception {
                GroupDAO groupDAO = handle.attach(GroupDAO.class);
                groupDAO.update(group);
                GroupRoleDAO groupRoleDAO = handle.attach(GroupRoleDAO.class);
                for (long rid : group.getRoleIds()) {
                    // update the group->role mapping table
                    Role role = roleDAO.get(rid);
                    groupRoleDAO.insert(new GroupRole(group.getId(), role.getId()));
                }
                checkIfTxShouldBeRolledBack(group);
                return group.getId();
            }
        });
    }

    static void checkIfTxShouldBeRolledBack(Group group) {
        if (group.getName().equals("FAIL")) {
            throw new IllegalStateException("Name of role was FAIL");
        }
    }

    private void getGroupRole(Group group) {
        List<GroupRole> groupRoleList = groupRoleDAO.findByGroupId(group.getId());
        for (GroupRole groupRole : groupRoleList) {
            group.addRole(roleDAO.get(groupRole.getRoleId()));
        }
    }
}
