package com.olacabs.jackhammer.git.manager;

import com.google.inject.Inject;
import com.olacabs.jackhammer.utilities.GitUtil;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class ReposPuller implements Runnable {


    @Inject
    GitUtil gitUtil;

    public void run() {
        log.info("repos puller getting started..");
        gitUtil.pullGitRepos();
    }
}
