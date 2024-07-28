package org.cda.i2c;

import java.util.List;
import java.util.concurrent.Callable;

import org.cda.devices.i2c.AbstractI2CDevice;

public class I2CManager implements Callable<Void> {

    private AbstractI2CDevice[] i2cDevices;

    public I2CManager(AbstractI2CDevice... i2cDevices) {
        this.i2cDevices = i2cDevices;
    }

    public void setupDevices() {
        for (var device : this.i2cDevices) {
            device.setup();
        }
    }

    public Void call() {
        for (var device : this.i2cDevices) {
            device.collectData();
        }

        return null;
    }
}
