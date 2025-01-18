package org.cda.devices.i2c.bme280;

import java.text.DecimalFormat;

import org.cda.common.enums.ConfigIntegers;
import org.cda.common.utils.ConfigUtil;
import org.cda.devices.i2c.AbstractI2CSensor;

import com.pi4j.context.Context;
import com.pi4j.io.i2c.I2C;
import com.pi4j.io.i2c.I2CConfig;
import com.pi4j.io.i2c.I2CProvider;

public class BME280Device extends AbstractI2CSensor {

    private int dig_T1;
    private int dig_T2;
    private int dig_T3;

    private int dig_P1;
    private int dig_P2;
    private int dig_P3;
    private int dig_P4;
    private int dig_P5;
    private int dig_P6;
    private int dig_P7;
    private int dig_P8;
    private int dig_P9;

    private int dig_H1;
    private int dig_H2;
    private int dig_H3;
    private int dig_H4;
    private int dig_H5;
    private int dig_H6;

    private int t_fine;

    public BME280Device(Context pi4j, ConfigUtil config) {
        super();

        I2CProvider i2cProvider = pi4j.provider("linuxfs-i2c");
        I2CConfig i2cConfig = I2C.newConfigBuilder(pi4j)
                .id("BME280")
                .bus(config.getInteger(ConfigIntegers.I2C_BUS_NUMBER))
                .device(config.getInteger(ConfigIntegers.I2C_BME280_ADDRESS))
                .build();
        this.sensor = i2cProvider.create(i2cConfig);
    }

    @Override
    public void setup() {
        // Check if the BME280 ID is correct
        int id = this.sensor.readRegister(BME280RegisterAddresses.id);
        if (id != BME280RegisterValues.id) {
            throw new Error("The chip ID of BME280 sensor is incorrect!");
        }

        // By default most registers responsible for
        // storing measurement config are set to 0x00 after powering up,
        // so it is necessary to set them up manually
        this.sensor.writeRegister(BME280RegisterAddresses.reset,
                BME280RegisterValues.reset);

        // Wait 5 ms after resetting the sensor
        try {
            Thread.sleep(5);
        } catch (InterruptedException e) {
        }

        // Enable humidity measurement by setting
        // the LSB in ctrl_hum register to 1
        int ctrl_hum = this.sensor.readRegister(
                BME280RegisterAddresses.ctrl_hum);
        ctrl_hum |= BME280RegisterMasks.ctrl_hum_osrs_h_1smpl;
        this.sensor.writeRegister(BME280RegisterAddresses.ctrl_hum, ctrl_hum);

        // Enable pressure and temperature measurements
        this.sensor.writeRegister(
                BME280RegisterAddresses.ctrl_meas,
                BME280RegisterValues.press_and_temp_osrs_1smpl);

        collectCalibrationParams();
    }

    private void collectCalibrationParams() {
        this.dig_T1 = super.read16u(BME280RegisterAddresses.dig_T1);
        this.dig_T2 = super.read16s(BME280RegisterAddresses.dig_T2);
        this.dig_T3 = super.read16s(BME280RegisterAddresses.dig_T3);

        this.dig_P1 = super.read16u(BME280RegisterAddresses.dig_P1);
        this.dig_P2 = super.read16s(BME280RegisterAddresses.dig_P2);
        this.dig_P3 = super.read16s(BME280RegisterAddresses.dig_P3);
        this.dig_P4 = super.read16s(BME280RegisterAddresses.dig_P4);
        this.dig_P5 = super.read16s(BME280RegisterAddresses.dig_P5);
        this.dig_P6 = super.read16s(BME280RegisterAddresses.dig_P6);
        this.dig_P7 = super.read16s(BME280RegisterAddresses.dig_P7);
        this.dig_P8 = super.read16s(BME280RegisterAddresses.dig_P8);
        this.dig_P9 = super.read16s(BME280RegisterAddresses.dig_P9);

        this.dig_H1 = super.read8u(BME280RegisterAddresses.dig_H1);
        this.dig_H2 = super.read16s(BME280RegisterAddresses.dig_H2);
        this.dig_H3 = super.read8u(BME280RegisterAddresses.dig_H3);

        byte[] buffer = new byte[2];

        this.sensor.readRegister(BME280RegisterAddresses.dig_H4, buffer);
        this.dig_H4 = (short) (((buffer[0] & 0xFF) << 4) | (buffer[1] & 0x0F));

        this.sensor.readRegister(BME280RegisterAddresses.dig_H5, buffer);
        this.dig_H5 = (short) (((buffer[0] & 0x0F))
                | ((buffer[1] & 0xFF) << 4));

        this.dig_H6 = super.read8s(BME280RegisterAddresses.dig_H6);
    }

    @Override
    public void collectData() {
        // Set the device to forced mode
        int ctrl_meas = this.sensor.readRegister(
                BME280RegisterAddresses.ctrl_meas);
        ctrl_meas |= BME280RegisterMasks.forced_mode;
        this.sensor.writeRegister(BME280RegisterAddresses.ctrl_meas, ctrl_meas);

        // Sleep for t = 1.25 + 2.3*1 + 2.3*1 + 0.575 + 2.3*1 + 0.575 = 9.3 ~ 10
        // (chapter 9.1 in datasheet)
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
        }

        // Read the measurements
        byte[] buffer = new byte[8];
        this.sensor.readRegister(BME280RegisterAddresses.press_msb, buffer);

        // Store row values from registers in integers
        int rawPressure = ((buffer[0] & 0xFF) << 12)
                | ((buffer[1] & 0xFF) << 4)
                | ((buffer[2] & 0xF0) >> 4);
        int rawTemperature = ((buffer[3] & 0xFF) << 12)
                | ((buffer[4] & 0xFF) << 4) | ((buffer[5] & 0xF0) >> 4);
        int rawHumidity = ((buffer[6] & 0xFF) << 8) | (buffer[7] & 0xFF);

        DecimalFormat df = new DecimalFormat("0.00");
        String compTemperature = df.format(
                this.calculateTemperature(rawTemperature));
        String compPressure = df.format(this.calculatePressure(rawPressure));
        String compHumidity = df.format(this.calculateHumidity(rawHumidity));

        super.weatherData.setTemperature(Double.parseDouble(compTemperature));
        super.weatherData.setPressure(Double.parseDouble(compPressure));
        super.weatherData.setHumidity(Double.parseDouble(compHumidity));
    }

    /**
     * Returns calculated temperature in DegC and calculates t_fine for other
     * weather measurements calculations.
     *
     */
    private double calculateTemperature(int rawTemperature) {
        double var1, var2, temp;
        double rawTemp = (double) rawTemperature;

        var1 = rawTemp / 16384.0 - (double) this.dig_T1 / 1024.0;
        var1 *= (double) this.dig_T2;
        var2 = rawTemp / 131072.0 - (double) this.dig_T1 / 8192.0;
        var2 *= var2 * (double) this.dig_T3;

        this.t_fine = (int) (var1 + var2);

        temp = (var1 + var2) / 5120.0;

        if (temp < -273.15) {
            return -273.15;
        }

        return temp;
    }

    /**
     * Returns calculated pressure in hPa.
     * Must be run after BME280Device.calcluateTemperature() to get t_fine.
     *
     */
    private double calculatePressure(int rawPressure) {
        double var1, var2, var3, pressure;

        var1 = (double) this.t_fine / 2.0 - 64000.0;
        var2 = var1 * var1 * (double) this.dig_P6 / 32768.0;
        var2 += var1 * (double) this.dig_P5 * 2.0;
        var2 = var2 / 4.0 + (double) this.dig_P4 * 65536.0;
        var3 = (double) this.dig_P3 * var1 * var1 / 524288.0;
        var1 = (var3 + (double) this.dig_P2 * var1) / 524288.0;
        var1 = (1.0 + var1 / 32768.0) * (double) this.dig_P1;

        if (var1 <= 0.0)
            return 0.0;

        pressure = 1048576.0 - (double) rawPressure;
        pressure = (pressure - (var2 / 4096.0)) * 6250.0 / var1;
        var1 = (double) this.dig_P9 * pressure * pressure / 2147483648.0;
        var2 = pressure * (double) this.dig_P8 / 32768.0;
        pressure += (var1 + var2 + (double) this.dig_P7) / 16.0;
        pressure /= 100.0; // To get pressure in hPa

        if (pressure < 0)
            return 0.0;

        return pressure;
    }

    /**
     * Returns humidity in %RH.
     * Must be run after BME280Device.calcluateTemperature() to get t_fine.
     *
     */
    private double calculateHumidity(int rawHumidity) {
        double var1, var2, var3, var4, var5, var6, hum;

        var1 = (double) this.t_fine - 76800.0;
        var2 = (double) this.dig_H4 * 64.0 +
                ((double) this.dig_H5 / 16384.0) * var1;
        var3 = (double) rawHumidity - var2;
        var4 = (double) this.dig_H2 / 65546.0;
        var5 = 1.0 + ((double) this.dig_H3 / 67108864.0) * var1;
        var6 = 1.0 + ((double) this.dig_H6 / 67108864.0) * var1 * var5;
        var6 *= var3 * var4 * var5;
        hum = var6 * (1.0 - (double) this.dig_H1 * var6 / 524288.0);

        if (hum > 100.0) {
            return 100.0;
        } else if (hum < 0.0) {
            return 0.0;
        }

        return hum;
    }
}

class BME280RegisterAddresses {

    static final int id = 0xD0;
    static final int reset = 0xE0;
    static final int ctrl_hum = 0xF2;
    static final int ctrl_meas = 0xF4;
    static final int press_msb = 0xF7;

    // Compensation registers (1x8 bits word)
    static final int dig_H1 = 0xA1;
    static final int dig_H3 = 0xE3;
    static final int dig_H6 = 0xE7;

    // Compensation registers (2x8 bits words)
    static final int dig_T1 = 0x88;
    static final int dig_T2 = 0x8A;
    static final int dig_T3 = 0x8C;

    static final int dig_P1 = 0x8E;
    static final int dig_P2 = 0x90;
    static final int dig_P3 = 0x92;
    static final int dig_P4 = 0x94;
    static final int dig_P5 = 0x96;
    static final int dig_P6 = 0x98;
    static final int dig_P7 = 0x9A;
    static final int dig_P8 = 0x9C;
    static final int dig_P9 = 0x9E;
    static final int dig_H2 = 0xE1;

    // Compensation registers (12 bits)
    static final int dig_H4 = 0xE4;
    static final int dig_H5 = 0xE5;
}

class BME280RegisterValues {

    static final int id = 0x60;
    static final int reset = 0xB6;
    static final int press_and_temp_osrs_1smpl = 0x25;
}

class BME280RegisterMasks {

    static final int ctrl_hum_osrs_h_1smpl = 0x01;
    static final int forced_mode = 0x01;
}
