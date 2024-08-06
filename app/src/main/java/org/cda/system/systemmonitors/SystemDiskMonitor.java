package org.cda.system.systemmonitors;

import java.io.File;

import org.cda.data.SystemData;

public class SystemDiskMonitor implements SystemResourceMonitorInterface {

    private final File root = new File("/");

    public SystemDiskMonitor() {
    }

    /**
     * Sets used disk space and total disk space values in a SystemData object.
     *
     */
    @Override
    public void collectTelemetry(SystemData systemData) {
        double usableSpace = this.root.getUsableSpace() / (1024.0 * 1024 * 1024);
        double totalSpace = this.root.getTotalSpace() / (1024.0 * 1024 * 1024);
        double usedSpace = totalSpace - usableSpace;

        systemData.setDiskTotalSpace(totalSpace);
        systemData.setDiskUsedSpace(usedSpace);
    }
}
