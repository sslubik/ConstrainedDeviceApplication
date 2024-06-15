package org.cda.common.enums;

import lombok.Getter;

@Getter
public enum Units {

    CELSIUS("°C"),
    PERCENT("%"),
    MEGABYTE("MB"),
    GIGABYTE("GB"),
    SECOND("s"),
    MILISECOND("ms");

    private final String name;

    private Units(String unit) {
        this.name = unit;
    }
}
