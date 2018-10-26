package com.olacabs.jackhammer.service;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.olacabs.jackhammer.common.Constants;
import com.olacabs.jackhammer.db.UserDAO;
import com.olacabs.jackhammer.models.PagedResponse;
import com.olacabs.jackhammer.models.User;

public class ResetPasswordDataService extends AbstractDataService<User> {
    @Inject
    @Named(Constants.USER_DAO)
    UserDAO userDAO;
    @Override
    public PagedResponse<User> getAllRecords(User user) { return null; }

    @Override
    public User createRecord(User user) {
        userDAOJdbi.updateResetPasswordToken(user);
        return user;
    }

    @Override
    public User fetchRecordByname(User model) {
        return null;
    }

    @Override
    public User fetchRecordById(long id) {
        return null;
    }

    @Override
    public void updateRecord(User user) {
            user.setNewPassword(user.getPassword());
            userDAO.updatePassword(user);
    }

    @Override
    public void deleteRecord(long id) {

    }
}
