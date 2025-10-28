package org.g102._controllers;

import org.g102.domain.*;

import java.util.*;
import java.util.List;

import static java.util.Collections.reverseOrder;

public class SimulationController {
    private String MACHINE_FILE_PATH = "prodPlanSimulator/src/main/resources/workstations.csv";
    private String ITEM_FILE_PATH = "prodPlanSimulator/src/main/resources/articles.csv";
    private Simulator simulator;
    public SimulationController(){}
    public SimulationController(boolean usePriority) {
        simulator = new Simulator(usePriority, MACHINE_FILE_PATH, ITEM_FILE_PATH);
    }

    public SimulationController(boolean usePriority, List<Machine> machines, List<Item> items) {
        simulator = new Simulator(usePriority, machines, items);
    }

    public void setMACHINE_FILE_PATH(String MACHINE_FILE_PATH) {
        this.MACHINE_FILE_PATH = MACHINE_FILE_PATH;
    }

    public void setITEM_FILE_PATH(String ITEM_FILE_PATH) {
        this.ITEM_FILE_PATH = ITEM_FILE_PATH;
    }

    public List<Map.Entry<Machine, Integer>> getMachineDependency() {
        return simulator.getMachineDependency();
    }

    public Map<String, Float> getOperationAverages() {
        return simulator.getOperationAverages();
    }

    public Map<String, Float> getWaitingAverages() {
        return simulator.getWaitingAverages();
    }

    public Map<Machine, Float> getMachineOperatingPerc() {
        return simulator.getMachineOperatingPerc();
    }

    public Map<String, Time> getOperationTimes() {
        return simulator.getOperationTimes();
    }

    public Map<Machine, Queue<Item>> getPreQueue() {
        return simulator.getPreQueue();
    }

    public Map<Machine, Time> getPreTimeRemaining() {
        return simulator.getPreTimeRemaining();
    }

    public int getSimulationTimeInt() {
        return simulator.getSimulationTimeInt();
    }

    public Time getSimulationTime(){
        return new Time(simulator.getSimulationTimeInt());
    }

    public int getFinishedProducts() {
        return simulator.getFinishedProducts();
    }

}