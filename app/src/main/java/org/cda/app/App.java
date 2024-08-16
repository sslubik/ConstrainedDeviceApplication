package org.cda.app;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;

import org.cda.managers.DeviceDataManager;

import lombok.extern.java.Log;

@Log
public class App {

    private DeviceDataManager deviceDataManager;

    /**
     * Logger setup.
     *
     */
    static {
        ConsoleHandler handler = new ConsoleHandler();
        handler.setLevel(Level.ALL);
        log.addHandler(handler);
        log.setUseParentHandlers(false);
        log.setLevel(Level.ALL);
    }

    public App(
            String[] args,
            DeviceDataManager deviceDataManager) {

        App.log.info("Initializing Constrained Device Application...");

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
