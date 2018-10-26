package com.olacabs.jackhammer.models;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Group extends AbstractModel {
    private SeverityCount severityCount;
    private Boolean isDefault;
    private List<Role> roles = Lists.newArrayList();
    private List<Long> roleIds = Lists.newArrayList();

    public void addRole(Role p) {
        if (!roles.contains(p)) roles.add(p);
    }

}
