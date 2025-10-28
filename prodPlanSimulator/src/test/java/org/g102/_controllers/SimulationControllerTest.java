/*package org.g102.controllers;

import org.g102.domain.Time;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.g102.domain.Item;
import org.g102.domain.Machine;
import org.junit.jupiter.api.Assertions;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class SimulationControllerTest {
    private static final String MACHINE_FILE_PATH = "../prodPlanSimulator/src/main/resources/workstationsTest.csv";
    private static final String ITEM_FILE_PATH = "../prodPlanSimulator/src/main/resources/articlesTest.csv";
    static List<Machine> machines = new ArrayList<>();
    SimulationController simulationController = new SimulationController();
    @BeforeEach
    void setUp() {
        simulationController.runSimulation(true, true);
    }

    @Test
    void getMachineList() {
        assertEquals(6, SimulationController.machines.size());
    }

    @Test
    void getItemsList() {
        assertEquals(4, SimulationController.items.size());
    }

    @Test
    void verifyFastestMachine() {
        assertTrue(SimulationController.verifyFastestMachine(SimulationController.machines.get(0), SimulationController.items.get(0)));
    }

    @Test
    void getLastMachineEndTimeWithPriority() {
        assertEquals(684, SimulationController.getLastMachineEndTime());
    }
    @Test
    void getLastMachineEndTimeWithoutPriority() {
        simulationController.runSimulation(false, true);
        assertEquals(724, SimulationController.getLastMachineEndTime());
    }

    @Test
    void simulationTime(){
        assertEquals(684, SimulationController.simulationTime);
    }

    @Test
    void finishedProducts(){
        assertEquals(4, SimulationController.finishedProducts);
    }

    @Test
    void machineDependency(){
        SimulationController.machineDependency();
        machines = SimulationController.machines;

        Map<Machine, Integer> machineDependency = new LinkedHashMap<>();

        machineDependency.put(machines.get(3), 4);
        machineDependency.put(machines.get(4), 4);
        machineDependency.put(machines.get(5), 3);

        assertEquals(machineDependency.get(machines.get(3)), machines.get(0).getMachineDependency().get(machines.get(3)));
        assertEquals(machineDependency.get(machines.get(4)), machines.get(3).getMachineDependency().get(machines.get(4)));
        assertEquals(machineDependency.get(machines.get(5)), machines.get(4).getMachineDependency().get(machines.get(5)));
    }

    @Test
    void printMachineOperatingPerc() {
        Map<Machine, Float> machineOperatingPercentage = new LinkedHashMap<>();
        machines = SimulationController.machines;
        machineOperatingPercentage.put(machines.get(0), 6.57277f);
        machineOperatingPercentage.put(machines.get(3), 23.787167f);
        machineOperatingPercentage.put(machines.get(4), 31.611893f);
        machineOperatingPercentage.put(machines.get(5), 38.028168f);

        Assertions.assertEquals(machineOperatingPercentage, SimulationController.printMachineOperatingPerc());
    }
}
*/