package com.olacabs.jackhammer.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mesosphere.marathon.client.utils.ModelUtils;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Command {
    private String value;

    @Override
    public String toString() {
        return ModelUtils.toString(this);
    }
}
