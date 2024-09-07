package org.cda.app;

import org.cda.managers.DeviceDataManager;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class App {

    private DeviceDataManager deviceDataManager;

    public App(
            String[] args,
            DeviceDataManager deviceDataManager) {


        this.deviceDataManager = deviceDataManager;
    }

    /**
     * Starts the application.
     *
     */
    public void run() {
        App.log.info("Constrained Device Application initialized successfully!");

        this.deviceDataManager.init();
    }
}
