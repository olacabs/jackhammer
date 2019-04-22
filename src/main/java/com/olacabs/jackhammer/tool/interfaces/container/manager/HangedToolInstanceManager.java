package com.olacabs.jackhammer.tool.interfaces.container.manager;

import com.google.inject.Inject;
import com.olacabs.jackhammer.configuration.JackhammerConfiguration;
import io.dropwizard.lifecycle.Managed;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
public class HangedToolInstanceManager implements Managed {


    @Inject
    JackhammerConfiguration jackhammerConfiguration;

    @Inject
    HangedToolInstanceCheck hangedToolInstanceCheck;


    public void start() throws Exception {
        try {
            //setting thread pool
            int threadPoolSize = jackhammerConfiguration.getToolManagerConfiguration().getThreadPoolSize();
            int initialDelay = jackhammerConfiguration.getToolManagerConfiguration().getInitialDelay();
            int period = jackhammerConfiguration.getToolManagerConfiguration().getPeriod();
            ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(threadPoolSize);
            executor.scheduleAtFixedRate(hangedToolInstanceCheck, initialDelay, period/2, TimeUnit.MINUTES);
        } catch (Throwable th) {
            log.error("Error in ToolPooler while pooling", th);
        }
    }

    public void stop() throws Exception {

    }
}
