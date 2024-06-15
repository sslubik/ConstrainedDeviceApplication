package org.cda.common.enums;

import lombok.Getter;

@Getter
public enum Parameters {

    TEMPERATURE("Temperature"),
    USAGE("Usage"),
    TOTAL_SPACE("Total space"),
    USED_SPACE("Used space"),
    TOTAL_MEMORY("Total memory"),
    USED_MEMORY("Used memory"),
    UPLOAD("Upload"),
    DOWNLOAD("Download"),
    PING("Ping");

    private final String name;

    Parameters(String name) {
        this.name = name;
    }
}
