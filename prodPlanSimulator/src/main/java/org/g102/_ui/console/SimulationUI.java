package org.g102._ui.console;

import org.g102._controllers.SimulationController;
import org.g102.domain.Item;
import org.g102.domain.Machine;
import org.g102.domain.Time;
import org.g102.tools.ConsoleWriter;
import org.g102.tools.InputReader;
import org.g102._ui.console._Menu.MenuUI;

import java.util.*;

public class SimulationUI implements Runnable{

    private SimulationController simulationController;

    public SimulationUI(){
        simulationController = new SimulationController();
    }

    public SimulationUI(List<Machine> machines, List<Item> items){
        ConsoleWriter.displayHeader("Machine Simulation");
        simulationController = new SimulationController(false, machines, items);
        printPreQueue();
        printEndStats();
        showIntermediateMenu();
    }

    @Override
    public void run() {
        ConsoleWriter.displayHeader("Machine Simulation");
        boolean usePriority = InputReader.readBoolean("Do you want to use priority in the simulation? (y/n)");
        simulationController = new SimulationController(usePriority);
        getFilePaths();
        printPreQueue();
        printEndStats();
        showMenu();
    }

    private void getFilePaths() {
        String machineFilePath = InputReader.read("Enter the path to the machines file");
        String itemFilePath = InputReader.read("Enter the path to the items file");
        if (machineFilePath.isEmpty()) {
            machineFilePath = "prodPlanSimulator/src/main/resources/workstations.csv";
        }
        if (itemFilePath.isEmpty()) {
            itemFilePath = "prodPlanSimulator/src/main/resources/articles.csv";
        }
        simulationController.setMACHINE_FILE_PATH(machineFilePath);
        simulationController.setITEM_FILE_PATH(itemFilePath);
    }

    public void showMenu(){
        ConsoleWriter.displayHeader("Menu");
        int selectedOption = InputReader.showListReturnIndex(List.of("Display Machine Dependency Flow","Display Machine Statistics",
                "Display Operation Statistics","Simulate Again", "Go Back"), null);
        switch (selectedOption) {
            case 1:
                showMachineDependency();
                showMenu();
                break;
            case 2:
                showMachineOperatingPerc();
                showMenu();
                break;
            case 3:
                showOperationStatistics();
                showMenu();
                break;
            case 4:
                run();
                break;
            case 5:
                MenuUI.showMenu();
                break;
        }
    }

    public void showIntermediateMenu(){
        ConsoleWriter.displayHeader("Menu");
        int selectedOption = InputReader.showListReturnIndex(List.of("Display Machine Dependency Flow","Display Machine Statistics",
                "Display Operation Statistics", "Continue"), null);
        switch (selectedOption) {
            case 1:
                showMachineDependency();
                showIntermediateMenu();
                break;
            case 2:
                showMachineOperatingPerc();
                showIntermediateMenu();
                break;
            case 3:
                showOperationStatistics();
                showIntermediateMenu();
                break;
            case 4:
                break;
        }
    }

    public void showMachineDependency(){
        List<Map.Entry<Machine, Integer>> machineDependency = simulationController.getMachineDependency();
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Machine, Integer> entry : machineDependency) {
            sb.append(entry.getKey().getId()).append(" : [");
            for (Machine m : entry.getKey().getMachineDependency().keySet()) {
                sb.append("(").append(m.getId()).append(",").append(entry.getKey().getMachineDependency().get(m)).append(")").append(" , ");
            }
            sb = new StringBuilder(sb.substring(0, sb.length() - 3));
            sb.append("] (").append(entry.getValue()).append(")\n");
        }

        if (sb.length() > 0) {
            System.out.println(sb);
        } else {
            ConsoleWriter.displayError("No dependencies found.");
        }
    }

    public void showMachineOperatingPerc(){
        Map<Machine, Float> machineOperatingPerc = simulationController.getMachineOperatingPerc();
        Map<String, Time> operationTimes = simulationController.getOperationTimes();

        for (Machine machine : machineOperatingPerc.keySet()) {
            if(machine.getTimeActiveInInt() > 0){
                float timeOperationInPerc = (float) (machine.getTimeActiveInInt() * 100) / operationTimes.get(machine.getOperationName()).toSeconds();
                System.out.printf("Machine %s| OPERATING TIME: %ss - %.2f%% of total production time - %.2f%% of total %s time.\n", machine.getId(), new Time(machine.getTimeActiveInInt()), machineOperatingPerc.get(machine), timeOperationInPerc, machine.getOperationName());
            }
        }
    }

    public void showOperationStatistics(){
        Map<String, Float> operationAverages = simulationController.getOperationAverages();
        Map<String, Float> waitingAverages = simulationController.getWaitingAverages();
        Map<String, Time> operationTimes = simulationController.getOperationTimes();

        for (String s : operationAverages.keySet()) {
            System.out.printf("Operation " + s + " average time: %.2fs\n", operationAverages.get(s));
            System.out.printf("Operation " + s + " average waiting time: %.2fs\n", waitingAverages.get(s));
        }
        System.out.println("\nExecution times by operation:");
        for (String operation : operationTimes.keySet()) {
            System.out.println(operation + " total execution time: " + operationTimes.get(operation) + "s");
        }
    }

    private void printPreQueue() {
        Map<Machine, Time> machineTimeRemaining = simulationController.getPreTimeRemaining();
        Map<Machine, Queue<Item>> machineItemQueue = simulationController.getPreQueue();

        ConsoleWriter.displayHeader("PRE QUEUE");
        for (Machine machine : machineItemQueue.keySet()) {
            if (machineItemQueue.containsKey(machine)) {
                System.out.println("------------------");
                System.out.printf("Machine %s: %s (~%ss)\n", machine.getId(), machine.getOperationName(), machineTimeRemaining.get(machine).toString());
                Queue<Item> itemQueue = machineItemQueue.get(machine);
                System.out.print("Item(s): ");
                StringBuilder subString = new StringBuilder();
                for (Item item : itemQueue) {
                    subString.append(item.getId()).append(", ");
                }
                if (subString.length() > 2) {
                    subString = new StringBuilder(subString.substring(0, subString.length() - 2));
                }
                System.out.println(subString);
            }
        }
        System.out.println("------------------");
    }

    private void printEndStats(){
        Time simulationTime = simulationController.getSimulationTime();
        int finishedProducts = simulationController.getFinishedProducts();
        ConsoleWriter.displayHeader("SIMULATION FINISHED");
        System.out.print("TOTAL PRODUCTION TIME: " + simulationTime + "s ("+simulationTime.toSeconds()+"s).\n");
        System.out.print("FINISHED PRODUCTS: " + finishedProducts + "\n");
    }
}
