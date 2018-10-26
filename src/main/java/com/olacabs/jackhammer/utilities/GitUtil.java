package com.olacabs.jackhammer.utilities;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import com.olacabs.jackhammer.configuration.JackhammerConfiguration;
import com.olacabs.jackhammer.models.*;
import com.olacabs.jackhammer.security.AES;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;


import com.olacabs.jackhammer.db.*;
import com.olacabs.jackhammer.common.Constants;
import org.apache.commons.lang3.StringUtils;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;


@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class GitUtil {

    @Inject
    @Named(Constants.GROUP_DAO)
    GroupDAO groupDAO;


    @Inject
    @Named(Constants.REPO_DAO)
    RepoDAO repoDAO;

    @Inject
    @Named(Constants.SCAN_TYPE_DAO)
    ScanTypeDAO scanTypeDAO;

    @Inject
    @Named(Constants.OWNER_TYPE_DAO)
    OwnerTypeDAO ownerTypeDAO;

    @Inject
    @Named(Constants.GIT_DAO)
    GitDAO gitDAO;

    @Inject
    JackhammerConfiguration jackhammerConfiguration;

    public void pullGitRepos() {
        try {
            Git git = gitDAO.get();
            if (StringUtils.equals(git.getGitType().toLowerCase(), Constants.GITHUB)) {
                pullGitHubInfo();
            } else if (StringUtils.equals(git.getGitType().toLowerCase(), Constants.GITLAB)) {
                pullGitLabInfo();
            }
        } catch (Throwable t) {
            log.info("Exception while getting repos ", t);
        }
    }

    public void pullGitLabInfo() {
        List<GitLabGroup> gitlabGroups = getGitLabGroups();
        try {
            OwnerType ownerType = ownerTypeDAO.getDefaultOwnerType();
            ScanType scanType = scanTypeDAO.getStaticScanType();
            for (GitLabGroup gitLabGroup : gitlabGroups) {
                Group group = groupDAO.findGroupByName(gitLabGroup.getName());
                long groupId = 0;
                if (group == null) {
                    Group newGroup = new Group();
                    newGroup.setName(gitLabGroup.getName());
                    groupId = groupDAO.insert(newGroup);
                }
                for (GitLabProject gitLabProject : gitLabGroup.getGitLabProjects()) {
                    Repo repo = repoDAO.findRepoByName(gitLabProject.getName());
                    if (repo == null) {
                        Repo newRepo = new Repo();
                        newRepo.setName(gitLabProject.getName());
                        newRepo.setGroupId(groupId);
                        newRepo.setOwnerTypeId(ownerType.getId());
                        newRepo.setScanTypeId(scanType.getId());
                        newRepo.setTarget(gitLabProject.getHttp_url_to_repo());
                        repoDAO.insert(newRepo);
                    }
                }
            }
        } catch (Throwable t) {
            log.error("Exception while reading fetched groups", t);
        }
    }

    public void pullGitHubInfo() {
        List<GitHubGroup> gitHubGroups = getGiHubGroups();
        try {
            OwnerType ownerType = ownerTypeDAO.getDefaultOwnerType();
            ScanType scanType = scanTypeDAO.getStaticScanType();
            for (GitHubGroup gitHubGroup : gitHubGroups) {
                Group group = groupDAO.findGroupByName(gitHubGroup.getName());
                long groupId = 0;
                if (group == null) {
                    Group newGroup = new Group();
                    newGroup.setName(gitHubGroup.getName());
                    groupId = groupDAO.insert(newGroup);
                }
                for (GitHubProject gitHubProject : gitHubGroup.getGitHubProjects()) {
                    Repo repo = repoDAO.findRepoByName(gitHubProject.getName());
                    if (repo == null) {
                        Repo newRepo = new Repo();
                        newRepo.setName(gitHubProject.getName());
                        newRepo.setGroupId(groupId);
                        newRepo.setOwnerTypeId(ownerType.getId());
                        newRepo.setScanTypeId(scanType.getId());
                        newRepo.setTarget(gitHubProject.getHtml_url());
                        repoDAO.insert(newRepo);
                    }
                }
            }
        } catch (Throwable t) {
            log.error("Exception while reading fetched groups", t);
        }
    }

    private List<GitLabGroup> getGitLabGroups() {
        List<GitLabGroup> gitLabGroups = new ArrayList<GitLabGroup>();
        Git git = gitDAO.get();
        if (git != null) {
            int page = 1;
            try {
                String privateToken = AES.decrypt(git.getApiAccessToken(), jackhammerConfiguration.getJwtConfiguration().getTokenSigningKey());
                while (true) {
                    WebTarget webTarget = ClientBuilder.newClient()
                            .target(git.getGitEndPoint() + Constants.GIT_LAB_GROUPS_END_POINT)
                            .queryParam(Constants.PRIVATE_TOKEN, privateToken)
                            .queryParam(Constants.ALL_AVAILABLE, true)
                            .queryParam(Constants.PAGE, page);
                    Response response = webTarget.request(MediaType.APPLICATION_JSON).get();
                    List<GitLabGroup> fetchedGroups = response.readEntity(new GenericType<List<GitLabGroup>>() {
                    });
                    if (fetchedGroups.size() == 0) break;
                    gitLabGroups.addAll(fetchedGroups);
                    page += 1;
                }

                for (GitLabGroup gitLabGroup : gitLabGroups) {
                    StringBuilder endPointBuilder = new StringBuilder();

                    endPointBuilder.append(git.getGitEndPoint());
                    endPointBuilder.append(Constants.GIT_LAB_GROUPS_END_POINT);
                    endPointBuilder.append(gitLabGroup.getId());
                    endPointBuilder.append(Constants.GIT_PROJECTS_END_POINT);

                    WebTarget webTarget = ClientBuilder.newClient()
                            .target(endPointBuilder.toString())
                            .queryParam(Constants.PRIVATE_TOKEN, privateToken);
                    Response response = webTarget.request(MediaType.APPLICATION_JSON).get();
                    List<GitLabProject> fetchedProjects = response.readEntity(new GenericType<List<GitLabProject>>() {
                    });
                    gitLabGroup.setGitLabProjects(fetchedProjects);
                }
            } catch (Throwable ex) {
                log.error("Throwable Exception while fetching groups ", ex);
            }
        }
        return gitLabGroups;
    }


    private List<GitHubGroup> getGiHubGroups() {
        List<GitHubGroup> gitHubGroups = new ArrayList<GitHubGroup>();
        Git git = gitDAO.get();
        if (git != null) {
            int page = 1;
            try {
                String privateToken = AES.decrypt(git.getApiAccessToken(), jackhammerConfiguration.getJwtConfiguration().getTokenSigningKey());
                while (true) {
                    StringBuilder webTargetBuilder = new StringBuilder();

                    webTargetBuilder.append(Constants.GITHUB_API_URL);
                    webTargetBuilder.append(Constants.ORGS);
                    webTargetBuilder.append(Constants.URL_SEPARATOR);
                    webTargetBuilder.append(git.getOrganizationName());
                    webTargetBuilder.append(Constants.URL_SEPARATOR);
                    webTargetBuilder.append(Constants.TEAMS);

                    WebTarget webTarget = ClientBuilder.newClient()
                            .target(webTargetBuilder.toString())
                            .queryParam(Constants.ACCESS_TOKEN, privateToken)
                            .queryParam(Constants.PAGE, page);
                    Response response = webTarget.request(MediaType.APPLICATION_JSON).get();
                    List<GitHubGroup> fetchedGroups = response.readEntity(new GenericType<List<GitHubGroup>>() {
                    });
                    if (fetchedGroups.size() == 0) break;
                    gitHubGroups.addAll(fetchedGroups);
                    page += 1;
                }

                for (GitHubGroup gitHubGroup : gitHubGroups) {
                    StringBuilder repoEndPointBuilder = new StringBuilder();

                    repoEndPointBuilder.append(Constants.GITHUB_API_URL);
                    repoEndPointBuilder.append(Constants.TEAMS);
                    repoEndPointBuilder.append(Constants.URL_SEPARATOR);
                    repoEndPointBuilder.append(gitHubGroup.getId());
                    repoEndPointBuilder.append(Constants.URL_SEPARATOR);
                    repoEndPointBuilder.append(Constants.REPOS);

                    WebTarget webTarget = ClientBuilder.newClient()
                            .target(repoEndPointBuilder.toString())
                            .queryParam(Constants.ACCESS_TOKEN, privateToken);
                    Response response = webTarget.request(MediaType.APPLICATION_JSON).get();
                    List<GitHubProject> fetchedProjects = response.readEntity(new GenericType<List<GitHubProject>>() {
                    });
                    gitHubGroup.setGitHubProjects(fetchedProjects);
                }
            } catch (Throwable ex) {
                log.error("Throwable Exception while fetching groups ", ex);
            }
        }
        return gitHubGroups;
    }
}
