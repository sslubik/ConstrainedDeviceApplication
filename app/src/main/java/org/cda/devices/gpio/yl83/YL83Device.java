package org.cda.devices.gpio.yl83;

import org.cda.common.enums.ConfigIntegers;
import org.cda.common.utils.ConfigUtil;
import org.cda.data.WeatherData;
import org.cda.devices.SensorTask;

import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.DigitalInput;
import com.pi4j.io.gpio.digital.DigitalState;
import com.pi4j.io.gpio.digital.PullResistance;

public class YL83Device extends SensorTask {

    private final DigitalInput yl83;

    public YL83Device(WeatherData weatherData, Context pi4j, ConfigUtil config) {
        super.weatherData = weatherData;

        var inputConfig = DigitalInput.newConfigBuilder(pi4j)
                .id("YL-83")
                .name("Rain sensor")
                .address(config.getInteger(ConfigIntegers.YL83_PIN))
                .pull(PullResistance.PULL_UP);

        this.yl83 = pi4j.create(inputConfig);
    }

    @Override
    public void setup() {
    }

    @Override
    public Void call() {
        // If sensor pulls down the pin, then raining = true
        super.weatherData.setRaining(this.yl83.state() == DigitalState.LOW);

        return null;
    }

}
