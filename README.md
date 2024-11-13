# Constrained Device Application
An application for edge devices in IoT Weather Station solutions.

## About
This software was developed for Raspberry Pi boards as the edge device layer in an IoT Weather Station system, which was the subject of my thesis. It is intended to work in production with BME280 and YL-83 sensors, but you can test the connection to Azure from any device running Java.  

The software was designed with scalability in mind, making it as seamless as possible to add additional sensors.

## Prerequisites 
 - An Azure account with the IoT Hub service enabled and at least one registered device
 - Java 11 or newer
 - Gradle 8.10 or newer for building the project (optional)

## Usage
**Warning:** If you intend to use the application on a Raspberry Pi with sensors, make sure the I2C bus is enabled beforehand.  

If you want to modify the source code and build a new `.jar` file, simply execute the following command after making the changes:

```bash
$ gradle shadowJar
```

To run the application, navigate to `app/build/jar` subdirectory and create a new file named `config.props`. Copy the content from the `config.props.template` file into the newly created file, and update the required parameters.
Once it's done, simply run the following command:

```bash
$ java -jar cda-X.X.X.jar
```

Enjoy!
