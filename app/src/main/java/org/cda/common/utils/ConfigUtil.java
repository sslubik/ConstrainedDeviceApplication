package org.cda.common.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.cda.common.enums.*;

/**
 * Class responsible for managing the config file.
 */
public class ConfigUtil {

    private final static ConfigUtil instance = new ConfigUtil();
    private final Properties properties = new Properties();

    /**
     * Private default constructor.
     *
     */
    private ConfigUtil() {
        final String configPath = "config.props";

        try (InputStream config = getClass().getClassLoader().getResourceAsStream(configPath)) {
            if (config == null) {
                throw new FileNotFoundException(
                        "Failed to read config.props file! Make sure it's located in app/src/main/resources directory.");
            }

            this.properties.load(config);

        } catch (IOException e) {
            e.printStackTrace();

            System.exit(1);
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
