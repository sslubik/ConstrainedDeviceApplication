package org.cda.devices;

import java.util.concurrent.Callable;

public abstract class SensorTask
        extends AbstractSensor
        implements Callable<Void> {
}
