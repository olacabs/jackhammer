package com.olacabs.jackhammer.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Filter extends AbstractModel {

    //dropdown values
    private List<Tag> tags;
    private List<Group> groups;
    private List<Repo> repos;
    private List<Tool> tools;
    private List<VulnerableType> vulnerableTypes;
    private String tool;

    //selected values
    private List<Long> tagIds = new ArrayList<Long>();
    private List<Long> groupIds = new ArrayList<Long>();
    private List<Long> repoIds = new ArrayList<Long>();
    private List<String> toolNames = new ArrayList<String>();
    private List<String> vulnerabilities = new ArrayList<String>();
    private String status;
    private String severity;
    private long aging;
    private Date fromDate;
    private Date toDate;

    //other fields
    private long userId;
}
