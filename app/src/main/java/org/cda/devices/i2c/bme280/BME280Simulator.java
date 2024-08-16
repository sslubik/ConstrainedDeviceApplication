package org.cda.devices.i2c.bme280;

import java.text.DecimalFormat;

import org.cda.devices.i2c.AbstractI2CSensor;

public class BME280Simulator extends AbstractI2CSensor {

    @Override
    public void setup() {
    }

    @Override
    public void collectData() {
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
