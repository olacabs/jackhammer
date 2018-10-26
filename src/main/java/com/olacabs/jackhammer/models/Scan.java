package com.olacabs.jackhammer.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.nio.file.Path;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipInputStream;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Scan extends AbstractModel {
    private String target;
    private String branch;
    private String status;
    private String statusReason;
    private Timestamp startTime;
    private Timestamp endTime;
    private long scheduleTypeId;
    private long repoId;
    private long userId;
    private long groupId;
    private long criticalCount;
    private long highCount;
    private long mediumCount;
    private long lowCount;
    private long infoCount;
    private long toolInstanceId;
    private boolean supported;
    private boolean cloneRequired;
    private boolean isAccessible;
    private Boolean isTaggedTools;
    private OwnerType ownerTypeRecord;
    private ScanType scanTypeRecord;
    private String scanPlatforms;
    private InputStream apkFile;
    private String apkTempFile;
    private Boolean isMobileScan;
    private Date lastRunDate;
    private List<Language> languageList = Lists.newArrayList();
    private List<Tool> toolList = Lists.newArrayList();
    private List<Long> groupIds = Lists.newArrayList();
    private List<Long> repoIds = Lists.newArrayList();
    private List<Finding> findingList = Lists.newArrayList();
    private Set<String> platforms = new HashSet<String>();

    public void addTool(Tool tool) {
        if (!toolList.contains(tool)) toolList.add(tool);
    }
}
