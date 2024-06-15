package org.cda.system.systemmonitors;

import java.io.File;

import org.cda.common.enums.Units;
import org.cda.common.enums.Parameters;
import org.cda.system.SystemData;
import org.cda.system.enums.SystemResources;

public class SystemDiskMonitor extends AbstractSystemResourceMonitor {

    private final File root = new File("/");

    public SystemDiskMonitor() {
        this.resource = SystemResources.DISK;
    }

    /**
     * Returns array of data related to local disks.
     *
     */
    @Override
    public SystemData[] getTelemetry() {
        double usableSpace = this.root.getUsableSpace() / (1024.0 * 1024 * 1024);
        double totalSpace = this.root.getTotalSpace() / (1024.0 * 1024 * 1024);
        double usedSpace = totalSpace - usableSpace;

        return new SystemData[] {
                new SystemData(Parameters.USED_SPACE, usedSpace, Units.GIGABYTE),
                new SystemData(Parameters.TOTAL_SPACE, totalSpace, Units.GIGABYTE)
        };
    }
}
