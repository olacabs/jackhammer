package com.olacabs.jackhammer.service;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.olacabs.jackhammer.common.Constants;
import com.olacabs.jackhammer.db.UserDAO;
import com.olacabs.jackhammer.models.PagedResponse;
import com.olacabs.jackhammer.models.User;

public class ChangePasswordDataService extends AbstractDataService<User> {

    @Inject
    @Named(Constants.USER_DAO)
    UserDAO userDAO;

    @Override
    public PagedResponse<User> getAllRecords(User model) {
        return null;
    }

    @Override
    public User createRecord(User model) {
        return null;
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
        userDAO.updatePassword(user);
    }

    @Override
    public void deleteRecord(long id) {

    }
}
