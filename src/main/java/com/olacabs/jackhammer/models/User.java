package com.olacabs.jackhammer.models;

import com.fasterxml.jackson.annotation.JsonInclude;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.olacabs.jackhammer.common.Constants;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Timestamp;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Slf4j
public class User extends AbstractModel {

    private String email;
    private String password;
    private String newPassword;
    private Boolean allowedExecutiveDashboard;
    private Boolean allowedCorporateDashboard;
    private Boolean allowedTeamDashboard;
    private Boolean allowedPersonalDashboard;

    private List<OwnerType> ownerTypes = Lists.newArrayList();
    private List<Group> groups = Lists.newArrayList();
    private List<Long> groupIds = Lists.newArrayList();
    private List<Role> roles = Lists.newArrayList();
    private List<Long> roleIds = Lists.newArrayList();

    public void addGroup(Group g) {
        if (!groups.contains(g)) groups.add(g);
    }

    public void addRole(Role r) {
        if (!roles.contains(r)) {
            roles.add(r);
        }
    }

}
