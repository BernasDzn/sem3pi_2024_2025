package org.g102.domain;

public class WorkstationTypeOperation {

    private WorkstationType workstationType;
    private Operation operation;

    public WorkstationTypeOperation(WorkstationType workstationType, Operation operation) {
        this.workstationType = workstationType;
        this.operation = operation;
    }

    public WorkstationType getWorkstationType() {return workstationType;}
    public Operation getOperation() {return operation;}

    public void setWorkstationType(WorkstationType workstationType) {this.workstationType = workstationType;}
    public void setOperation(Operation operation) {this.operation = operation;}

    @Override
    public boolean equals(Object obj) {
        boolean arg1 = getWorkstationType().getName().equals(((WorkstationTypeOperation)obj).getWorkstationType().getName());
        boolean arg2 = getOperation().getName().equals(((WorkstationTypeOperation)obj).getOperation().getName());
        return arg1 && arg2;
    }

    public String getSQLInsert() {
        return "INSERT INTO WorkstationTypeOperation(operation_id, workstation_type_id) VALUES (" + operation.getOperation_id() + ", '" + workstationType.getWorkstationType_id() + "');\n";
    }


}
