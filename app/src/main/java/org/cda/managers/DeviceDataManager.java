package org.cda.managers;

import org.cda.common.enums.ConfigStrings;
import org.cda.common.utils.ConfigUtil;
import org.cda.common.utils.DataUtil;
import org.cda.data.SystemData;
import org.cda.data.SystemDataHandlerInterface;
import org.cda.data.WeatherData;
import org.cda.data.WeatherDataHandlerInterface;

import com.microsoft.azure.sdk.iot.device.DeviceClient;
import com.microsoft.azure.sdk.iot.device.IotHubClientProtocol;
import com.microsoft.azure.sdk.iot.device.Message;
import com.microsoft.azure.sdk.iot.device.exceptions.IotHubClientException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DeviceDataManager
        implements
        SystemDataHandlerInterface,
        WeatherDataHandlerInterface {

    private final SystemPerformanceManager sysPerfMan;
    private final SensorManager sensMan;

    private final DeviceClient client;

    public DeviceDataManager() {
        this.sysPerfMan = new SystemPerformanceManager();
        this.sensMan = new SensorManager();

        this.sysPerfMan.setSystemDataHandler(this);
        this.sensMan.setWeatherDataHandler(this);

        ConfigUtil config = ConfigUtil.getInstance();

        DeviceClient client = null;

        try {
            client = new DeviceClient(
                    config.getString(ConfigStrings.CONNECTION_STRING),
                    IotHubClientProtocol.MQTT);
            client.open(true);

        } catch (IllegalArgumentException e) {
            DeviceDataManager.log.warn(
                    "Incorrect Azure IoT Connection String! Aborting...");
            e.printStackTrace();
            System.exit(1);
        } catch (IotHubClientException e) {
            DeviceDataManager.log
                    .warn("Connection cannot be established or the server "
                            + "rejects the connection! Aborting...");
            e.printStackTrace();
            System.exit(1);
        }

        this.client = client;

        DeviceDataManager.log.info("Connected to Azure Iot Hub!");

        // Shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            this.client.close();
            DeviceDataManager.log.info(
                    "Connection to Azure IoT Hub closed successfully!");
        }));
    }

    public void init() {
        this.sysPerfMan.init();
        this.sensMan.init();
    }

    @Override
    public void handleSystemData(SystemData systemData) {
        String systemDataJson = DataUtil.systemDataToJson(systemData);
        DeviceDataManager.log.info("Sending message: " + systemDataJson);

        try {
            this.client.sendEvent(new Message(systemDataJson));

            DeviceDataManager.log.info(
                    "Successfully sent system data to the cloud!");
        } catch (IotHubClientException e) {
            DeviceDataManager.log.warn(
                    "The request is rejected by the service or the operation "
                            + "timed out!");
            e.printStackTrace();
        } catch (InterruptedException | IllegalStateException e) {
        }
    }

    @Override
    public void handleWeatherData(WeatherData weatherData) {
        String weatherDataJson = DataUtil.weatherDataToJson(weatherData);
        DeviceDataManager.log.info("Sending message: " + weatherDataJson);

        try {
            this.client.sendEvent(new Message(weatherDataJson));

            DeviceDataManager.log.info(
                    "Successfully sent weather data to the cloud!");
        } catch (IotHubClientException e) {
            DeviceDataManager.log.warn(
                    "The request is rejected by the service or the operation "
                            + "timed out!");
            e.printStackTrace();
        } catch (InterruptedException | IllegalStateException e) {
        }
    }
}
