package com.olacabs.jackhammer.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mesosphere.marathon.client.model.v2.Volume;
import mesosphere.marathon.client.utils.ModelUtils;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Container {
    private Docker docker;
    private String type;
    private List<Volume> volumes;
    @Override
    public String toString() {
        return ModelUtils.toString(this);
    }
}
