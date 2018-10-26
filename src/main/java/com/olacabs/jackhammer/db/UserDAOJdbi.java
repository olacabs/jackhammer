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
import lombok.extern.slf4j.Slf4j;
import org.skife.jdbi.v2.Handle;
import org.skife.jdbi.v2.TransactionCallback;
import org.skife.jdbi.v2.TransactionStatus;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Slf4j
public class UserDAOJdbi implements UserDAO {

    @Inject
    @Named(Constants.USER_DAO)
    private UserDAO userDAO;

    @Inject
    @Named(Constants.GROUP_DAO)
    private GroupDAO groupDAO;

    @Inject
    @Named(Constants.ROLE_DAO)
    private RoleDAO roleDAO;

    @Inject
    @Named(Constants.GROUP_USER_DAO)
    private GroupUserDAO groupUserDAO;

    @Inject
    @Named(Constants.ROLE_USER_DAO)
    private RoleUserDAO roleUserDAO;

    public int insert(User user) {
        return userDAO.insert(user);
    }
    public User save(final User user) {
        long id;
        id = insertWithTxCallback(user);
        return get(id);
    }

    public long totalCount()
    {
        return userDAO.totalCount();
    }

    public List<User> getAll(User user,String orderBy,String direction) {
        List<User> users = userDAO.getAll(user,orderBy,direction);
        getGroupsAndRoles(users);
        return users;
    }

    public List<User> getSearchResults(User user,String orderBy,String direction){
        List<User> users = userDAO.getSearchResults(user,orderBy,direction);
        getGroupsAndRoles(users);
        return users;
    }

    public long totalSearchCount(User user){
        return userDAO.totalSearchCount(user);
    }

    public void getGroupsAndRoles(List<User> userList){
        for (User eachUser : userList) {
            getGroupUser(eachUser);
            getRoleUser(eachUser);
        }
    }

    public User findByEmail(String name) {
        User user = userDAO.findByEmail(name);
        if(user == null) return user;
        getGroupUser(user);
        getRoleUser(user);
        return user;

    }

    public User get(long pk) {
        User user = userDAO.get(pk);
        getGroupUser(user);
        getRoleUser(user);
        return user;
    }

    public void update(User user){
        User dbUser = userDAO.get(user.getId());
        getGroupUser(dbUser);
        getRoleUser(dbUser);
        if(dbUser.getGroups()!=null && dbUser.getGroups().size() > 0) {
            for (Group g : dbUser.getGroups()) {
                groupUserDAO.delete(new GroupUser(g.getId(), dbUser.getId()));
            }
        }
        if(dbUser.getRoles()!=null && dbUser.getRoles().size() > 0) {
            for (Role r : dbUser.getRoles()) {
                roleUserDAO.delete(new RoleUser(r.getId(), dbUser.getId()));
            }
        }
        updatetWithTxCallback(user);
    }

    @Override
    public void updatePassword(User user) {
        userDAO.updatePassword(user);
    }

    @Override
    public void updateResetPasswordToken(User user) {
     userDAO.updateResetPasswordToken(user);
    }

    public void delete(long id) {
        User user = get(id);
        userDAO.delete(user.getId());
    }

    public long insertWithTxCallback(final User user) {
        return DBFactory.getDBI().inTransaction(new TransactionCallback<Long>() {
            public Long inTransaction(Handle handle, TransactionStatus status) throws Exception {
                long userId;
                UserDAO userDAO = handle.attach(UserDAO.class);
                userId = userDAO.insert(user);
                GroupUserDAO groupUserDAO = handle.attach(GroupUserDAO.class);
                if (user.getGroups() != null){
                    for (Group g : user.getGroups()) {
                        // update the user->group mapping table
                        groupUserDAO.insert(new GroupUser(g.getId(),userId));
                    }
                }

                RoleUserDAO roleUserDAO = handle.attach(RoleUserDAO.class);
                for (Role r : user.getRoles()) {
                    // update the user->role mapping table
                    roleUserDAO.insert(new RoleUser(r.getId(),userId));
                }

//                checkIfTxShouldBeRolledBack(user);
                return userId;
            }
        });
    }

    public long updatetWithTxCallback(final User user) {
        return DBFactory.getDBI().inTransaction(new TransactionCallback<Long>() {
            public Long inTransaction(Handle handle, TransactionStatus status) throws Exception {
                GroupDAO groupDAO = handle.attach(GroupDAO.class);
                GroupUserDAO groupUserDAO = handle.attach(GroupUserDAO.class);
                RoleUserDAO roleUserDAO = handle.attach(RoleUserDAO.class);

                for (long gid : user.getGroupIds()) {
                    // update the user->group mapping table
                    Group group = groupDAO.get(gid);
                    groupUserDAO.insert(new GroupUser(group.getId(),user.getId()));
                }
                for (long rid : user.getRoleIds()) {
                    // update the user->role mapping table
                    Role role = roleDAO.get(rid);
                    roleUserDAO.insert(new RoleUser(role.getId(), user.getId()));
                }
//                checkIfTxShouldBeRolledBack(user);
                return user.getId();
            }
        });
    }

    static void checkIfTxShouldBeRolledBack(User user) {
        if (user.getEmail().equals("FAIL")) {
            throw new IllegalStateException("user update was FAIL");
        }
    }

    private void getGroupUser(User user) {
        List<GroupUser> groupUserList = groupUserDAO.findByUserId(user.getId());
        if( groupUserList!=null &&  groupUserList.size() > 0) {
            for (GroupUser groupUser : groupUserList) {
                Group group = groupDAO.get(groupUser.getGroupId());
                if(group!=null) user.addGroup(groupDAO.get(groupUser.getGroupId()));
            }
        }
    }
    private void getRoleUser(User user) {
        List<RoleUser> roleUserList = roleUserDAO.findByUserId(user.getId());
        if( roleUserList!=null && roleUserList.size() > 0) {
            for (RoleUser roleUser : roleUserList) {
                Role role = roleDAO.get(roleUser.getRoleId());
                if(role!=null) user.addRole(roleDAO.get(roleUser.getRoleId()));
            }
        }
    }

}
