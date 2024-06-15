package org.cda.system;

import org.cda.common.enums.Units;
import org.cda.common.enums.Parameters;

import lombok.Getter;

@Getter
public class SystemData {

    private Parameters parameter;
    private double value;
    private Units unit;

    public SystemData(Parameters parameter, double value, Units unit) {
        this.parameter = parameter;
        this.value = value;
        this.unit = unit;
    }
}
