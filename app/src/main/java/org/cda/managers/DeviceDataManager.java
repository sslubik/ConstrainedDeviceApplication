package org.cda.managers;

import org.cda.common.utils.DataUtil;
import org.cda.data.SystemData;
import org.cda.data.SystemDataHandlerInterface;
import org.cda.data.WeatherData;
import org.cda.data.WeatherDataHandlerInterface;

public class DeviceDataManager
        implements
        SystemDataHandlerInterface,
        WeatherDataHandlerInterface {

    private final SystemPerformanceManager sysPerfMan;
    private final SensorManager sensMan;

    public DeviceDataManager() {
        this.sysPerfMan = new SystemPerformanceManager();
        this.sysPerfMan.setSystemDataHandler(this);

        this.sensMan = new SensorManager();
        this.sensMan.setWeatherDataHandler(this);
    }

    public void init() {
        this.sysPerfMan.init();
        this.sensMan.init();
    }

    @Override
    public void handleSystemData(SystemData systemData) {
        String systemDataJson = DataUtil.systemDataToJson(systemData);
        System.out.println(systemDataJson);
    }

    @Override
    public void handleWeatherData(WeatherData weatherData) {
        String weatherDataJson = DataUtil.weatherDataToJson(weatherData);
        System.out.println(weatherDataJson);
    }
}
