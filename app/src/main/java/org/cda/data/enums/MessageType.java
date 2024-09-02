package org.cda.data.enums;

import lombok.Getter;

public enum MessageType {

    SYSTEM_DATA("systemData"),
    WEATHER_DATA("weatherData");

    @Getter
    private final String string;

    private MessageType(String string) {
        this.string = string;
    }
}
