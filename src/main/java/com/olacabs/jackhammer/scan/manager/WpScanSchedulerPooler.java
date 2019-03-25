package com.olacabs.jackhammer.scan.manager;

import com.google.inject.Inject;
import com.olacabs.jackhammer.configuration.JackhammerConfiguration;
import io.dropwizard.lifecycle.Managed;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class WpScanSchedulerPooler implements Managed {

    @Inject
    JackhammerConfiguration jackhammerConfiguration;

    @Inject
    WpScanSchedulerPicker wpScanSchedulerPicker;

    public void start() throws Exception {
        //setting thread pool
        int threadPoolSize = jackhammerConfiguration.getScanMangerConfiguration().getThreadPoolSize();
        int initialDelay = jackhammerConfiguration.getScanMangerConfiguration().getInitialDelay();
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(threadPoolSize);
        executor.scheduleAtFixedRate(wpScanSchedulerPicker, initialDelay, 1, TimeUnit.DAYS);
    }

    public void stop() throws Exception {
    }
}
