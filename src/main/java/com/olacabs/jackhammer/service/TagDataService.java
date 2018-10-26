package com.olacabs.jackhammer.service;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import com.olacabs.jackhammer.application.DBFactory;
import com.olacabs.jackhammer.common.Constants;
import com.olacabs.jackhammer.db.FindingDAO;
import com.olacabs.jackhammer.db.FindingTagDAO;
import com.olacabs.jackhammer.db.TagDAO;
import com.olacabs.jackhammer.models.*;

import org.skife.jdbi.v2.Handle;
import org.skife.jdbi.v2.TransactionCallback;
import org.skife.jdbi.v2.TransactionStatus;

import java.util.List;


public class TagDataService extends AbstractDataService<Tag> {


    @Inject
    @Named(Constants.TAG_DAO)
    TagDAO tagDAO;

    @Inject
    @Named(Constants.FINDING_DAO)
    FindingDAO findingDAO;

    @Inject
    @Named(Constants.FINDING_TAG_DAO)
    FindingTagDAO findingTagDAO;

    @Override
    public PagedResponse<Tag> getAllRecords(Tag tag) {
        paginationRecords.setItems(getAll(tag));
        return paginationRecords;
    }

    @Override
    public Tag fetchRecordByname(Tag tag){
        return null;
    }

    @Override
    public Tag fetchRecordById(long id){
        return null;
    }

    @Override
    public Tag createRecord(Tag tag) {
        List<String> tagList = tag.getTagList();
        Tag lastTag = null;
        Finding finding = findingDAO.get(tag.getFindingId());
        finding.setUserId(tag.getUserId());
        finding.setTagNames(tagList);
        deleteFindingTags(finding);
        updatetWithTxCallback(finding);
        return lastTag;
    }
    @Override
    public void updateRecord(Tag tag){
        tagDAO.update(tag);
    }
    @Override
    public void deleteRecord(long id){
        roleDAOJdbi.delete(id);
    }

    public List<Tag> getAll(Tag tag) {
        if(tag.getFindingId() == -1) return tagDAO.getFindingTags();
        Finding finding = findingDAO.get(tag.getFindingId());
        getFindingTag(finding);
        return finding.getTags();
    }

    public long updatetWithTxCallback(final Finding finding) {
        return DBFactory.getDBI().inTransaction(new TransactionCallback<Long>() {
            public Long inTransaction(Handle handle, TransactionStatus status) throws Exception {
                FindingTagDAO findingTagDAO = handle.attach(FindingTagDAO.class);
                for (String tagName : finding.getTagNames()) {
                    Tag tag = tagDAO.findTagByName(tagName);
                    if(tag == null) {
                        Tag newTag = new Tag();
                        newTag.setName(tagName);
                        newTag.setUserId(finding.getUserId());
                        tagDAO.insert(newTag);
                        tag = tagDAO.findTagByName(tagName);
                    }
                    findingTagDAO.insert(new FindingTag(finding.getId(), tag.getId()));
                }
                return finding.getId();
            }
        });
    }

    private void getFindingTag(Finding finding) {
        List<FindingTag> findingTagList = findingTagDAO.findByFindingId(finding.getId());
        for (FindingTag findingTag : findingTagList) {
            finding.addTag(tagDAO.get(findingTag.getTagId()));
        }
    }
    private void deleteFindingTags(Finding finding) {
        findingTagDAO.deleteByFindingId(finding.getId());
    }
}
