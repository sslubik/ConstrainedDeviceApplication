package org.cda.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SystemData {

    // CPU related parameters
    double cpuTemp;
    double cpuLoadAvg;

    // Disk related parameters
    double diskUsedSpace;
    double diskTotalSpace;

    // Memory related parameters
    double memoryUsed;
    double memoryTotal;
}
