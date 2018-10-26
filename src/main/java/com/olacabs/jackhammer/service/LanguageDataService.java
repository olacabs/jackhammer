package com.olacabs.jackhammer.service;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.olacabs.jackhammer.common.Constants;
import com.olacabs.jackhammer.db.LanguageDAO;
import com.olacabs.jackhammer.models.Language;
import com.olacabs.jackhammer.models.PagedResponse;

public class LanguageDataService extends AbstractDataService<Language>  {

    @Inject
    @Named(Constants.LANGUAGE_DAO)
    LanguageDAO languageDAO;

    @Override
    public PagedResponse<Language> getAllRecords(Language language) {

        if(language.getLimit() == -1) {
            paginationRecords.setItems(languageDAO.getDropdownValues());
        }
        else if(language.getSearchTerm() == null) {
            paginationRecords.setItems(languageDAO.getAll(language,language.getOrderBy(),language.getSortDirection()));
            paginationRecords.setTotal(languageDAO.totalCount(language));
        }
        else {
            paginationRecords.setItems(languageDAO.getSearchResults(language,language.getOrderBy(),language.getSortDirection()));
            paginationRecords.setTotal(languageDAO.totalSearchCount(language));
        }
        return paginationRecords;

    }

    @Override
    public Language fetchRecordByname(Language language){
        return languageDAO.findLanguageByName(language.getName());
    }

    @Override
    public Language fetchRecordById(long id){
        return languageDAO.get(id);
    }

    @Override
    public Language createRecord(Language language) {
        long insertedLanguageId = languageDAO.insert(language);
        return languageDAO.get(insertedLanguageId);
    }

    @Override
    public void updateRecord(Language language){
        languageDAO.update(language);
    }

    @Override
    public void deleteRecord(long id){
        languageDAO.delete(id);
    }

}
