package com.olacabs.jackhammer.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JwtToken  extends AbstractModel {

    private Date tokenValidFrom;
    private Date tokenValidUntil;
    private String token;
    private Long userId;
    private boolean Deleted;
    private Long version;
}
