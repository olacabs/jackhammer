package com.olacabs.jackhammer.service;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.olacabs.jackhammer.common.Constants;
import com.olacabs.jackhammer.configuration.JackhammerConfiguration;
import com.olacabs.jackhammer.db.HardcodeSecretDAO;
import com.olacabs.jackhammer.models.PagedResponse;
import com.olacabs.jackhammer.models.HardcodeSecret;

public class HardcodeSecretDataService extends AbstractDataService<HardcodeSecret> {

    @Inject
    @Named(Constants.HARDCODE_SECRET_DAO)
    HardcodeSecretDAO hardcodeSecretDAO;

    @Inject
    JackhammerConfiguration jackhammerConfiguration;

    public PagedResponse<HardcodeSecret> getAllRecords(HardcodeSecret hardcodeSecret) {
        return null;
    }

    public HardcodeSecret createRecord(HardcodeSecret hardcodeSecret) {
        HardcodeSecret dbHardcodeSecret = hardcodeSecretDAO.get();
        if (dbHardcodeSecret == null) {
            hardcodeSecretDAO.insert(hardcodeSecret);
        }
        return dbHardcodeSecret;
    }

    public HardcodeSecret fetchRecordByname(HardcodeSecret model) {
        return null;
    }

    public HardcodeSecret fetchRecordById(long id) {
        HardcodeSecret hardcodeSecret = hardcodeSecretDAO.get();
        return hardcodeSecret;
    }

    public void updateRecord(HardcodeSecret hardcodeSecret) {
        hardcodeSecretDAO.update(hardcodeSecret);
    }

    public void deleteRecord(long id) {

    }
}
