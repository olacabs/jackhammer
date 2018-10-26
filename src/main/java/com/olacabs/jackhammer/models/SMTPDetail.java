package com.olacabs.jackhammer.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SMTPDetail extends AbstractModel {
    private String applicationUrl;
    private String smtpHost;
    private String smtpUserName;
    private String smtpPassword;
    private int smtpPort;
}
