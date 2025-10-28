package org.g102.domain;

import java.util.ArrayList;
import java.util.List;

public class Workstation {
    private int number;
    private WorkstationType workstationType;
    private String name;
    private String description;
    private List<Machine> machines;

    public Workstation(int number, List<Machine> machines) {
        this.number = number;
        this.machines = machines;
    }

    public Workstation(String id, String name) {
        this.description = id;
        this.name = name;
    }

    public Workstation(int number, WorkstationType workstationType, String name, String description) {
        this.number = number;
        this.workstationType = workstationType;
        this.name = name;
        this.description = description;
        this.machines = new ArrayList<>();
    }

    public int getNumber() {
        return number;
    }

    public List<Machine> getMachines() {
        return machines;
    }

    public WorkstationType getWorkstationType() {
        return workstationType;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setMachines(List<Machine> machines) {
        this.machines = machines;
    }

    public void addMachine(Machine machine) {
        machines.add(machine);
    }

    public void removeMachine(Machine machine) {
        machines.remove(machine);
    }

    public List<Operation> getOperations() {
        List<Operation> operations = new ArrayList<>();
        for (Machine machine : machines) {
            operations.add(machine.getOperation());
        }
        return operations;
    }

    @Override
    public String toString() {
        return getName();
    }

    public String getSQLInsert() {
        return "INSERT INTO Workstation (workstation_id, workstation_type_id, name, description) VALUES (" + number + ", '" + workstationType.getWorkstationType_id() + "', '" + name + "', '" + description + "');\n";
    }
}
