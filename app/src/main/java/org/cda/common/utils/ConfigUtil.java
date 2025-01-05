package org.cda.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Properties;

import org.cda.common.enums.ConfigBooleans;
import org.cda.common.enums.ConfigIntegers;
import org.cda.common.enums.ConfigStrings;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConfigUtil {

    private final static ConfigUtil instance = new ConfigUtil();
    private final Properties properties = new Properties();

    private ConfigUtil() {
        try {
            File jarFile = new File(
                    ConfigUtil.class
                            .getProtectionDomain()
                            .getCodeSource()
                            .getLocation()
                            .toURI());

            File jarParentDir = jarFile.getParentFile();

            File configFile = new File(jarParentDir, "config.props");

            if (!configFile.exists()) {
                ConfigUtil.log
                        .error("The config file \"config.props\" in the .jar "
                                + "direcotry does not exist! Aborting...");
                System.exit(1);
            }

            FileInputStream fis = new FileInputStream(configFile);
            this.properties.load(fis);
            fis.close();

            ConfigUtil.log.info(
                    "Successfully loaded app configuration form config.props!");

        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
    }

    public static ConfigUtil getInstance() {
        return ConfigUtil.instance;
    }

    public String getString(ConfigStrings key) {
        return this.properties.getProperty(key.name());
    }

    public int getInteger(ConfigIntegers key) {
        try {
            String property = this.properties.getProperty(key.name());

            return Integer.parseInt(property);

        } catch (NumberFormatException e) {
            System.err.println("Error: failed to parse " + key.name()
                    + " as an int! "
                    + "Make sure the value is properly assigned in "
                    + "app/config/config.props");

            System.exit(1);
        }

        throw new IllegalStateException("Unexpected Error: "
                + "failed to shut down "
                + "the application after logging an error");
    }

    public boolean getBoolean(ConfigBooleans key) {
        String property = this.properties.getProperty(key.name());

        return Boolean.parseBoolean(property);
    }
}
