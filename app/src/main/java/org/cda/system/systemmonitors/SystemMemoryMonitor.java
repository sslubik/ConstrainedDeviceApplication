package org.cda.system.systemmonitors;

import org.cda.data.SystemData;

import oshi.hardware.GlobalMemory;

public class SystemMemoryMonitor implements SystemResourceMonitorInterface {

    private final GlobalMemory memory;

    public SystemMemoryMonitor(GlobalMemory memory) {
        this.memory = memory;
    }

    /**
     * Sets used memory and total memory valuse in a SystemData object.
     *
     */
    @Override
    public void collectTelemetry(SystemData systemData) {
        double availableMemory = this.memory.getAvailable() / (1024.0 * 1024);
        double totalMemory = this.memory.getTotal() / (1024.0 * 1024);
        double usedMemory = totalMemory - availableMemory;

        double roundedTotalMemory = Math.round(totalMemory * 100.0) / 100.0;
        double roundedUsedMemory = Math.round(usedMemory * 100.0) / 100.0;

        systemData.setMemoryTotal(roundedTotalMemory);
        systemData.setMemoryUsed(roundedUsedMemory);
    }
}
