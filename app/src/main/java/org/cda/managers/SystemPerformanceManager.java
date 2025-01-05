package org.cda.managers;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.cda.common.enums.ConfigIntegers;
import org.cda.common.utils.ConfigUtil;
import org.cda.data.SystemData;
import org.cda.data.SystemDataHandlerInterface;
import org.cda.system.systemmonitors.SystemCpuMonitor;
import org.cda.system.systemmonitors.SystemDiskMonitor;
import org.cda.system.systemmonitors.SystemMemoryMonitor;
import org.cda.system.systemmonitors.SystemResourceMonitorInterface;

import lombok.Setter;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.Sensors;

public class SystemPerformanceManager {

    private final SystemData systemData = new SystemData();
    private boolean isInitialized = false;
    private int pollRate;

    @Setter
    private SystemDataHandlerInterface systemDataHandler;

    private final SystemResourceMonitorInterface[] systemMonitors;

    private final ScheduledExecutorService schedExecSrvc;
    private final Runnable taskRunner;

    public SystemPerformanceManager() {
        this.schedExecSrvc = Executors.newScheduledThreadPool(1);

        SystemInfo sysInfo = new SystemInfo();
        HardwareAbstractionLayer hal = sysInfo.getHardware();
        CentralProcessor cpu = hal.getProcessor();
        Sensors sensors = hal.getSensors();
        GlobalMemory memory = hal.getMemory();

        this.systemMonitors = new SystemResourceMonitorInterface[] {
                new SystemCpuMonitor(cpu, sensors),
                new SystemMemoryMonitor(memory),
                new SystemDiskMonitor()
        };

        ConfigUtil config = ConfigUtil.getInstance();

        this.pollRate = config.getInteger(ConfigIntegers.POLL_RATE);
        this.pollRate = Math.max(pollRate, 10);

        this.taskRunner = () -> {
            this.handleTelemetry();
        };
    }

    private void handleTelemetry() {
        for (var monitor : this.systemMonitors) {
            monitor.collectTelemetry(this.systemData);
        }

        this.systemDataHandler.handleSystemData(this.systemData);
    }

    public void init() {
        if (!this.isInitialized) {
            this.schedExecSrvc.scheduleAtFixedRate(
                    this.taskRunner,
                    0L,
                    this.pollRate,
                    TimeUnit.SECONDS);

            this.isInitialized = true;
        }
    }
}
