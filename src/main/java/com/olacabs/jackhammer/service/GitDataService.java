package com.olacabs.jackhammer.service;

import com.olacabs.jackhammer.configuration.JackhammerConfiguration;
import com.olacabs.jackhammer.security.AES;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.olacabs.jackhammer.common.Constants;
import com.olacabs.jackhammer.db.GitDAO;
import com.olacabs.jackhammer.models.Git;
import com.olacabs.jackhammer.models.PagedResponse;
import com.olacabs.jackhammer.utilities.GitUtil;


public class GitDataService extends AbstractDataService<Git> {

    @Inject
    @Named(Constants.GIT_DAO)
    GitDAO gitDAO;

    @Inject
    GitUtil gitUtil;

    @Inject
    JackhammerConfiguration jackhammerConfiguration;

    @Override
    public PagedResponse<Git> getAllRecords(Git model) {
        return null;
    }

    @Override
    public Git createRecord(Git git) {
        Git dbGit = gitDAO.get();
        if (dbGit == null) {
            git.setApiAccessToken(AES.encrypt(git.getApiAccessToken(),jackhammerConfiguration.getJwtConfiguration().getTokenSigningKey()));
            gitDAO.insert(git);
            gitUtil.pullGitLabInfo();
        }
        return dbGit;
    }

    @Override
    public Git fetchRecordByname(Git model) {
        return null;
    }

    @Override
    public Git fetchRecordById(long id) {
        Git git = gitDAO.get();
        if(git!=null) git.setApiAccessToken(AES.decrypt(git.getApiAccessToken(),jackhammerConfiguration.getJwtConfiguration().getTokenSigningKey()));
        return git;
    }

    @Override
    public void updateRecord(Git git) {
        git.setApiAccessToken(AES.encrypt(git.getApiAccessToken(),jackhammerConfiguration.getJwtConfiguration().getTokenSigningKey()));
        gitDAO.update(git);
    }

    @Override
    public void deleteRecord(long id) {

    }
}
