package org.cda;

import org.cda.app.App;
import org.cda.managers.DeviceDataManager;

public class Main {
    public static void main(String[] args) {
        App cda = new App(
                args,
                new DeviceDataManager());

        cda.run();
    }
}
