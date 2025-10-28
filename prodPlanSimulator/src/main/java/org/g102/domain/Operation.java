package org.g102.domain;

import org.g102.tree.ProductionNode;

import java.util.List;

public class Operation implements ProductionNode, Comparable<Operation> {
    private int operation_id;
    private String product_family_name;
    private String name;
    private Time time;
    private GenericItem outputProduct;
    private float outputQuantity;
    private List<WorkstationType> workstationTypes = null;

    /**
     * Constructor for Operation
     * @param name
     * @param time
     */
    public Operation(String name, int time) {
        this.name = name;
        this.time = new Time(time);
    }

    public Operation(int id, String name, int time) {
        this.operation_id = id;
        this.name = name;
        this.time = new Time(time);
    }

    /**
     * Constructor for Operation
     * @param op_id
     * @param name
     */
    public Operation(int op_id, String name) {
        this.operation_id = op_id;
        this.name = name;
        this.time = new Time(0);
    }

    public Operation(int op_id, String name, GenericItem outputProduct, int outputQuantity) {
        this.operation_id = op_id;
        this.name = name;
        this.outputProduct = outputProduct;
        this.outputQuantity = outputQuantity;
        this.time = new Time(0);
    }

    public Operation(int op_id, String name, List<WorkstationType> workstationTypes) {
        this.operation_id = op_id;
        this.name = name;
        this.workstationTypes = workstationTypes;
        this.time = new Time(0);
    }

    public Operation(int op_id, String name, List<WorkstationType> workstationTypes, GenericItem outputProduct, int outputQuantity) {
        this.operation_id = op_id;
        this.name = name;
        this.outputProduct = outputProduct;
        this.outputQuantity = outputQuantity;
        this.workstationTypes = workstationTypes;
        this.time = new Time(0);
    }

    /**
     * Operation id getter
     * @return operation_id
     */
    public int getOperation_id() {return operation_id;}

    /**
     * Name getter
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Time getter
     * @return time
     */
    public Time getTime() {
        return time;
    }

    public float getOutputQuantity() {
        return outputQuantity;
    }

    /**
     * Operation id setter
     * @param operation_id
     */
    public void setOperation_id(int operation_id) {this.operation_id = operation_id;}

    /**
     * Name setter
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    public void setOutputQuantity(float outputQuantity) {
        this.outputQuantity = outputQuantity;
    }

    public GenericItem getOutputProduct() {
        return outputProduct;
    }

    public void setOutputProduct(GenericItem outputProduct) {
        this.outputProduct = outputProduct;
    }

    /**
     * Time setter
     * @param time
     */
    public void setTime(Time time) {
        this.time = time;
    }

    /**
     * Time setter
     * @param time
     */
    public void setTime(int time) {
        this.time = new Time(time);
    }

    public List<WorkstationType> getWorkstationTypes() {
        return workstationTypes;
    }

    public void setWorkstationTypes(List<WorkstationType> workstationTypes) {
        this.workstationTypes = workstationTypes;
    }

    /**
     * SQL insert statement for operations
     * @return SQL insert statement
     */
    public String getSQLInsert(){
        return "INSERT INTO Operation (operation_id, description) VALUES (" + operation_id + ", '" + name + "');\n";
    }

    /**
     * Check if two operations are equal
     * @param obj
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Operation)
            return this.operation_id == ((Operation) obj).operation_id;
        return false;
    }

    @Override
    public String toString() {
        String output = name + " (op_id: " + operation_id + ")";
        if(outputProduct != null){
            output += " -> x" + outputQuantity + " " + outputProduct.getName();
        }
        return output;
    }

    @Override
    public int compareTo(Operation o) {
        return Integer.compare(this.operation_id, o.operation_id);
    }
}
