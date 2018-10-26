package com.olacabs.jackhammer.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.io.Serializable;
import java.sql.Timestamp;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(value=Include.NON_NULL)
public class AbstractModel  implements Serializable {

    protected long id;
    protected String name;
    protected Timestamp createdAt;
    protected Timestamp updatedAt;

    private String userToken;

    //pagination fields
    private long totalSize;
    private long offset;
    private long limit;
    private long taskId;
    private long ownerTypeId;
    private long scanTypeId;
    private String orderBy;
    private String sortDirection;
    private String searchTerm;
    private Boolean createAllowed = false;
    private Boolean readAllowed = false;
    private Boolean updateAllowed = false;
    private Boolean deleteAllowed = false;
    private User user;
}

