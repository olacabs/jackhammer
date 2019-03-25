package com.olacabs.jackhammer.service;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.olacabs.jackhammer.common.Constants;
import com.olacabs.jackhammer.db.PermissionDAO;
import com.olacabs.jackhammer.models.PagedResponse;
import com.olacabs.jackhammer.models.Permission;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class PermissionDataService extends AbstractDataService<Permission> {

    @Inject
    @Named(Constants.PERMISSION_DAO)
    PermissionDAO permissionDAO;

    @Override
    public PagedResponse<Permission> getAllRecords(Permission permission) {

        if(permission.getLimit() == -1){
            paginationRecords.setItems(permissionDAO.getDropdownValues());
        }
        else if(permission.getSearchTerm() == null){
            paginationRecords.setItems(permissionDAO.getAll(permission,permission.getOrderBy(),permission.getSortDirection()));
            paginationRecords.setTotal(permissionDAO.totalCount());
        }
        else {
            paginationRecords.setItems(permissionDAO.getSearchResults(permission,permission.getOrderBy(),permission.getSortDirection()));
            paginationRecords.setTotal(permissionDAO.totalSearchCount(permission));
        }
        return paginationRecords;
    }
    @Override
    public Permission createRecord(Permission permission) {
        long permissionId  = permissionDAO.insert(permission);
        return permissionDAO.get(permissionId);
    }

    @Override
    public Permission fetchRecordByname(Permission permission){
        return permissionDAO.findPermissionByName(permission.getName());
    }

    @Override
    public Permission fetchRecordById(long id){
        return permissionDAO.get(id);
    }

    @Override
    public void updateRecord(Permission permission){
        permissionDAO.update(permission);
    }

    @Override
    public void deleteRecord(long id){
        permissionDAO.delete(id);
    }

}
