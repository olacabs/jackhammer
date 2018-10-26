package com.olacabs.jackhammer.service;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.olacabs.jackhammer.common.Constants;
import com.olacabs.jackhammer.db.ApplicationDAO;
import com.olacabs.jackhammer.db.GroupDAO;
import com.olacabs.jackhammer.models.Group;
import com.olacabs.jackhammer.models.PagedResponse;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class ApplicationDataService extends AbstractDataService<Group> {

    @Inject
    @Named(Constants.APPLICATION_DAO)
    ApplicationDAO applicationDAO;

    @Inject
    @Named(Constants.GROUP_DAO)
    GroupDAO groupDAO;

    @Override
    public PagedResponse getAllRecords(Group group) {
        if(group.getSearchTerm() == null) {
            paginationRecords.setItems(applicationDAO.getApplications(group, group.getOrderBy(), group.getSortDirection()));
            paginationRecords.setTotal(groupDAO.totalCount());
        } else {
            paginationRecords.setItems(applicationDAO.getSearchResults(group, group.getOrderBy(), group.getSortDirection()));
            paginationRecords.setTotal(groupDAO.totalSearchCount(group));
        }
        setOwnerAndScanType(paginationRecords,group);
        return paginationRecords;
    }

    public Group createRecord(Group model) {
        return null;
    }

    public Group fetchRecordByname(Group model) {
        return null;
    }

    public Group fetchRecordById(long id) {
        return null;
    }

    public void updateRecord(Group model) {

    }

    public void deleteRecord(long id) {

    }


}
