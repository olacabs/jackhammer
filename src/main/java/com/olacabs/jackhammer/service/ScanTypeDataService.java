package com.olacabs.jackhammer.service;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.olacabs.jackhammer.common.Constants;
import com.olacabs.jackhammer.db.OwnerTypeDAO;
import com.olacabs.jackhammer.db.ScanTypeDAO;
import com.olacabs.jackhammer.models.PagedResponse;
import com.olacabs.jackhammer.models.ScanType;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class ScanTypeDataService extends AbstractDataService<ScanType> {

    @Inject
    @Named(Constants.SCAN_TYPE_DAO)
    ScanTypeDAO scanTypeDAO;

    @Inject
    @Named(Constants.OWNER_TYPE_DAO)
    OwnerTypeDAO ownerTypeDAO;

    @Override
    public PagedResponse<ScanType> getAllRecords(ScanType scanType) {
        if(scanType.getLimit() == -1){
            paginationRecords.setItems(scanTypeDAO.getDropdownValues(scanType));
        }
        else if(scanType.getSearchTerm() == null){
            paginationRecords.setItems(scanTypeDAO.getAll(scanType,scanType.getOrderBy(),scanType.getSortDirection()));
            paginationRecords.setTotal(scanTypeDAO.totalCount());
        }
        setCRUDPermissions(paginationRecords, scanType,getCurrentTask(Constants.SCAN_TYPES,0));
        setOwnerAndScanType(paginationRecords,scanType);
        if(paginationRecords.getOwnerType() == null) paginationRecords.setOwnerType(ownerTypeDAO.getDefaultOwnerType());
        return paginationRecords;
    }

    @Override
    public ScanType fetchRecordByname(ScanType scanType){
        return scanTypeDAO.findScanTypeByName(scanType.getName());
    }

    @Override
    public ScanType fetchRecordById(long id){
        return scanTypeDAO.get(id);
    }

    @Override
    public ScanType createRecord(ScanType ScanType) {
       long id =  scanTypeDAO.insert(ScanType);
       return scanTypeDAO.findScanTypeById(id);
    }
    @Override
    public void updateRecord(ScanType ScanType){
        scanTypeDAO.update(ScanType);
    }
    @Override
    public void deleteRecord(long id){
        scanTypeDAO.delete(id);
    }
}
