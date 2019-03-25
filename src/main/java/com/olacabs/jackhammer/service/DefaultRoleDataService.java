package com.olacabs.jackhammer.service;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.olacabs.jackhammer.common.Constants;
import com.olacabs.jackhammer.db.DefaultRoleDAO;
import com.olacabs.jackhammer.models.DefaultRole;
import com.olacabs.jackhammer.models.PagedResponse;

public class DefaultRoleDataService extends AbstractDataService<DefaultRole> {

    @Inject
    @Named(Constants.DEFAULT_ROLE_DAO)
    DefaultRoleDAO defaultRoleDAO;


    public PagedResponse<DefaultRole> getAllRecords(DefaultRole defaultRole) {
        return null;
    }

    public DefaultRole createRecord(DefaultRole defaultRole) {
        DefaultRole dbDefaultRole = defaultRoleDAO.get();
        if (dbDefaultRole == null) {
            defaultRoleDAO.insert(defaultRole);
        }
        return dbDefaultRole;
    }

    public DefaultRole fetchRecordByname(DefaultRole model) {
        return null;
    }

    public DefaultRole fetchRecordById(long id) {
        DefaultRole defaultRole = defaultRoleDAO.get();
        return defaultRole;
    }

    public void updateRecord(DefaultRole defaultRole) {
        defaultRoleDAO.update(defaultRole);
    }

    public void deleteRecord(long id) {

    }
}
