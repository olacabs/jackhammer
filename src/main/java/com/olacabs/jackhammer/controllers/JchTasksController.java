package com.olacabs.jackhammer.controllers;

import com.google.inject.Inject;
import com.olacabs.jackhammer.common.Constants;
import com.olacabs.jackhammer.configuration.JackhammerConfiguration;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/api/v1/jch_tasks")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Slf4j
public class JchTasksController {

    @Inject
    JackhammerConfiguration jackhammerConfiguration;

    @GET
    public Response getJchRunningTasks() {
        WebTarget webTarget = ClientBuilder.newClient().target(getMarathonTaskApi());
        Response response = webTarget.request(MediaType.APPLICATION_JSON).get();
        return response;
    }

    private String getMarathonTaskApi() {
        String marathonEndpoint = jackhammerConfiguration.getMarathonConfiguration().getEndpoint();
        String jchAppId = jackhammerConfiguration.getMarathonConfiguration().getJchAppId();
        StringBuilder marathonTaskApi = new StringBuilder();
        marathonTaskApi.append(marathonEndpoint);
        marathonTaskApi.append(Constants.MARATHON_APPS_API);
        marathonTaskApi.append(Constants.URL_SEPARATOR);
        marathonTaskApi.append(jchAppId);
        marathonTaskApi.append(Constants.MARATHON_TASKS_API);
        return marathonTaskApi.toString();
    }
}
