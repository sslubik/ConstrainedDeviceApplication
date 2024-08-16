package org.cda.devices.i2c;

import org.cda.devices.AbstractSensor;

import com.pi4j.io.i2c.I2C;

public abstract class AbstractI2CSensor extends AbstractSensor {

    protected I2C sensor;

    protected AbstractI2CSensor() {
    }

    public abstract void collectData();

    /**
     * Reads 8 bits from register as unsigned char and casts it to int.
     *
     */
    protected int read8u(int registerAddress) {
        return this.sensor.readRegister(registerAddress);
    }

    /**
     * Reads 8 bits from register as signed char and casts it to int.
     *
     */
    protected int read8s(int registerAddress) {
        byte[] buffer = new byte[1];
        this.sensor.readRegister(registerAddress, buffer);

        return buffer[0];
    }

    /**
     * Reads 16 bits from registers as unsigned short and casts it to int.
     *
     */
    protected int read16u(int registerAddress) {
        byte[] buffer = new byte[2];
        this.sensor.readRegister(registerAddress, buffer);

        return ((buffer[1] & 0xFF) << 8) | (buffer[0] & 0xFF);
    }

    /**
     * Reads 16 bits from registers as signed short and casts it to int.
     *
     */
    protected int read16s(int registerAddress) {
        byte[] buffer = new byte[2];
        this.sensor.readRegister(registerAddress, buffer);

        return (short) (((buffer[1] & 0xFF) << 8) | (buffer[0] & 0xFF));
    }
}
