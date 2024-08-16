package org.cda.managers;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;

import org.cda.common.utils.ConfigUtil;
import org.cda.common.enums.ConfigIntegers;
import org.cda.data.SystemData;
import org.cda.data.SystemDataHandlerInterface;
import org.cda.system.systemmonitors.*;

import lombok.Setter;
import lombok.extern.java.Log;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.Sensors;

@Log
public class SystemPerformanceManager {

    private final SystemData systemData = new SystemData();
    private boolean isInitialized = false;
    private int pollRate;

    @Setter
    private SystemDataHandlerInterface systemDataHandler;

    private final SystemResourceMonitorInterface[] systemMonitors;

    private final ScheduledExecutorService schedExecSrvc = Executors.newScheduledThreadPool(1);
    private final Runnable taskRunner;

    /**
     * Logger setup.
     *
     */
    static {
        ConsoleHandler handler = new ConsoleHandler();
        handler.setLevel(Level.ALL);
        log.addHandler(handler);
        log.setUseParentHandlers(false);
        log.setLevel(Level.ALL);
    }

    /**
     * Default constructor.
     *
     */
    public SystemPerformanceManager() {
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

        this.pollRate = ConfigUtil.getInstance().getInteger(ConfigIntegers.POLL_RATE);
        this.pollRate = Math.max(pollRate, 10); // Set to 10 in case a negative number is provided

        this.taskRunner = () -> {
            this.handleTelemetry();
        };
    }

    /**
     * Handles telemetry by running
     * SystemResourceMonitorInterface.collectTelemetry()
     * on each SystemResourceMonitor that has been added to the systemMonitors
     * array.
     *
     */
    public void handleTelemetry() {
        for (var monitor : this.systemMonitors) {
            monitor.collectTelemetry(this.systemData);
        }

        this.systemDataHandler.handleSystemData(this.systemData);
    }

    /**
     * Initializes collecting system telemetry data.
     *
     */
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
