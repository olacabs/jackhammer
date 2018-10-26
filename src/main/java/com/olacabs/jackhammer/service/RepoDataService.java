package com.olacabs.jackhammer.service;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.olacabs.jackhammer.common.Constants;
import com.olacabs.jackhammer.db.GroupDAO;
import com.olacabs.jackhammer.db.RepoDAO;
import com.olacabs.jackhammer.models.Group;
import com.olacabs.jackhammer.models.PagedResponse;
import com.olacabs.jackhammer.models.Repo;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class RepoDataService extends AbstractDataService<Repo> {

    @Inject
    @Named(Constants.REPO_DAO)
    RepoDAO repoDAO;

    @Inject
    @Named(Constants.GROUP_DAO)
    GroupDAO groupDAO;

    @Override
    public PagedResponse<Repo> getAllRecords(Repo repo) {
        //listing repos
        if (repo.getLimit() > 0) {
            //with no searching filter
            if (repo.getSearchTerm() == null) {
                getRepos(repo);
            } else {
                getSearchReposResults(repo);
            }
            Group group = groupDAO.get(repo.getGroupId());
            paginationRecords.setItem(group);
        } else {
            //drop down values in other pages
            getRepoDropDownValues(repo);
        }
        return paginationRecords;
    }

    @Override
    public Repo fetchRecordByname(Repo repo) {
        return repoDAO.findRepoByName(repo.getName());
    }

    @Override
    public Repo fetchRecordById(long id) {
        return repoDAO.get(id);
    }

    @Override
    public Repo createRecord(Repo repo) {
        long repoId = repoDAO.insert(repo);
        return repoDAO.get(repoId);
    }

    @Override
    public void updateRecord(Repo repo) {
        repoDAO.update(repo);
    }

    @Override
    public void deleteRecord(long id) {
        repoDAO.delete(id);
    }

    private void getRepos(Repo repo) {
        if (repo.getGroupId() != 0) {
            paginationRecords.setItems(repoDAO.getApplicationRepos(repo, repo.getOrderBy(), repo.getSortDirection()));
            paginationRecords.setTotal(repoDAO.groupRepoCount(repo));
        } else {
            paginationRecords.setItems(repoDAO.getPersonalRepos(repo, repo.getOrderBy(), repo.getSortDirection()));
            paginationRecords.setTotal(repoDAO.getPersonalRepoCount(repo));
        }
    }

    private void getSearchReposResults(Repo repo) {
        if (repo.getGroupId() != 0) {
            paginationRecords.setItems(repoDAO.getReposSearchResult(repo, repo.getOrderBy(), repo.getSortDirection()));
            paginationRecords.setTotal(repoDAO.totalSearchCount(repo));
        } else {
            paginationRecords.setItems(repoDAO.getPersonalRepoSearchResult(repo, repo.getOrderBy(), repo.getSortDirection()));
            paginationRecords.setTotal(repoDAO.totalPersonalRepoSearchCount(repo));
        }
    }

    private void getRepoDropDownValues(Repo repo) {
        //drop down values in other pages
        if (repo.getGroupId() == 0) {
            List<Repo> repoList = new ArrayList();
            if (repo.getGroupIds().size() > 0) repoList = repoDAO.getRepos(repo.getGroupIds());
            paginationRecords.setItems(repoList);
        } else {
            paginationRecords.setItems(repoDAO.getAll(repo));
            paginationRecords.setTotal(repoDAO.totalCount());
        }
    }
}
