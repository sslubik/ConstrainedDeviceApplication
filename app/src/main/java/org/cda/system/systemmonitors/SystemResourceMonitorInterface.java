package org.cda.system.systemmonitors;

import org.cda.data.SystemData;

public interface SystemResourceMonitorInterface {

    /**
     * This method sets system parameters inside a SystemData object.
     *
     */
    public abstract void collectTelemetry(SystemData systemData);
}
