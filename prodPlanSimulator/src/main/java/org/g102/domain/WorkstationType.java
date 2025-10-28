package org.g102.domain;

public class WorkstationType {

    private String workstationType_id;
    private String name;

    public WorkstationType(String workstationType_id, String name) {
        this.workstationType_id = workstationType_id;
        this.name = name;
    }

    public String getWorkstationType_id() {return workstationType_id;}
    public String getName() {return name;}

    public void setWorkstationType_id(String workstationType_id) {this.workstationType_id = workstationType_id;}
    public void setName(String name) {this.name = name;}

    public String getSQLInsert(){
        return "INSERT INTO WorkstationType (type_id, type_name) VALUES ('" + workstationType_id + "', '" + name + "')";
    }

}
