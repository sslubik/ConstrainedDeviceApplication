package org.cda.system.enums;

import lombok.Getter;

@Getter
public enum SystemResources {

    CPU("CPU"),
    MEMORY("Memory"),
    DISK("Disk");

    private final String name;

    private SystemResources(String name) {
        this.name = name;
    }
}
