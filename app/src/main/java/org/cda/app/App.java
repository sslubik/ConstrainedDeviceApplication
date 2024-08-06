package org.cda.app;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;

import org.cda.managers.SystemPerformanceManager;

import lombok.extern.java.Log;

@Log
public class App {

    private SystemPerformanceManager sysPerfMan;

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
            SystemPerformanceManager sysPerfMan) {

        App.log.info("Initializing Constrained Device Application...");

        this.sysPerfMan = sysPerfMan;
    }

    /**
     * Starts the application.
     *
     */
    public void start() {
        App.log.info("Constrained Device Application initialized successfully!");

        try {
            this.sysPerfMan.start();
            Thread.sleep(600000L);
        } catch (InterruptedException e) {
        }
    }

    /**
     * Shuts down the application.
     *
     */
    public void stop() {
        App.log.info("Closing Constrained Device Application...");

        this.sysPerfMan.stop();
    }
}
