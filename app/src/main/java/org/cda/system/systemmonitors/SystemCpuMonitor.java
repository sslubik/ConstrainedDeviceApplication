package org.cda.system.systemmonitors;

import org.cda.common.enums.Units;
import org.cda.common.enums.Parameters;
import org.cda.system.SystemData;
import org.cda.system.enums.SystemResources;

import oshi.hardware.CentralProcessor;
import oshi.hardware.Sensors;

public class SystemCpuMonitor extends AbstractSystemResourceMonitor {

    private final CentralProcessor cpu;
    private final Sensors sensors;

    public SystemCpuMonitor(CentralProcessor cpu, Sensors sensors) {

        this.resource = SystemResources.CPU;
        this.cpu = cpu;
        this.sensors = sensors;
    }

    /**
     * Returns array of data related to CPU.
     *
     */
    @Override
    public SystemData[] getTelemetry() {
        double[] loadAvg = this.cpu.getSystemLoadAverage(1);
        double cpuTemp = this.sensors.getCpuTemperature();

        return new SystemData[] {
                new SystemData(Parameters.USAGE, loadAvg[0] * 100, Units.PERCENT),
                new SystemData(Parameters.TEMPERATURE, cpuTemp, Units.CELSIUS)
        };
    }
}
