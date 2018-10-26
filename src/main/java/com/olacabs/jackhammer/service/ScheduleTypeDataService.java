package com.olacabs.jackhammer.service;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.olacabs.jackhammer.common.Constants;
import com.olacabs.jackhammer.db.ScheduleTypeDAO;
import com.olacabs.jackhammer.models.PagedResponse;
import com.olacabs.jackhammer.models.ScheduleType;

import java.util.List;

public class ScheduleTypeDataService extends AbstractDataService<ScheduleType> {

    @Inject
    @Named(Constants.SCHEDULE_TYPE_DAO)
    ScheduleTypeDAO scheduleTypeDAO;
    @Override
    public PagedResponse<ScheduleType> getAllRecords(ScheduleType scheduleType) {
        paginationRecords.setItems(scheduleTypeDAO.getAll());
//        setCRUDPermissions(paginationRecords,scheduleType,null);
        setOwnerAndScanType(paginationRecords,scheduleType);
        return paginationRecords;
    }
    @Override
    public ScheduleType fetchRecordByname(ScheduleType scheduleType){
        return scheduleTypeDAO.findScheduleByName(scheduleType.getName());
    }
    @Override
    public ScheduleType fetchRecordById(long id){
        return scheduleTypeDAO.get(id);
    }
    @Override
    public ScheduleType createRecord(ScheduleType scheduleType) {
        return scheduleTypeDAO.save(scheduleType);
    }
    @Override
    public void updateRecord(ScheduleType scheduleType){
        scheduleTypeDAO.update(scheduleType);
    }
    @Override
    public void deleteRecord(long id){
        scheduleTypeDAO.delete(id);
    }
}
