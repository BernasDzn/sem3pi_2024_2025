package org.g102.domain;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Class that represents an item in the production plan
 */
public class Item {
    private int id;
    private ItemPriority priority;
    private LinkedList<String> operations;
    private int nextOperation;
    private double[] size;
    private String color;
    private List<Machine> machinesUsed;
    private int prodTime;

    /**
     * Constructor for Item
     * @param id Item id
     * @param priority Item priority
     * @param operations List of operations
     * @param size Item size
     * @param color Item color
     * @param prodTime Item production time
     */
    public Item(int id, ItemPriority priority, LinkedList<String> operations, double[] size, String color, int prodTime){
        if(size.length != 3){
            throw new IllegalArgumentException("Size must have 3 dimensions");
        }
        this.id = id;
        this.priority = priority;
        this.operations = operations;
        this.size = size;
        this.color = color;
        this.nextOperation = 0;
        this.prodTime=prodTime;
    }

    /**
     * Constructor for Item
     * @param id Item id
     * @param priority Item priority
     * @param operations List of operations
     */
    public Item(int id, ItemPriority priority, LinkedList<String> operations){
        this.id = id;
        this.priority = priority;
        this.operations = operations;
        this.nextOperation = 0;
        machinesUsed = new ArrayList<>();
    }

    /**
     * get item id
     * @return item id
     */
    public int getId(){return id;}

    /**
     * get item priority
     * @return item priority
     */
    public ItemPriority getPriority(){return priority;}

    /**
     * get list of operations
     * @return list of operations
     */
    public LinkedList<String> getOperations(){return operations;}

    /**
     * get item size
     * @return item size
     */
    public double[] getSize(){return size;}

    /**
     * get item color
     * @return item color
     */
    public String getColor(){return color;}

    /**
     * get next operation
     * @return next operation
     */
    public int getNextOperation(){return nextOperation;}

    /**
     * get production time
     * @return production time
     */
    public int getProdTime() {return prodTime;}

    /**
     * get machines used
     * @return machines used
     */
    public List<Machine> getMachinesUsed(){return machinesUsed;}

    /**
     * set item id
     * @param id item id
     */
    public void setId(int id){this.id = id;}

    /**
     * set item priority
     * @param priority item priority
     */
    public void setPriority(ItemPriority priority){this.priority = priority;}

    /**
     * set list of operations
     * @param operations list of operations
     */
    public void setOperations(LinkedList<String> operations){this.operations = operations;}

    /**
     * set item size
     * @param size item size
     */
    public void setSize(double[] size){
        if(size.length != 3){
            throw new IllegalArgumentException("Size must have 3 dimensions.");
        }
        this.size = size;
    }

    /**
     * set item color
     * @param color item color
     */
    public void setColor(String color){this.color = color;}

    /**
     * set next operation
     * @param nextOperation next operation
     */
    public void setNextOperation(int nextOperation){this.nextOperation = nextOperation;}

    /**
     * set production time
     * @param prodTime production time
     */
    public void setProductionTime(int prodTime) {this.prodTime = prodTime;}

    /**
     * set machines used
     * @param machinesUsed machines used
     */
    public void setMachinesUsed(List<Machine> machinesUsed){this.machinesUsed = machinesUsed;}

    /**
     * add machine used
     * @param machine machine used
     */
    public void addMachineUsed(Machine machine){machinesUsed.add(machine);}

    /**
     * return the item as a string
     * @return item as a string in a JSON-like format
     */
    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", priority=" + priority +
                ", operations=" + operations +
                ", size=" + size +
                ", color='" + color + '\'' +
                '}';
    }

}
