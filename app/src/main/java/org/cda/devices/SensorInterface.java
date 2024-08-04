package org.cda.devices;

import org.cda.data.WeatherData;

public interface SensorInterface {

    public void setup();

    public void collectData(WeatherData weatherData);
}
