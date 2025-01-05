package org.cda.managers;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.cda.common.enums.ConfigBooleans;
import org.cda.common.enums.ConfigIntegers;
import org.cda.common.utils.ConfigUtil;
import org.cda.data.WeatherData;
import org.cda.data.WeatherDataHandlerInterface;
import org.cda.devices.SensorTask;
import org.cda.devices.gpio.yl83.YL83Device;
import org.cda.devices.gpio.yl83.YL83Simulator;
import org.cda.devices.i2c.bme280.BME280Device;
import org.cda.devices.i2c.bme280.BME280Simulator;

import com.pi4j.Pi4J;
import com.pi4j.context.Context;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SensorManager {

    private boolean isInitialized = false;
    private int pollRate;

    @Setter
    private WeatherDataHandlerInterface weatherDataHandler;

    private final WeatherData weatherData = new WeatherData();
    private final I2CSensorManager i2cManager;
    private final SensorTask[] sensors;

    private final ExecutorService measExecSrvc;
    private final ScheduledExecutorService schedExecSrvc;
    private final Runnable taskRunner;

    public SensorManager() {
        ConfigUtil config = ConfigUtil.getInstance();

        // Add new sensors/emulators over here
        if (config.getBoolean(ConfigBooleans.ENABLE_EMULATION)) {
            this.i2cManager = new I2CSensorManager(
                    this.weatherData,
                    new BME280Simulator());

            this.sensors = new SensorTask[] {
                    new YL83Simulator(this.weatherData)
            };
        } else {
            Context pi4jContext = Pi4J.newAutoContext();

            this.i2cManager = new I2CSensorManager(
                    this.weatherData,
                    new BME280Device(pi4jContext, config));

            this.sensors = new SensorTask[] {
                    new YL83Device(this.weatherData, pi4jContext, config)
            };
        }

        this.pollRate = config.getInteger(ConfigIntegers.POLL_RATE);
        this.pollRate = Math.max(this.pollRate, 10);

        // Create ExecutorService for each SensorTask + I2CSensorManager
        this.measExecSrvc = Executors
                .newFixedThreadPool(this.sensors.length + 1);

        this.schedExecSrvc = Executors.newScheduledThreadPool(1);
        this.taskRunner = () -> {
            this.collectWeatherData();
        };
    }

    public void init() {
        if (!this.isInitialized) {
            this.schedExecSrvc.scheduleAtFixedRate(
                    this.taskRunner,
                    0L,
                    this.pollRate,
                    TimeUnit.SECONDS);

            this.i2cManager.init();

            for (var sensor : this.sensors) {
                sensor.setup();
            }

            this.isInitialized = true;
        }
    }

    private void collectWeatherData() {
        ArrayList<Callable<Void>> tasks = new ArrayList<>(sensors.length + 1);

        tasks.add(this.i2cManager);

        for (var sensor : this.sensors) {
            tasks.add(sensor);
        }

        try {
            this.measExecSrvc.invokeAll(tasks);
        } catch (InterruptedException e) {
            this.log.error("Error during executing sensor measurement tasks: ");
            e.printStackTrace();
            System.exit(1);
        }

        this.weatherDataHandler.handleWeatherData(this.weatherData);
    }
}
