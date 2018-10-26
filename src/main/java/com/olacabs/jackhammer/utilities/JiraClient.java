package com.olacabs.jackhammer.utilities;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import com.olacabs.jackhammer.common.Constants;
import com.olacabs.jackhammer.configuration.JackhammerConfiguration;
import com.olacabs.jackhammer.db.JiraDetailDAO;
import com.olacabs.jackhammer.db.RepoDAO;
import com.olacabs.jackhammer.models.*;
import com.olacabs.jackhammer.security.AES;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

@Slf4j
public class JiraClient {

    @Inject
    @Named(Constants.JIRA_DETAIL_DAO)
    JiraDetailDAO jiraDetailDAO;

    @Inject
    @Named(Constants.REPO_DAO)
    RepoDAO repoDAO;

    @Inject
    JackhammerConfiguration jackhammerConfiguration;

    public void createIssue(Finding finding) {
        JiraDetail jiraDetail = jiraDetailDAO.get();
        Repo repo = repoDAO.findRepoById(finding.getId());
        WebTarget webTarget = getWebTarget(jiraDetail);
        JiraIssue jiraIssue = getJiraIssue(jiraDetail, repo, finding);
        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
        invocationBuilder.post(Entity.entity(jiraIssue, MediaType.APPLICATION_JSON));
    }

    private WebTarget getWebTarget(JiraDetail jiraDetail) {
        String userName = jiraDetail.getUserName();
        String password = AES.decrypt(jiraDetail.getPassword(), jackhammerConfiguration.getJwtConfiguration().getTokenSigningKey());
        HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic(userName, password);
        WebTarget webTarget = ClientBuilder.newClient().target(jiraDetail.getHost() + Constants.JIRA_END_POINT)
                .register(feature);
        return webTarget;
    }

    private JiraIssue getJiraIssue(JiraDetail jiraDetail, Repo repo, Finding finding) {
        JiraIssue jiraIssue = new JiraIssue();
        jiraIssue.setFields(getJiraField(jiraDetail, repo, finding));
        return jiraIssue;
    }

    private JiraField getJiraField(JiraDetail jiraDetail, Repo repo, Finding finding) {
        JiraField jiraField = new JiraField();
        jiraField.setProject(getJiraProject(jiraDetail));
        jiraField.setIssuetype(getJiraIssueType());
        String summary;
        if (repo != null && repo.getName() != null) {
            summary = repo.getName() + "-" + finding.getName();
        } else {
            summary = finding.getName();
        }
        jiraField.setSummary(summary);
        jiraField.setDescription(getIssueBody(finding, repo));
        return jiraField;
    }

    private JiraProject getJiraProject(JiraDetail jiraDetail) {
        JiraProject jiraProject = new JiraProject();
        jiraProject.setKey(jiraDetail.getDefaultProject());
        return jiraProject;
    }

    private JiraIssueType getJiraIssueType() {
        JiraIssueType jiraIssueType = new JiraIssueType();
        jiraIssueType.setName(Constants.JIRA_ISSUE_TYPE);
        return jiraIssueType;
    }

    private String getIssueBody(Finding finding, Repo repo) {
        StringBuilder issueBody = new StringBuilder();
        String applicationName = repo != null ? repo.getName() : Constants.EMPTY_STRING;
        String repoUrl = repo != null ? repo.getTarget() : Constants.EMPTY_STRING;

        issueBody.append(Constants.FINDING_APPLICATION_NAME + Constants.COLON);
        issueBody.append(applicationName);
        issueBody.append(Constants.JIRA_LINE_BREAK);
        issueBody.append(Constants.REPO_URL + Constants.COLON);
        issueBody.append(repoUrl);
        if (StringUtils.equals(finding.getToolName(), Constants.NMAP)) {
            issueBody.append(Constants.SERVICE + Constants.COLON);
        } else {
            issueBody.append(Constants.VULNERABILITY_TYPE + Constants.COLON);
        }
        issueBody.append(finding.getName());
        issueBody.append(Constants.JIRA_LINE_BREAK);
        issueBody.append(Constants.SEVERITY + Constants.COLON);
        issueBody.append(finding.getSeverity());
        issueBody.append(Constants.JIRA_LINE_BREAK);

        if (finding.getSolution() != null) {
            issueBody.append(Constants.SOLUTION + Constants.COLON);
            issueBody.append(finding.getSolution());
            issueBody.append(Constants.JIRA_LINE_BREAK);
        }
        if (finding.getLocation() != null) {
            issueBody.append(Constants.LOCATION + Constants.COLON);
            issueBody.append(finding.getLocation());
            issueBody.append(Constants.JIRA_LINE_BREAK);
        }
        if (finding.getUserInput() != null) {
            issueBody.append(Constants.USER_INPUT + Constants.COLON);
            issueBody.append(finding.getUserInput());
            issueBody.append(Constants.JIRA_LINE_BREAK);
        }
        if (finding.getFileName() != null) {
            issueBody.append(Constants.FILE_NAME + Constants.COLON);
            issueBody.append(finding.getFileName());
            issueBody.append(Constants.JIRA_LINE_BREAK);
        }
        if (finding.getLineNumber() != null) {
            issueBody.append(Constants.LINE_NUMBER + Constants.COLON);
            issueBody.append(finding.getLineNumber());
            issueBody.append(Constants.JIRA_LINE_BREAK);
        }
        if (finding.getCode() != null) {
            issueBody.append(Constants.CODE + Constants.COLON);
            issueBody.append(finding.getCode());
            issueBody.append(Constants.JIRA_LINE_BREAK);
        }
        if (finding.getAdvisory() != null) {
            issueBody.append(Constants.ADVISORY + Constants.COLON);
            issueBody.append(finding.getAdvisory());
            issueBody.append(Constants.JIRA_LINE_BREAK);
        }
        if (finding.getCvssScore() != null) {
            issueBody.append(Constants.CVSS_SCORE + Constants.COLON);
            issueBody.append(finding.getCvssScore());
            issueBody.append(Constants.JIRA_LINE_BREAK);
        }
        if (finding.getExternalLink() != null) {
            issueBody.append(Constants.EXTERNAL_LINK + Constants.COLON);
            issueBody.append(finding.getExternalLink());
            issueBody.append(Constants.JIRA_LINE_BREAK);
        }
        if (finding.getCweCode() != null) {
            issueBody.append(Constants.CWE_CODE + Constants.COLON);
            issueBody.append(finding.getCweCode());
            issueBody.append(Constants.JIRA_LINE_BREAK);
        }
        if (finding.getFingerprint() != null) {
            issueBody.append(Constants.FINGERPRINT + Constants.COLON);
            issueBody.append(finding.getFingerprint());
            issueBody.append(Constants.JIRA_LINE_BREAK);
        }

        if (finding.getHost() != null) {
            issueBody.append(Constants.HOST + Constants.COLON);
            issueBody.append(finding.getHost());
            issueBody.append(Constants.JIRA_LINE_BREAK);
        }
        if (finding.getProduct() != null) {
            issueBody.append(Constants.PRODUCT + Constants.COLON);
            issueBody.append(finding.getProduct());
            issueBody.append(Constants.JIRA_LINE_BREAK);
        }

        if (finding.getProtocol() != null) {
            issueBody.append(Constants.PROTOCOL + Constants.COLON);
            issueBody.append(finding.getProtocol());
            issueBody.append(Constants.JIRA_LINE_BREAK);
        }

        if (finding.getState() != null) {
            issueBody.append(Constants.STATE + Constants.COLON);
            issueBody.append(finding.getState());
            issueBody.append(Constants.JIRA_LINE_BREAK);
        }

        if (finding.getPort() != null) {
            issueBody.append(Constants.PORT + Constants.COLON);
            issueBody.append(finding.getPort());
            issueBody.append(Constants.JIRA_LINE_BREAK);
        }

        if (finding.getVersion() != null) {
            issueBody.append(Constants.VERSION + Constants.COLON);
            issueBody.append(finding.getVersion());
            issueBody.append(Constants.JIRA_LINE_BREAK);
        }

        if (finding.getScripts() != null) {
            issueBody.append(Constants.SCRIPTS + Constants.COLON);
            issueBody.append(finding.getScripts());
            issueBody.append(Constants.JIRA_LINE_BREAK);
        }

        if (finding.getRequest() != null) {
            issueBody.append(Constants.REQUEST + Constants.COLON);
            issueBody.append(finding.getRequest());
            issueBody.append(Constants.JIRA_LINE_BREAK);
        }

        if (finding.getResponse() != null) {
            issueBody.append(Constants.RESPONSE + Constants.COLON);
            issueBody.append(finding.getResponse());
            issueBody.append(Constants.JIRA_LINE_BREAK);
        }
        issueBody.append(Constants.JACKHAMMER_URL + Constants.COLON);
        issueBody.append(jackhammerConfiguration.getClientConfiguration().getClientUrl());
        issueBody.append(Constants.CLIENT_FIDNING_URL);
        issueBody.append(finding.getScanId());
        issueBody.append(Constants.URL_SEPARATOR);
        issueBody.append(finding.getId());
        return issueBody.toString();
    }
}
