package org.cda.devices;

import org.cda.data.WeatherData;

import lombok.Setter;

public abstract class AbstractSensor {

    @Setter
    protected WeatherData weatherData;

    public abstract void setup();
}
