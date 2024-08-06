package org.cda.devices.i2c.bme280;

import java.text.DecimalFormat;

import org.cda.data.WeatherData;
import org.cda.devices.SensorInterface;

public class BME280Simulator implements SensorInterface {

    @Override
    public void setup() {
    }

    @Override
    public void collectData(WeatherData weatherData) {
        double randomTemp = Math.random() * 100;
        double randomPress = Math.random() * 100;
        double randomHum = Math.random() * 100;

        DecimalFormat df = new DecimalFormat("0.00");
        randomTemp = Double.parseDouble(df.format(randomTemp));
        randomPress = Double.parseDouble(df.format(randomPress));
        randomHum = Double.parseDouble(df.format(randomHum));

        weatherData.setTemperature(randomTemp);
        weatherData.setPressure(randomPress);
        weatherData.setHumidity(randomHum);
    }
}
