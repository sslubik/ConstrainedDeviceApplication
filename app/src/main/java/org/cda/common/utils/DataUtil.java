package org.cda.common.utils;

import org.cda.data.SystemData;
import org.cda.data.WeatherData;

import com.google.gson.Gson;

public class DataUtil {

    private static Gson gson = new Gson();

    public static String systemDataToJson(SystemData systemData) {
        if (systemData == null) {
            return "";
        }

        return DataUtil.gson.toJson(systemData);
    }

    public static String weatherDataToJson(WeatherData weatherData) {
        if (weatherData == null) {
            return "";
        }

        return DataUtil.gson.toJson(weatherData);
    }
}
