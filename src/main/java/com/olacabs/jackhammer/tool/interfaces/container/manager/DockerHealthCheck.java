package com.olacabs.jackhammer.tool.interfaces.container.manager;

//import mesosphere.marathon.client.model.v2.HealthCheck;

import com.olacabs.jackhammer.models.Command;
import com.olacabs.jackhammer.models.HealthCheck;
import com.olacabs.jackhammer.models.ToolManifest;

import java.util.*;


public class DockerHealthCheck {

    public List<HealthCheck> getDockerHealthCheck(ToolManifest toolManifest) {
        List<HealthCheck> healthChecksCollection = new ArrayList<HealthCheck>() {};
        HealthCheck healthCheck = new HealthCheck();
        healthCheck.setProtocol(toolManifest.getHealthChecks().get(0).getProtocol());
        healthCheck.setCommand(getHealthCheckCommand(toolManifest));
        healthCheck.setGracePeriodSeconds(toolManifest.getHealthChecks().get(0).getGracePeriodSeconds());
        healthCheck.setIntervalSeconds(toolManifest.getHealthChecks().get(0).getIntervalSeconds());
        healthCheck.setTimeoutSeconds(toolManifest.getHealthChecks().get(0).getTimeoutSeconds());
        healthCheck.setMaxConsecutiveFailures(toolManifest.getHealthChecks().get(0).getMaxConsecutiveFailures());
        healthCheck.setIgnoreHttp1xx(toolManifest.getHealthChecks().get(0).isIgnoreHttp1xx());
        healthChecksCollection.add(healthCheck);
        return healthChecksCollection;
    }

    private Command getHealthCheckCommand(ToolManifest toolManifest) {
        Command command = new Command();
        command.setValue(toolManifest.getHealthChecks().get(0).getCommand().getValue());
//        Map<String,Object> commandMap = new HashMap<String, Object>();
//        commandMap.put("value","curl -f -X GET http://$HOST:9998/admin/healthcheck");
        return command;
    }
}
