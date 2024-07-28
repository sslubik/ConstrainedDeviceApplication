package org.cda.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WeatherData {

    private double temperature;
    private double humidity;
    private double pressure;
    private double windSpeed;
    private boolean isRaining;
}
