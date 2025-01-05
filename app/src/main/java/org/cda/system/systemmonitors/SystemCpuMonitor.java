package org.cda.system.systemmonitors;

import org.cda.data.SystemData;

import oshi.hardware.CentralProcessor;
import oshi.hardware.Sensors;

public class SystemCpuMonitor implements SystemResourceMonitorInterface {

    private final CentralProcessor cpu;
    private final Sensors sensors;

    public SystemCpuMonitor(CentralProcessor cpu, Sensors sensors) {
        this.cpu = cpu;
        this.sensors = sensors;
    }

    /**
     * Sets CPU Temperature in degC and CPU usage in % in a SystemData object.
     *
     */
    @Override
    public void collectTelemetry(SystemData systemData) {
        double[] loadAvg = this.cpu.getSystemLoadAverage(1);
        double cpuTemp = this.sensors.getCpuTemperature();

        double roundedLoadAvg = loadAvg[0] * 100;
        roundedLoadAvg = Math.round(roundedLoadAvg * 100.0) / 100.0;

        systemData.setCpuLoadAvg(roundedLoadAvg);
        systemData.setCpuTemp(cpuTemp);
    }
}
