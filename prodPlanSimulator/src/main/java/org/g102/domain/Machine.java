package org.g102.domain;

import java.util.*;

public class Machine {
    private String id;
    private String name;
    private Date purchaseDate;
    private Date lastMaintenanceDate;
    private MachineStatus status;
    private Operation operation;
    private Map<Time, Time> timeActive;
    private Map<Time, Item> itemProcessedTime;
    private Map<Machine, Integer> machineDependency;

    /**
     * Constructor for Machine
     * @param id
     * @param name
     * @param purchaseDate
     * @param lastMaintenanceDate
     * @param status
     * @param operationName
     * @param operationTime
     */
    public Machine(String id, String name, Date purchaseDate, Date lastMaintenanceDate, MachineStatus status, String operationName, int operationTime) {
        this.id = id;
        this.name = name;
        this.purchaseDate = purchaseDate;
        this.lastMaintenanceDate = lastMaintenanceDate;
        this.status = status;
        this.operation = new Operation(operationName, operationTime);
        this.itemProcessedTime = new LinkedHashMap<>();
    }

    /**
     * Constructor for Machine
     * @param id
     * @param operationName
     * @param operationTime
     */
    public Machine(String id, String operationName, int operationTime) {
        this.id = id;
        this.operation = new Operation(operationName, operationTime);
        timeActive = new HashMap<>();
        machineDependency = new LinkedHashMap<>();
        this.itemProcessedTime = new LinkedHashMap<>();
    }

    /**
     * Constructor for Machine
     * @param machine
     */
    public Machine(Machine machine){
        this.id = machine.getId();
        this.operation = machine.getOperation();
        this.timeActive = machine.getTimeActive();
        this.itemProcessedTime = machine.getItemProcessedTime();
        this.machineDependency = machine.getMachineDependency();
    }

    /**
     * Id getter
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * Name getter
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Purchase date getter
     * @return purchaseDate
     */
    public Date getPurchaseDate() {
        return purchaseDate;
    }

    /**
     * Last maintenance date getter
     * @return lastMaintenanceDate
     */
    public Date getLastMaintenanceDate() {
        return lastMaintenanceDate;
    }

    /**
     * Status getter
     * @return status
     */
    public MachineStatus getStatus() {
        return status;
    }

    /**
     * Operation name getter
     * @return operation.getName()
     */
    public String getOperationName() {
        return operation.getName();
    }

    /**
     * Operation time getter
     * @return operation.getTime()
     */
    public Time getOperationTime() {
        return operation.getTime();
    }

    /**
     * Operation getter
     * @return operation
     */
    public Operation getOperation() {
        return operation;
    }

    /**
     * Machine dependency getter
     * @return machineDependency
     */
    public Map<Machine, Integer> getMachineDependency() {
        return machineDependency;
    }

    /**
     * Id setter
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Name setter
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Purchase date setter
     * @param purchaseDate
     */
    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    /**
     * Last maintenance date setter
     * @param lastMaintenanceDate
     */
    public void setLastMaintenanceDate(Date lastMaintenanceDate) {
        this.lastMaintenanceDate = lastMaintenanceDate;
    }

    /**
     * Status setter
     * @param status
     */
    public void setStatus(MachineStatus status) {
        this.status = status;
    }

    /**
     * Operation name setter
     * @param operationName
     */
    public void setOperationName(String operationName) {
        operation.setName(operationName);
    }

    /**
     * Operation time setter
     * @param operationTime
     */
    public void setOperationTime(Time operationTime) {
        operation.setTime(operationTime);
    }

    /**
     * Operation time setter
     * @param operationTime
     */
    public void setOperationTime(int operationTime) {
        operation.setTime(operationTime);
    }

    /**
     * Operation setter
     * @param operation
     */
    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    /**
     * Time active setter
     * @param start
     * @param end
     */
    public void setTimeActive(Time start, Time end) {
        timeActive.put(start, end);
    }

    /**
     * Time active setter
     * @param timeActive
     */
    public void setTimeActive(Map<Time, Time> timeActive) {
        this.timeActive.clear();
        this.timeActive.putAll(timeActive);
    }

    /**
     * End time setter
     * @param end
     */
    public void setEndTime(Time end){
        for (Time start : timeActive.keySet()){
            if (timeActive.get(start).toSeconds() == 0){
                timeActive.put(start, end);
            }
        }
    }

    /**
     * Machine dependency setter
     * @param machine
     * @param count
     */
    public void setMachineDependency(Machine machine, int count){
        machineDependency.put(machine, count);
    }

    /**
     * Machine dependency setter
     * @param machineDependency
     */
    public void setMachineDependency(Map<Machine, Integer> machineDependency) {
        this.machineDependency.clear();
        this.machineDependency.putAll(machineDependency);
    }

    /**
     * End time getter
     * @return End time
     */
    public int getEndTime(){
        int max = 0;
        for(Time end : timeActive.values()){
            if(end.toSeconds() < max){
                max = end.toSeconds();
            }
        }
        return max;
    }

    /**
     * Time active getter
     * @return timeActive
     */
    public Map<Time, Time> getTimeActive() {
        return timeActive;
    }

    public Map<Integer, Integer> getTimeActiveInSeconds(){
        Map<Integer, Integer> timeActiveInSeconds = new LinkedHashMap<>();
        for (Map.Entry<Time, Time> entry : timeActive.entrySet()) {
            timeActiveInSeconds.put(entry.getKey().toSeconds(), entry.getValue().toSeconds());
        }
        return timeActiveInSeconds;
    }

    /**
     * Time active in integer getter
     * @return totalActiveTime
     */
    public int getTimeActiveInInt() {
        int totalActiveTime=0;
        for (Map.Entry<Time, Time> entry : timeActive.entrySet()) {
            int startTime = entry.getKey().toSeconds();
            int endTime = entry.getValue().toSeconds();
            totalActiveTime += (endTime - startTime);
        }
        return totalActiveTime;
    }

    /**
     * Active times lengths getter
     * @return times
     */
    public List<Integer> getActiveTimesLengths(){
        List<Integer> times = new ArrayList<>();
        for(Time t : timeActive.keySet()){
            times.add(timeActive.get(t).toSeconds()-t.toSeconds());
        }
        return times;
    }

    /**
     * Waiting time lengths getter
     * @return times
     */
    public List<Integer> getWaitingTimeLengths(){
        List<Integer> times = new ArrayList<>();
        Time[] values = timeActive.values().toArray(new Time[0]);
        Time[] keys = timeActive.keySet().toArray(new Time[0]);
        if(keys[0].toSeconds() != 0){
            times.add(keys[0].toSeconds());
        }
        for(int i = 0; i < keys.length - 1; i++){
            times.add(values[i].toSeconds() - keys[i + 1].toSeconds());
        }
        return times;
    }

    /**
     * Item processed time getter
     * @return itemProcessedTime
     */
    public Map<Time, Item> getItemProcessedTime() {
        return itemProcessedTime;
    }

    /**
     * Item end time getter
     * @param item
     * @return
     */
    public Time getItemEndTime(Item item){
        for (Map.Entry<Time, Item> entry : itemProcessedTime.entrySet()) {
            if (entry.getValue().equals(item)) {
                return entry.getKey();
            }
        }
        return  new Time(0);
    }

    /**
     * Item processed time getter
     * @param item
     * @return itemProcessedTime or 0
     */
    public Integer getItemProcessedTime(Item item) {
        for (Map.Entry<Time, Item> entry : itemProcessedTime.entrySet()) {
            if (entry.getValue().equals(item)) {
                return entry.getKey().toSeconds();
            }
        }
        return 0;
    }

    /**
     * Item processed time setter
     * @param itemProcessedTime
     */
    public void setItemProcessedTime(Map<Time, Item> itemProcessedTime) {
        this.itemProcessedTime = itemProcessedTime;
    }

    /**
     * Add item processed time
     * @param time
     * @param item
     */
    public void addItemProcessedTime(Time time, Item item){
        itemProcessedTime.put(time, item);
    }

    /**
     * Prints the machine information
     * @return String
     */
    @Override
    public String toString() {
        return "Machine{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", purchaseDate=" + purchaseDate +
                ", lastMaintenanceDate=" + lastMaintenanceDate +
                ", status=" + status +
                ", operation=" + operation +
                '}';
    }

}
