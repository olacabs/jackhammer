package com.olacabs.jackhammer.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PagedResponse<T> {
    private  List<T> items;
    private  long total;
    private T item;
    private OwnerType ownerType;
    private ScanType scanType;
    private Boolean createAllowed = false;
    private Boolean readAllowed = false;
    private Boolean updateAllowed = false;
    private Boolean deleteAllowed = false;
    private Boolean readFindingsAllowed = false;
}
