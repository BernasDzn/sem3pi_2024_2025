package org.g102.domain;

import org.g102.domain.Machine;
import org.g102.domain.MachineStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import java.util.Date;

class MachineTest {

    Machine machine = new Machine("1", "Machine A", new Date(), new Date(), MachineStatus.ACTIVE, "Cutting", 5);

    @Test
    void getOperationName() {
        Assertions.assertEquals("Cutting", machine.getOperationName());
    }

    @Test
    void getOperationTime() {
        Assertions.assertEquals(5, machine.getOperationTime().toSeconds());
    }

    @Test
    void setOperationName() {
        machine.setOperationName("Welding");
        Assertions.assertEquals("Welding", machine.getOperationName());
    }

    @Test
    void setOperationTime() {
        machine.setOperationTime(10);
        Assertions.assertEquals(10, machine.getOperationTime().toSeconds());
    }
}