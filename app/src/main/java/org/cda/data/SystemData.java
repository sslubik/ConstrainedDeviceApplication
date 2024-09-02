package org.cda.data;

import org.cda.data.enums.MessageType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SystemData extends AbstractDataMessage {

    public SystemData() {
        super.messageType = MessageType.SYSTEM_DATA.getString();
    }

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
