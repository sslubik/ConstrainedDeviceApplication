package org.cda.data;

import org.cda.data.enums.MessageType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WeatherData extends AbstractDataMessage {

    public WeatherData() {
        super.messageType = MessageType.WEATHER_DATA.getString();
    }

    private double temperature;
    private double humidity;
    private double pressure;
    private boolean isRaining;
}
