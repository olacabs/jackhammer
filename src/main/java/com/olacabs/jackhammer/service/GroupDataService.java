package com.olacabs.jackhammer.service;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.olacabs.jackhammer.common.Constants;
import com.olacabs.jackhammer.db.GroupDAO;
import com.olacabs.jackhammer.models.*;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class GroupDataService extends AbstractDataService<Group> {
    @Inject
    @Named(Constants.GROUP_DAO_JDBI)
    GroupDAO groupDAOJdbi;

    @Inject
    @Named(Constants.GROUP_DAO)
    GroupDAO groupDAO;

    @Override
    public PagedResponse<Group> getAllRecords(Group group) {
        setCRUDPermissions(paginationRecords, group,getCurrentTask(Constants.GROUPS,0));
        setOwnerAndScanType(paginationRecords, group);
        if (group.getLimit() == -1) {
            if (group.getScanTypeId() == 0 || fetchAllGroups(paginationRecords)) {
                paginationRecords.setItems(groupDAOJdbi.getAll());
            } else {
                List<Group> groupList = group.getUser().getGroups();
                if(!paginationRecords.getScanType().getIsStatic() && !paginationRecords.getScanType().getIsHardCodeSecret()) {
                    Group additionalGroup = groupDAO.getByScanTypeId(group.getScanTypeId());
                    additionalGroup.setIsDefault(true);
                    groupList.add(additionalGroup);
                }
                paginationRecords.setItems(groupList);
            }
        } else if (group.getSearchTerm() == null) {
            paginationRecords.setItems(groupDAOJdbi.getAll(group, group.getOrderBy(), group.getSortDirection()));
            paginationRecords.setTotal(groupDAOJdbi.totalCount());
        } else {
            paginationRecords.setItems(groupDAOJdbi.getSearchResults(group, group.getOrderBy(), group.getSortDirection()));
            paginationRecords.setTotal(groupDAOJdbi.totalSearchCount(group));
        }
        return paginationRecords;
    }

    @Override
    public Group fetchRecordByname(Group group) {
        return groupDAOJdbi.findGroupByName(group.getName());
    }

    @Override
    public Group fetchRecordById(long id) {
        return groupDAOJdbi.get(id);
    }

    @Override
    public Group createRecord(Group Group) {

        List<Long> roleIds = Group.getRoleIds();
        List<Role> roleList = Lists.newArrayList();
        for (Long roleId : roleIds) {
            Role role = roleDAOJdbi.get(roleId);
            roleList.add(role);
        }
        Group.setRoles(roleList);
        return groupDAOJdbi.save(Group);
    }

    @Override
    public void updateRecord(Group Group) {

        List<Long> permissionIds = Group.getRoleIds();
        List<Role> roleList = Lists.newArrayList();
        for (Long permissionId : permissionIds) {
            Role role = roleDAOJdbi.get(permissionId);
            roleList.add(role);
        }
        Group.setRoles(roleList);
        groupDAOJdbi.update(Group);
    }

    @Override
    public void deleteRecord(long id) {
        groupDAOJdbi.delete(id);
    }

    private Boolean fetchAllGroups(PagedResponse pagedResponse) {
        return ((pagedResponse.getScanType().getIsStatic()
                || pagedResponse.getScanType().getIsHardCodeSecret())
                && pagedResponse.getOwnerType().getIsDefault());
    }
}
