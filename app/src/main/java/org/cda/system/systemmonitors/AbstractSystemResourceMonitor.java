package org.cda.system.systemmonitors;

import org.cda.system.SystemData;
import org.cda.system.enums.SystemResources;

import lombok.Getter;

@Getter
public abstract class AbstractSystemResourceMonitor {

    protected SystemResources resource;

    /**
     * This method returns array of classes holding data related to the system
     * resource.
     *
     */
    public abstract SystemData[] getTelemetry();
}
