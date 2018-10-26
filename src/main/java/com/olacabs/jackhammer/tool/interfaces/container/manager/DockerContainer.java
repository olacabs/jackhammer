package com.olacabs.jackhammer.tool.interfaces.container.manager;
//

import com.olacabs.jackhammer.models.ToolManifest;
import lombok.extern.slf4j.Slf4j;
import mesosphere.marathon.client.model.v2.Container;
import mesosphere.marathon.client.model.v2.Docker;
import mesosphere.marathon.client.model.v2.Port;
import mesosphere.marathon.client.model.v2.Volume;

import java.util.ArrayList;
import java.util.Collection;

@Slf4j
public class DockerContainer {

    public Container getContainer(ToolManifest toolManifest) {
        Container container = new Container();
        container.setType(toolManifest.getContainer().getType());
        container.setDocker(getDocker(toolManifest));
        container.setVolumes(getVolumes(toolManifest));
        return container;
    }

    private Docker getDocker(ToolManifest toolManifest) {
        Docker docker = new Docker();
        docker.setImage(toolManifest.getContainer().getDocker().getImage());
        docker.setNetwork(toolManifest.getContainer().getDocker().getNetwork());
        docker.setPortMappings(getPortMappings(toolManifest));
        return docker;
    }

    private Collection<Port> getPortMappings(ToolManifest toolManifest) {
        Collection<Port> portCollection = new ArrayList<Port>();
        Port port = new Port(toolManifest.getContainer().getDocker().getPortMappings().get(0).getContainerPort());
        port.setHostPort(toolManifest.getContainer().getDocker().getPortMappings().get(0).getHostPort());
        port.setProtocol(toolManifest.getContainer().getDocker().getPortMappings().get(0).getProtocol());
        portCollection.add(port);
        return portCollection;
    }

    private Collection<Volume> getVolumes(ToolManifest toolManifest) {
        Collection<Volume> volumeCollection = new ArrayList<Volume>();
        if (toolManifest.getContainer().getVolumes()!=null && toolManifest.getContainer().getVolumes().get(0) != null) {
            Volume volume = new Volume();
            volume.setContainerPath(toolManifest.getContainer().getVolumes().get(0).getContainerPath());
            volume.setHostPath(toolManifest.getContainer().getVolumes().get(0).getHostPath());
            volume.setMode(toolManifest.getContainer().getVolumes().get(0).getMode());
            volumeCollection.add(volume);
        }
        return volumeCollection;
    }
}
