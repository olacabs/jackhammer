package com.olacabs.jackhammer.utilities;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.Image;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.command.PullImageResultCallback;

import com.olacabs.jackhammer.common.Constants;
import com.olacabs.jackhammer.models.ToolManifest;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
public class DockerUtil {

    final DockerClient dockerClient = DockerClientBuilder.getInstance().build();
    public static Set<String> toolDeleteRequests = new HashSet<String>();

    public void pullImage(String imageId) {
        final List<Image> images = dockerClient.listImagesCmd().withImageNameFilter(imageId).exec();
        if (images.size() == 0) {
            try {
                dockerClient.pullImageCmd(imageId).exec(new PullImageResultCallback()).awaitCompletion(30, TimeUnit.SECONDS);
                waitUntilPullImage(dockerClient, imageId);
            } catch (InterruptedException ie) {
                log.info("InterruptedException while pulling image...{}", ie);
            }
        }
    }

    public Map<String, Integer> getContainerImages() {
        List<Container> containerList = dockerClient.listContainersCmd().exec();
        log.info("containers list...{}..{}", containerList.size());
        Map<String, Integer> imagesMap = new HashMap<String, Integer>();
        for (Container container : containerList) {
            Integer count = imagesMap.get(container.getImage());
            imagesMap.put(container.getImage(), (count == null) ? 1 : count + 1);
        }
        return imagesMap;
    }

    public void startInstances(String imageId, ToolManifest toolManifest) {
        if (toolDeleteRequests.contains(imageId)) return;
        Map<String, Integer> containerImages = getContainerImages();
        int runningCount = containerImages.get(imageId) == null ? 0 : containerImages.get(imageId);
        if (runningCount == toolManifest.getInitialInstances()) return;
        int count;
        log.info("starting container for image..{}..{}", imageId);
        for (count = runningCount; count < toolManifest.getInitialInstances(); count++) {
            startContainer(toolManifest, dockerClient, imageId);
        }
    }


    private String startContainer(ToolManifest toolManifest, DockerClient dockerClient, String imageId) {
        List<String> envList = new ArrayList<String>();

        for (Map.Entry<String, String> eachEnv : toolManifest.getEnv().entrySet()) {
            StringBuilder env = new StringBuilder();
            env.append(eachEnv.getKey());
            env.append(Constants.EQUAL);
            env.append(eachEnv.getValue());
            envList.add(env.toString());
        }

        CreateContainerResponse container = dockerClient.createContainerCmd(imageId).withEnv(envList).exec();
        dockerClient.startContainerCmd(container.getId()).exec();
        waitUntilContainerUp(imageId);
        return container.getId();
    }


    public void stopContainers(String imageId) {
        toolDeleteRequests.add(imageId);
        List<Container> containerList = dockerClient.listContainersCmd().exec();
        for (Container container : containerList) {
            if (StringUtils.equals(container.getImage(), imageId)) {
                stopContainer(container.getId());
            }
        }
        toolDeleteRequests.remove(imageId);
    }

    public void stopContainer(String containerId) {
        dockerClient.stopContainerCmd(containerId).exec();
        dockerClient.removeContainerCmd(containerId).exec();
    }

    private void waitUntilPullImage(DockerClient dockerClient, String imageId) {
        int retryCount = 0;
        while (true) {
            List<Image> images = dockerClient.listImagesCmd().withImageNameFilter(imageId).exec();
            try {
                TimeUnit.SECONDS.sleep(10);
                if (images.size() > 0 || retryCount == 12) break;
            } catch (InterruptedException ie) {
                log.error("InterruptedException while getting status of image..", ie);
            }
            retryCount += 1;
        }
    }

    public void waitUntilContainerUp(String imageId) {
        int retryCount = 0;
        Map<String, Integer> containerImages = getContainerImages();
        int currentRunningCount = containerImages.get(imageId) == null ? 0 : containerImages.get(imageId);
        while (true) {
            containerImages = getContainerImages();
            try {
                TimeUnit.SECONDS.sleep(10);
                if ((containerImages.containsKey(imageId)
                        && (containerImages.get(imageId) == currentRunningCount + 1))
                        || retryCount == 12)
                    break;
            } catch (InterruptedException ie) {
                log.error("InterruptedException while getting status of container..", ie);
            }
            retryCount += 1;
        }
    }
}
