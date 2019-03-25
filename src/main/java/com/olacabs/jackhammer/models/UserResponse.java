package com.olacabs.jackhammer.models;

import com.google.common.collect.Lists;
import lombok.*;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class UserResponse extends AbstractResponse {
    private String username;
    private String email;
    private String userToken;
    private Boolean allowedExecutiveDashboard;
    private Boolean allowedCorporateDashboard;
    private Boolean allowedTeamDashboard;
    private Boolean allowedPersonalDashboard;
    private List<OwnerType> ownerTypes;
}
