package org.cda.app;

import org.cda.managers.DeviceDataManager;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class App {

    private DeviceDataManager deviceDataManager = new DeviceDataManager();

    public void run() {
        App.log.info(
            "Constrained Device Application initialized successfully!");

        this.deviceDataManager.init();
    }
}
