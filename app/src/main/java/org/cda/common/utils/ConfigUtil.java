package org.cda.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Properties;

import org.cda.common.enums.ConfigBooleans;
import org.cda.common.enums.ConfigFloats;
import org.cda.common.enums.ConfigIntegers;
import org.cda.common.enums.ConfigStrings;

import lombok.extern.slf4j.Slf4j;

/**
 * Class responsible for managing the config file.
 */
@Slf4j
public class ConfigUtil {

    private final static ConfigUtil instance = new ConfigUtil();
    private final Properties properties = new Properties();

    /**
     * Private default constructor.
     *
     */
    private ConfigUtil() {
        try {
            final String jarPath = new File(
                    ConfigUtil.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath();
            File jarFile = new File(jarPath);
            File jarParentDir = jarFile.getParentFile();

            File configFile = new File(jarParentDir, "config.props");

            if (!configFile.exists()) {
                ConfigUtil.log.error("The config file \"config.props\" in the .jar direcotry does not exist! Aborting...");
                System.exit(1);
            }

            FileInputStream fis = new FileInputStream(configFile);
            this.properties.load(fis);
            fis.close();

            ConfigUtil.log.info("Successfully loaded app configuration form config.props!");

        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns singleton instance.
     *
     */
    public static ConfigUtil getInstance() {
        return ConfigUtil.instance;
    }

    /**
     * Returns a value loaded from config.props file.
     *
     */
    public String getString(ConfigStrings key) {
        return this.properties.getProperty(key.name());
    }

    /**
     * Returns a value loaded from config.props file parsed as an integerr
     *
     * @throws IllegalStateException when the parameter cannot be parsed as an int
     *                               and the application didn't terminate.
     *
     */
    public int getInteger(ConfigIntegers key) {
        try {
            String property = this.properties.getProperty(key.name());

            return Integer.parseInt(property);

        } catch (NumberFormatException e) {
            System.err.println("Error: failed to parse " + key.name()
                    + " as an int! Make sure the value is properly assigned in app/config/config.props");

            System.exit(1);
        }

        throw new IllegalStateException("Unexpected Error: failed to shut down the application after logging an error");
    }

    /**
     * Returns a value loaded form config.props file parsed as a float.
     *
     * @throws IllegalStateException when the parameter cannot be parsed as a float
     *                               and the application didn't terminate.
     *
     */
    public float getFloat(ConfigFloats key) {
        try {
            String property = this.properties.getProperty(key.name());

            return Float.parseFloat(property);

        } catch (NumberFormatException e) {
            System.err.println("Error: failed to parse " + key.name()
                    + " as a float! Make sure the value is properly assigned in app/config/config.props");

            System.exit(1);
        }

        throw new IllegalStateException("Unexpected Error: failed to shut down the application after logging an error");
    }

    /**
     * Returns a boolean loaded from config.props file.
     *
     */
    public boolean getBoolean(ConfigBooleans key) {
        String property = this.properties.getProperty(key.name());

        return Boolean.parseBoolean(property);
    }
}
