package org.cda;

import org.cda.app.App;
import org.cda.system.SystemPerformanceManager;

public class Main {
    public static void main(String[] args) {
        App gda = new App(
                args,
                new SystemPerformanceManager());

        gda.start();
        gda.stop();
    }
}
