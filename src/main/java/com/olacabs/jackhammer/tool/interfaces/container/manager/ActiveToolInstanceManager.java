package com.olacabs.jackhammer.tool.interfaces.container.manager;

import com.google.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import com.olacabs.jackhammer.configuration.JackhammerConfiguration;
import io.dropwizard.lifecycle.Managed;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ActiveToolInstanceManager implements Managed {

    @Inject
    JackhammerConfiguration jackhammerConfiguration;

    @Inject
    ActiveToolInstanceHealthCheck activeToolInstanceHealthCheck;


    public void start() throws Exception {
        try {
            //setting thread pool
            int threadPoolSize = jackhammerConfiguration.getToolManagerConfiguration().getThreadPoolSize();
            int initialDelay = jackhammerConfiguration.getToolManagerConfiguration().getInitialDelay();
            int period = jackhammerConfiguration.getToolManagerConfiguration().getPeriod();
            ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(threadPoolSize);
            executor.scheduleAtFixedRate(activeToolInstanceHealthCheck, initialDelay, period, TimeUnit.SECONDS);
        } catch (Throwable th) {
            log.error("Error in Tool Pooler while pooling", th);
        }
    }

    public void stop() throws Exception {

    }
}
