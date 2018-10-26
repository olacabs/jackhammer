package com.olacabs.jackhammer.service;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.olacabs.jackhammer.common.Constants;
import com.olacabs.jackhammer.db.UploadDAO;
import com.olacabs.jackhammer.models.PagedResponse;
import com.olacabs.jackhammer.models.Upload;
import com.olacabs.jackhammer.models.User;
import lombok.extern.slf4j.Slf4j;

import java.util.List;


@Slf4j
public class UploadDataService extends AbstractDataService<Upload> {

    @Inject
    @Named(Constants.UPLOAD_DAO)
    UploadDAO uploadDAO;

    @Override
    public PagedResponse<Upload> getAllRecords(Upload upload) {

        List<Upload> uploadList =  uploadDAO.getAll(upload);
        for(Upload eachUpload : uploadList) {
            User user = userDAOJdbi.get(eachUpload.getUserId());
            eachUpload.setUserName(user.getName());
        }
        paginationRecords.setItems(uploadList);
        return paginationRecords;
    }

    @Override
    public Upload fetchRecordByname(Upload upload){
        return null;
    }

    @Override
    public Upload fetchRecordById(long id){
        return uploadDAO.get(id);
    }

    @Override
    public Upload createRecord(Upload upload) {
        int id = uploadDAO.insert(upload);
        return uploadDAO.get(id);
    }

    @Override
    public void updateRecord(Upload upload){

    }
    @Override
    public void deleteRecord(long id){
        uploadDAO.delete(id);
    }
}
