package org.cda.managers;

import java.util.concurrent.Callable;

import org.cda.data.WeatherData;
import org.cda.devices.i2c.AbstractI2CSensor;

public class I2CSensorManager implements Callable<Void> {

    private boolean isInitialized = false;

    private final AbstractI2CSensor[] i2cSensors;
    private final WeatherData weatherData;

    public I2CSensorManager(WeatherData weatherData, AbstractI2CSensor... i2cSensors) {
        this.weatherData = weatherData;
        this.i2cSensors = i2cSensors;
    }

    /**
     * Sets up I2C devices.
     *
     */
    public void init() {
        if (!isInitialized) {
            for (var sensor : this.i2cSensors) {
                sensor.setup();
                sensor.setWeatherData(this.weatherData);
            }

            this.isInitialized = true;
        }
    }

    @Override
    public Void call() {
        for (var sensor : this.i2cSensors) {
            sensor.collectData();
        }

        return null;
    }
}
