package org.g102.domain;

import org.g102._controllers.TimelineController;
import org.g102.tools.ConsoleWriter;
import org.g102.tools.file.FileReader;

import java.util.*;
import java.util.List;

import static java.util.Collections.reverseOrder;

public class Simulator {

    private boolean pFlag;
    private int simulationTime;
    private int finishedProducts;
    private List<Machine> machines = new ArrayList<>();
    private List<Item> items = new ArrayList<>();
    private Map<Machine, Queue<Item>> machineItemQueue = new LinkedHashMap<>();
    private Map<Machine, Time> machineTimeRemaining = new LinkedHashMap<>();
    private Map<Machine, Queue<Item>> preQueue = new LinkedHashMap<>();
    private Map<Machine, Time> preTimeRemaining = new LinkedHashMap<>();
    private Map<String, Time> operationTimes = new LinkedHashMap<>();
    private Map<String, Float> operationAverages = new LinkedHashMap<>();
    private Map<String, Float> waitingAverages = new LinkedHashMap<>();
    private List<Map.Entry<Machine, Integer>> machineDependency = new ArrayList<>();
    private  Map<Machine, Float> machineOperatingPerc = new LinkedHashMap<>();

    /**
     * Simulator constructor
     *
     * @param usePriority A boolean flag indicating whether to use priority in the simulation.
     * @param machineFilePath The path to the CSV file containing the machine data.
     * @param itemFilePath The path to the CSV file containing the item data.
     */
    public Simulator(boolean usePriority, String machineFilePath, String itemFilePath) {
        machines = getMachineList(FileReader.readCSV(machineFilePath, true, ";"));
        items = getItemsList(FileReader.readCSV(itemFilePath, true, ";"));
        //Set priority flag
        pFlag = usePriority;

        runSimulation();
    }

    public Simulator(boolean usePriority, List<Machine> machines, List<Item> items) {
        this.machines = machines;
        this.items = items;
        //Set priority flag
        pFlag = usePriority;

        runSimulation();
    }

    /**
     * Runs the simulation.
     */
    private void runSimulation() {
        simulationTime = 0;
        if(machines == null || items == null){
            ConsoleWriter.displayError("There was an error processing the data. Please check the CSV files.");
            return;
        }

        preQueue = addItemToQueue(items, preQueue);
        machineItemQueue = addItemToQueue(items, machineItemQueue);

        preTimeRemaining = new LinkedHashMap<>(machineTimeRemaining);

        simulateProduction();
        updateOperationTimes();
        calculateAverageTimePerOperation(machines, operationAverages, waitingAverages);
        machineDependency = calcMachineDependency(items, machines);
        machineOperatingPerc =  calcMachineOperatingPerc(machines, items);
        TimelineController.timelinePlantUML(machines, false);
        TimelineController.itemTimeLinePlantUML(machines, items, false);
    }

    /**
     * Simulates the production process.
     * This method simulates the production process by iterating over all machines and items.
     * It calculates the minimum time among all machines and updates the simulation time.
     * It then processes the items in the machine queues and updates the machine time remaining.
     * The method continues to simulate the production process until all items are processed.
     * Time worst case time complexity of this method is O(i^6 * m^4) where i is the number of items and m is the number of machines.
     * This is taking into account the nested loops and the method calls.
     */
    public void simulateProduction() {
        int minTime;
        while (!machineItemQueue.isEmpty()) {
            minTime = getMinTime();
            simulationTime += minTime;
            for (Machine m : machineTimeRemaining.keySet()) {
                if (machineItemQueue.containsKey(m)) {
                    if (machineTimeRemaining.get(m).toSeconds() - minTime == 0) {
                        machineTimeRemaining.replace(m, new Time(0));
                    } else {
                        machineTimeRemaining.replace(m, new Time(machineTimeRemaining.get(m).toSeconds() - minTime));
                    }
                }
            }
            Map<Machine, Time> machineTimeCopy = new LinkedHashMap<>(machineTimeRemaining);
            for (Machine machine : machineTimeCopy.keySet()) {
                if (machineTimeRemaining.containsKey(machine) && machineTimeRemaining.get(machine).toSeconds() == 0) {
                    if (machineItemQueue.containsKey(machine) && !machineItemQueue.get(machine).isEmpty()) {
                        Queue<Item> itemQueue = machineItemQueue.get(machine);
                        Item item = itemQueue.poll();
                        if (item != null) {
                            item.setProductionTime(item.getProdTime() + machine.getOperationTime().toSeconds());
                            item.addMachineUsed(machine);
                            machine.addItemProcessedTime(new Time (simulationTime-machine.getOperationTime().toSeconds()), item);
                            if (item.getNextOperation() == item.getOperations().size() - 1) {
                                //(Faz parte de uma US acho eu, ent temos q trocar para a  UI dps)
                                //System.out.println(ANSIColors.paint("ITEM " + item.getId() + " FINISHED PRODUCTION", ANSIColors.ANSI_CYAN_BACKGROUND));
                                //System.out.println("PRODUCTION TIME: " + new Time(item.getProdTime()) + "s");
                                finishedProducts++;
                            } else {
                                item.setNextOperation(item.getNextOperation() + 1);
                                this.machineItemQueue = addItemToQueue(Collections.singletonList(item), machineItemQueue);
                            }
                            if (machineItemQueue.get(machine).isEmpty()) {
                                machineItemQueue.remove(machine);
                                machineTimeRemaining.remove(machine);
                                machine.setEndTime(new Time(simulationTime));
                            }
                        }
                    }
                }
            }
            for (Machine machine : machineTimeRemaining.keySet()) {
                if (machineTimeRemaining.get(machine).toSeconds() == 0) {
                    machineTimeRemaining.put(machine, machine.getOperationTime());
                }
            }
        }
    }

    /**
     * Updates the operation times.
     * This method iterates over all machines and updates the operation times.
     * Time complexity of this method is O(m) where m is the number of machines.
     */
    private void updateOperationTimes() {
        for (Machine machine : machines) {
            if(!operationTimes.containsKey(machine.getOperationName())){
                operationTimes.put(machine.getOperationName(), new Time(machine.getTimeActiveInInt()));
            }else{
                operationTimes.put(machine.getOperationName(), new Time(operationTimes.get(machine.getOperationName()).toSeconds() + machine.getTimeActiveInInt()));
            }
        }
    }

    /**
     * Calculates the average time per operation.
     * This method calculates the average time per operation and the average waiting time per operation.
     * It iterates over all machines and calculates the average time and waiting time for each operation.
     * It then prints the average time and waiting time for each operation.
     * Time complexity of this method is O(2op + op*m) where op is the number of operations and m is the number of machines.
     */
    //Maybe divide the method in 2 different methods that return operation averages and waiting averages separately
    public void calculateAverageTimePerOperation(List<Machine> machines, Map<String, Float> operationAverages, Map<String, Float> waitingAverages) {
        HashSet<String> operations = new HashSet<>();
        for (Machine machine : machines) {
            operations.add(machine.getOperationName());
        }

        for (String op : operations) {
            int i = 0;
            int j = 0;
            int sum = 0;
            int sum2 = 0;
            for (Machine machine : machines) {
                if (machine.getOperationName().equals(op) && !machine.getTimeActive().isEmpty()) {
                    i += machine.getActiveTimesLengths().size();
                    j += machine.getWaitingTimeLengths().size();
                    for (Integer t : machine.getActiveTimesLengths()) {
                        sum += t;
                    }
                    for (Integer t : machine.getWaitingTimeLengths()) {
                        sum2 += t;
                    }
                }
            }
            if (sum != 0)
                operationAverages.put(op, (float) sum / i);
            else
                operationAverages.put(op, 0.0f);
            if (sum2 != 0)
                waitingAverages.put(op, (float) sum2 / j);
            else
                waitingAverages.put(op, 0.0f);
        }
    }

    /**
     * Gets the operation averages.
     * @return The operation averages.
     */
    public Map<String, Float> getOperationAverages() {
        return operationAverages;
    }

    /**
     * Gets the waiting averages.
     * @return The waiting averages.
     */
    public Map<String, Float> getWaitingAverages() {
        return waitingAverages;
    }

    /**
     * Adds items to the machine queues.
     * This method iterates over all items and assigns them to the appropriate machine queue.
     * It checks if the machine is available and if the item can be assigned to the machine.
     * If the machine is available and the item can be assigned to the machine, the item is added to the machine queue.
     * Time complexity of this method is O(i^5 * m^3) where i is the number of items and m is the number of machines.
     *
     * @param items A list of items to be added to the machine queues.
     */
    public Map<Machine, Queue<Item>> addItemToQueue(List<Item> items, Map<Machine, Queue<Item>> machineItemQueue) {
        boolean assigned = false;
        for (Item item : items) {
            for (Machine machine : machines) {
                if (item.getOperations().get(item.getNextOperation()).equals(machine.getOperationName()) && !assigned) {
                    if (verifyFastestMachine(machine, item, machineItemQueue)) {
                        if (machineItemQueue.containsKey(machine)) {
                            machineItemQueue.get(machine).add(item);
                            //ConsoleWriter.displayLog("ITEM " + item.getId() + ANSIColors.paint(" ASSIGNED ", ANSIColors.ANSI_LIGHT_GREEN) + "TO MACHINE " + machine.getId());
                            if (pFlag) machineItemQueue = organizeQueue(machineItemQueue, machineTimeRemaining);
                        } else {
                            Queue<Item> itemQueue = new LinkedList<>();
                            itemQueue.add(item);
                            machineItemQueue.put(machine, itemQueue);
                            machineTimeRemaining.put(machine, machine.getOperationTime());
                            machine.setTimeActive(new Time(simulationTime), new Time(0));
                            //ConsoleWriter.displayLog("ITEM " + item.getId() + ANSIColors.paint(" ASSIGNED ", ANSIColors.ANSI_LIGHT_GREEN) + "TO MACHINE " + machine.getId());
                        }

                        assigned = true;
                    }
                }
            }
            assigned = false;
        }

        return machineItemQueue;
    }

    /**
     * Organizes the machine queues based on the priority of the items.
     * This method iterates over all machines and their queues and checks if the items in the queue need to be reorganized.
     * If the items in the queue need to be reorganized, the method swaps the items in the queue based on their priority.
     * The method then adds the unassigned items to the machine queues.
     * The method continues to reorganize the machine queues until no changes are made.
     * Time complexity of this method is O(i^3 * m) where i is the number of items and m is the number of machines.
     * If there were changes in the queue, the method calls the addItemToQueue method to add the unassigned items to the machine queues.
     */
    public Map<Machine, Queue<Item>> organizeQueue(Map<Machine, Queue<Item>> machineItemQueue, Map<Machine, Time> machineTimeRemaining) {
        boolean changesInQueue = false;
        List<Item> unassignedItems = new ArrayList<>();

        for (Machine machine : machines) {
            if (machineItemQueue.containsKey(machine) && machineItemQueue.get(machine).size() > 1) {
                Queue<Item> itemQueue = machineItemQueue.get(machine);
                List<Item> itemList = new ArrayList<>(itemQueue);

                int iIndex = (machineTimeRemaining.get(machine).toSeconds() < machine.getOperationTime().toSeconds()) ? 1 : 0;

                for (int i = iIndex; i < itemList.size(); i++) {
                    for (int j = i; j < itemList.size(); j++) {
                        if (checkPriority(itemList.get(i), itemList.get(j))) {
                            //ConsoleWriter.displayLog("ITEM " + itemList.get(i).getId() + ANSIColors.paint(" SWAPPED ", ANSIColors.ANSI_RED) + "WITH " + itemList.get(j).getId());
                            changesInQueue = true;
                            unassignedItems.addAll(itemList.subList(i, itemList.size()));
                            Item temp = itemList.get(j);
                            itemList = itemList.subList(0, i);
                            itemList.addLast(temp);
                            unassignedItems.remove(temp);
                            break;
                        }
                    }
                    if (changesInQueue) break;
                }

                itemQueue.clear();
                itemQueue.addAll(itemList);
            }
        }

        if (changesInQueue) machineItemQueue = addItemToQueue(unassignedItems, machineItemQueue);

        return machineItemQueue;
    }

    /**
     * Gets the simulation time.
     * @return The simulation time.
     */
    public Map<Machine, Queue<Item>> getPreQueue() {
        return preQueue;
    }

    public Map<Machine, Time> getPreTimeRemaining() {
        return preTimeRemaining;
    }

    /**
     * Compares the priority of two items.
     * This method compares the priority of two items and returns true if the first item has a higher priority than the second item.
     *
     * @param item The first item to compare.
     * @param item2 The second item to compare.
     *
     * @return True if the first item has a lower priority than the second item because priority
     * is an enumerator, false otherwise.
     */
    private boolean checkPriority(Item item, Item item2) {
        return item.getPriority().compareTo(item2.getPriority()) > 0;
    }

    /**
     * Verifies if the machine is the fastest machine for the item.
     * This method iterates over all machines and checks if the machine is the fastest machine for the item.
     * Time complexity of this method is O(m * i) where m is the number of machines and i is the number of items.
     *
     * @param currentMachine The machine to check if it is the fastest machine for the item.
     * @param currentItem The item to check if the machine is the fastest machine for it.
     *
     * @return True if the machine is the fastest machine for the item, false otherwise.
     */
    public boolean verifyFastestMachine(Machine currentMachine, Item currentItem, Map<Machine, Queue<Item>> machineItemQueue) {
        for (Machine m : machines) {
            if (m.getOperationName().equals(currentItem.getOperations().get(currentItem.getNextOperation())) && !Objects.equals(m.getId(), currentMachine.getId())) {
                if (machineItemQueue.containsKey(m) || machineItemQueue.containsKey(currentMachine)) {
                    int queueTime;
                    if (machineItemQueue.containsKey(m) && machineItemQueue.containsKey(currentMachine)) {
                        int queueTime2;
                        queueTime = machineTimeRemaining.get(m).toSeconds() + calcTime(machineItemQueue.get(m), currentItem, m.getOperationTime().toSeconds());
                        queueTime2 = machineTimeRemaining.get(currentMachine).toSeconds() + calcTime(machineItemQueue.get(currentMachine),
                                currentItem, currentMachine.getOperationTime().toSeconds());
                        if (queueTime < queueTime2) return false;
                    } else if (machineItemQueue.containsKey(m)) {
                        queueTime = machineTimeRemaining.get(m).toSeconds() + calcTime(machineItemQueue.get(m), currentItem, m.getOperationTime().toSeconds());
                        if (queueTime < currentMachine.getOperationTime().toSeconds()) return false;
                    } else if (machineItemQueue.containsKey(currentMachine)) {
                        queueTime = machineTimeRemaining.get(currentMachine).toSeconds() + calcTime(machineItemQueue.get(currentMachine),
                                currentItem, currentMachine.getOperationTime().toSeconds());
                        if (m.getOperationTime().toSeconds() < queueTime) return false;
                    }
                } else {
                    if (m.getOperationTime().toSeconds() < currentMachine.getOperationTime().toSeconds()) return false;
                }
            }
        }
        return true;
    }

    /**
     * Calculates the time needed to process the items in the queue.
     * This method calculates the time needed to process the items in the queue.
     * Time complexity of this method is O(i) where i is the number of items.
     *
     * @param items The items in the queue.
     * @param item The item to calculate the time needed to process.
     * @param time The time needed to process the item.
     *
     * @return The time needed to process the items in the queue.
     */
    private int calcTime(Queue<Item> items, Item item, int time) {
        int queueTime = 0;
        for (Item i : items) {
            if (pFlag) {
                if (!checkPriority(i, item)) {
                    queueTime += time;
                }
            } else queueTime += time;
        }
        return queueTime;
    }

    /**
     * Gets the minimum time among all machines.
     * This method iterates over all machines and gets the minimum time among all machines.
     * Time complexity of this method is O(m) where m is the number of machines.
     *
     * @return The minimum time among all machines.
     */
    private int getMinTime() {
        int minTime = Integer.MAX_VALUE;
        for (Machine machine : machineTimeRemaining.keySet()) {
            if (machineTimeRemaining.get(machine).toSeconds() < minTime) {
                minTime = machineTimeRemaining.get(machine).toSeconds();
            }
        }
        return minTime;
    }

    /**
     * Prints the machine dependency ordered in descendent order by processed items.
     * This method iterates over all machines and items and calculates the machine dependency.
     * It then prints the machine dependency ordered in descendent order by processed items.
     * The method continues to print the machine dependency until all machines are processed.
     * Time complexity of this method is O(m^2 * i) where m is the number of machines and i is the number of items.
     */
    public List<Map.Entry<Machine, Integer>> calcMachineDependency(List<Item> items, List<Machine> machines) {
        //printMachinesUsedForEachItem();
        for (Item item : items) {
            if (!item.getMachinesUsed().isEmpty()) {
                for (Machine machine : machines) {
                    if (item.getMachinesUsed().contains(machine)) {
                        boolean flag = false;
                        for (int i = 0; i < item.getMachinesUsed().size(); i++) {
                            if (item.getMachinesUsed().get(i).equals(machine)) {
                                flag = true;
                            }
                            if (flag && item.getMachinesUsed().get(i) != machine) {
                                if (machine.getMachineDependency().get(item.getMachinesUsed().get(i)) != null) {
                                    machine.setMachineDependency(item.getMachinesUsed().get(i),
                                            machine.getMachineDependency().get(item.getMachinesUsed().get(i)) + 1);
                                } else {
                                    machine.getMachineDependency().put(item.getMachinesUsed().get(i), 1);
                                }
                                flag = false;
                            }
                        }
                    }
                }
            }
        }


        Map<Machine, Integer> map = new LinkedHashMap<>();
        for (Machine machine : machines) {
            if (!machine.getMachineDependency().isEmpty()) {
                map.put(machine, sumDependencies(machine));
            }
        }


        List<Map.Entry<Machine, Integer>> list = new ArrayList<>(map.entrySet());
        list.sort(reverseOrder(Map.Entry.comparingByValue()));

        return list;
    }

    /**
     * Sums the dependencies of a machine.
     * This method sums the dependencies of a machine.
     * Time complexity of this method is O(m) where m is the number of machines.
     *
     * @param machine The machine to sum the dependencies.
     *
     * @return The sum of the dependencies of the machine.
     */
    private int sumDependencies(Machine machine){
        int sum = 0;
        for (Machine m : machine.getMachineDependency().keySet()) {
            sum += machine.getMachineDependency().get(m);
        }
        return sum;
    }

    /**
     * Gets the machine dependency.
     * @return The machine dependency.
     */
    public List<Map.Entry<Machine, Integer>> getMachineDependency() {
        return machineDependency;
    }

    /**
     * Prints the machines used for each item.
     * The time complexity of this method is O(i*m) where i is the number of items and m is the number of machines.
     */
    /*public void printMachinesUsedForEachItem() {
        for (Item item : items) {
            if (!item.getMachinesUsed().isEmpty()) {
                StringBuilder sb = new StringBuilder();
                for (Machine machine : item.getMachinesUsed()) {
                    sb.append(machine.getId()).append(", ");
                }
                sb = new StringBuilder(sb.substring(0, sb.length() - 2));
                ConsoleWriter.displayLog("Item " + item.getId() + " used machines: " + sb);
            }
        }
    }*/

    /**
     * Converts an array of machine data into a list of Machine objects.
     * The time complexity of this method is O(m) where m is the number of machines.
     *
     * @param machinesCSV An array where each row represents a machine. The first element is the machine ID,
     *                    the second element is the operation name, and the third element is the operation time.
     *
     * @return A list of Machine objects created from the provided CSV data.
     **/

    public List<Machine> getMachineList(String[][] machinesCSV) {
        List<Machine> machines = new ArrayList<>();
        for (String[] machine : machinesCSV) {
            try {
                Machine m = new Machine(
                        machine[0],
                        machine[1],
                        Integer.parseInt(machine[2])
                );
                machines.add(m);
            }catch (Exception e) {
                ConsoleWriter.displayError("There was an error processing the machine data. Please check the CSV file.");
                return null;
            }
        }
        return machines;
    }

    /**
     * Converts an array of item data into a list of Item objects.
     * The time complexity of this method is O(l * i) where i is the number of items and l is the number of lines in the CSV file.
     *
     * @param itemsCSV An array where each row represents an item. The first element is the item ID,
     *                 the second element is the priority (HIGH, NORMAL, LOW), and the remaining elements
     *                 are the operations associated with the item.
     *
     * @return A list of Item objects created from the provided CSV data.
     */
    public List<Item> getItemsList(String[][] itemsCSV) {
        List<Item> items = new ArrayList<>();
        for (String[] item : itemsCSV) {
            try{
                ItemPriority priority = item[1].equals("HIGH") ? ItemPriority.HIGH : item[1].equals("NORMAL") ? ItemPriority.NORMAL : ItemPriority.LOW;
                LinkedList<String> operations = new LinkedList<>();
                for (int i = 2; i < item.length && item[i] != null; i++) {
                    operations.add(item[i]);
                }
                items.add(new Item(
                        Integer.parseInt(item[0]),
                        priority,
                        operations
                ));
            }catch (Exception e) {
                ConsoleWriter.displayError("There was an error processing the item data. Please check the CSV file.");
                return null;
            }
        }

        filterItems(items);

        return items;
    }

    /**
     * Filters the items based on the operations of the machines.
     * This method iterates over all items and machines and removes the items that do not contain the operation of the machine.
     * The time complexity of this method is O(i * op * m) where i is the number of items and m is the number of machines.
     *
     * @param items The list of items to filter.
     */
    private void filterItems(List<Item> items) {
        List<Item> filteredItems = new ArrayList<>();
        boolean flag = false;
        for (Item item : items) {
            int count = 0;
            for (String operation : item.getOperations()) {
                for (Machine machine : machines) {
                    if (operation.equals(machine.getOperationName())) {
                        flag = true;
                    }
                }
                if (flag) {
                    count++;
                    flag = false;
                }
            }
            if (count == item.getOperations().size()) {
                filteredItems.add(item);
            }
        }

        items.clear();
        items.addAll(filteredItems);
    }

    /**
     * Finds the latest end time among all machines.
     * The time complexity of this method is O(m * t) where m is the number of machines and t is the number of time slots.
     *
     * @return The maximum end time of all machines.
     */
    public int getLastMachineEndTime() {
        int max = 0;
        for (Machine machine : machines) {
            for (Time start : machine.getTimeActive().keySet()) {
                if (machine.getTimeActive().get(start).toSeconds() > max) {
                    max = machine.getTimeActive().get(start).toSeconds();
                }
            }
        }
        return max;
    }


    /**
     * Simulation time getter.
     * @return The simulation time in Time.
     */
    public Time getSimulationTime() {
        return new Time(simulationTime);
    }

    /**
     * Simulation time getter.
     * @return The simulation time in Integer.
     */
    public int getSimulationTimeInt() {
        return simulationTime;
    }

    /**
     * Gets the number of finished products.
     * @return The number of finished products.
     */
    public int getFinishedProducts() {
        return finishedProducts;
    }

    /**
     * Gets the operation times.
     * @return The operation times.
     */
    public Map<String, Time> getOperationTimes(){
        return operationTimes;
    }

    /**
     * Prints the operating percentage of each machine in the production process.
     * This method calculates the total production time by summing the production times
     * of all items and then determines the operating percentage of each machine based
     * on its active time compared to the total production time.

     * It sorts the machines by their active time and prints each machine's ID,
     * its active time in seconds and its operating percentage.
     * The operating percentage is calculated by dividing the machine's active time
     * by the total operation time and multiplying by 100.
     * The time complexity of this method is O(m + i) where m is the number of machines and i is the number of items.
     *
     * @param  machines A list of Machine objects representing the machines in the production process.
     * @param items A list of Item objects representing the items in the production process.
     *
     * @return A map of Machine objects to Floats representing the operating percentage of each machine.
     **/
    public Map<Machine, Float> calcMachineOperatingPerc(List<Machine> machines, List<Item> items) {
        int allProductions = 0;
        for (Item item : items) {
            allProductions += item.getProdTime();
        }

        machines.sort(Comparator.comparing(Machine::getTimeActiveInInt));

        Map<Machine, Float> machineOperatingPerc = new LinkedHashMap<>();

        for (Machine machine : machines) {
            if(machine.getTimeActiveInInt() > 0){
                float timeInPerc = (float) (machine.getTimeActiveInInt() * 100) / allProductions;
                machineOperatingPerc.put(machine, timeInPerc);
            }
        }

        return machineOperatingPerc;
    }

    /**
     * Gets the machine operating percentage.
     * @return The machine operating percentage.
     */
    public Map<Machine, Float> getMachineOperatingPerc() {
        return machineOperatingPerc;
    }

}