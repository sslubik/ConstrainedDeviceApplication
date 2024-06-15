package org.cda.system;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;

import org.cda.common.ConfigUtil;
import org.cda.common.enums.ConfigIntegers;
import org.cda.system.systemmonitors.*;

import lombok.extern.java.Log;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.Sensors;

@Log
public class SystemPerformanceManager {

    private final ScheduledExecutorService schedExecSrvc = Executors.newScheduledThreadPool(1);
    private final Runnable taskRunner;

    private final AbstractSystemResourceMonitor[] systemMonitors;

    private boolean isStarted = false;
    private int pollRate;

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

        this.systemMonitors = new AbstractSystemResourceMonitor[] {
                new SystemCpuMonitor(cpu, sensors),
                new SystemMemoryMonitor(memory),
                new SystemDiskMonitor()
        };

        this.pollRate = ConfigUtil.getInstance().getIntiger(ConfigIntegers.POLL_RATE);
        this.pollRate = Math.max(pollRate, 10); // Set to 10 in case a negative number is provided

        this.taskRunner = () -> {
            this.handleTelemetry();
        };
    }

    /**
     * Handles telemetry by logging telemetry values to console.
     *
     */
    public void handleTelemetry() {
        SystemPerformanceManager.log.info("System telemetry");

        for (AbstractSystemResourceMonitor monitor : this.systemMonitors) {
            SystemPerformanceManager.log.fine("\t" + monitor.getResource().getName() + ": ");

            for (SystemData sd : monitor.getTelemetry()) {
                String formattedValue = String.format("%.2f", sd.getValue());

                System.out.println(
                        "\t"
                                + sd.getParameter().getName()
                                + ": "
                                + formattedValue
                                + " "
                                + sd.getUnit().getName());
            }
        }
    }

    /**
     * Starts collecting Gateway Device system telemetry data.
     *
     */
    public void start() {
        if (!this.isStarted) {
            this.schedExecSrvc.scheduleAtFixedRate(
                    this.taskRunner,
                    0L,
                    this.pollRate,
                    TimeUnit.SECONDS);

            this.isStarted = true;
        }
    }

    /**
     * Terminates collecting Gateway Device system telemetry data.
     *
     */
    public void stop() {
        this.isStarted = false;
        this.schedExecSrvc.shutdown();
    }
}
