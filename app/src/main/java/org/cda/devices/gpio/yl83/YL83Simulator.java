package org.cda.devices.gpio.yl83;

import java.util.Random;

import org.cda.data.WeatherData;
import org.cda.devices.SensorTask;

public class YL83Simulator extends SensorTask {

    private final Random random = new Random();

    public YL83Simulator(WeatherData weatherData) {
        super.weatherData = weatherData;
    }

    @Override
    public void setup() {
    }

    @Override
    public Void call() {
        this.weatherData.setRaining(this.random.nextBoolean());

        return null;
    }
}
