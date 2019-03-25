package com.olacabs.jackhammer.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Role extends AbstractModel {

    private List<Task> tasks = Lists.newArrayList();

    private List<Long> taskIds = Lists.newArrayList();


    //permissions are deprecated
    private List<Permission> permissions = Lists.newArrayList();

    private List<Long> permissionIds = Lists.newArrayList();

    public void addTask(Task t) {
        if (!tasks.contains(t)) tasks.add(t);
    }

    public void addPermission(Permission p) {
        if (!permissions.contains(p)) permissions.add(p);
    }

}