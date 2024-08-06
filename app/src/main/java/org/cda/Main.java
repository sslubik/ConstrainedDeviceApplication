package org.cda;

import org.cda.app.App;
import org.cda.data.SystemData;
import org.cda.managers.SystemPerformanceManager;

public class Main {
    public static void main(String[] args) {
        App gda = new App(
                args,
                new SystemPerformanceManager(new SystemData()));

        gda.start();
        gda.stop();
    }
}
