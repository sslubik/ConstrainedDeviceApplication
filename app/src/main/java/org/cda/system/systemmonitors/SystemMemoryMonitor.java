package org.cda.system.systemmonitors;

import org.cda.common.enums.Parameters;
import org.cda.common.enums.Units;
import org.cda.system.SystemData;
import org.cda.system.enums.SystemResources;

import oshi.SystemInfo;
import oshi.hardware.GlobalMemory;

public class SystemMemoryMonitor extends AbstractSystemResourceMonitor {

    private final GlobalMemory memory;

    public SystemMemoryMonitor(GlobalMemory memory) {
        this.resource = SystemResources.MEMORY;
        this.memory = memory;
    }

    /**
     * Returns array of data related to memory.
     *
     */
    @Override
    public SystemData[] getTelemetry() {
        double availableMemory = this.memory.getAvailable() / (1024.0 * 1024);
        double totalMemory = this.memory.getTotal() / (1024.0 * 1024);
        double usedMemory = totalMemory - availableMemory;

        return new SystemData[] {
                new SystemData(Parameters.USED_MEMORY, usedMemory, Units.MEGABYTE),
                new SystemData(Parameters.TOTAL_MEMORY, totalMemory, Units.MEGABYTE)
        };
    }
}
