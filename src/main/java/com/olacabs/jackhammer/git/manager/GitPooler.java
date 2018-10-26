package com.olacabs.jackhammer.git.manager;

import com.google.inject.Inject;

import io.dropwizard.lifecycle.Managed;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.olacabs.jackhammer.configuration.JackhammerConfiguration;

@Slf4j
public class GitPooler implements Managed {

    @Inject
    JackhammerConfiguration jackhammerConfiguration;

    @Inject
    ReposPuller reposPuller;

    public void start() throws Exception {

        //setting thread pool
        int threadPoolSize = jackhammerConfiguration.getGitConfiguration().getThreadPoolSize();
        int initialDelay = jackhammerConfiguration.getGitConfiguration().getInitialDelay();
        int period = jackhammerConfiguration.getGitConfiguration().getPeriod();
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(threadPoolSize);
        executor.scheduleAtFixedRate(reposPuller, initialDelay, period, TimeUnit.DAYS);
    }

    public void stop() throws Exception {
    }
}
