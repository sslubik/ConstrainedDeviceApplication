package org.cda.system.systemmonitors;

import java.io.File;

import org.cda.data.SystemData;

public class SystemDiskMonitor implements SystemResourceMonitorInterface {

    private final File root = new File("/");

    /**
     * Sets used disk space and total disk space values in a SystemData object.
     *
     */
    @Override
    public void collectTelemetry(SystemData systemData) {
        double usableSpace = this.root.getUsableSpace() / Math.pow(1024.0, 3);
        double totalSpace = this.root.getTotalSpace() / Math.pow(1024.0, 3);
        double usedSpace = totalSpace - usableSpace;

        double totalSpaceRounded = Math.round(totalSpace * 100.0) / 100.0;
        double usedSpaceRounded = Math.round(usedSpace * 100.0) / 100.0;

        systemData.setDiskTotalSpace(totalSpaceRounded);
        systemData.setDiskUsedSpace(usedSpaceRounded);
    }
}
