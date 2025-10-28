package org.g102.domain;

import org.apache.commons.math3.analysis.function.Add;

public class Customer {
    private int client_id;
    private String name;
    private String vatin;
    private Address address;
    private String email_address;
    private String phone_number;
    private CustomerType type;

    public Customer(int client_id, String name, String vatin, Address address, CustomerType type, String email_address, String phone_number) {
        this.client_id = client_id;
        this.name = name;
        this.vatin = vatin;
        this.address = address;
        this.type = type;
        this.email_address = email_address;
        this.phone_number = phone_number;
    }

    public Customer(int client_id, String name) {
        this.client_id = client_id;
        this.name = name;
    }

    public int getClient_id() {return client_id;}
    public String getName() {return name;}
    public String getVatin() {return vatin;}
    public Address getAddress() {return address;}
    public CustomerType getType() {return type;}
    public String getEmail_address() {return email_address;}
    public String getPhone_number() {return phone_number;}

    public void setClient_id(int client_id) {this.client_id = client_id;}
    public void setName(String name) {this.name = name;}
    public void setVatin(String vatin) {this.vatin = vatin;}
    public void setAddress(Address address) {this.address = address;}
    public void setType(CustomerType type) {this.type = type;}
    public void setEmail_address(String email_address) {this.email_address = email_address;}
    public void setPhone_number(String phone_number) {this.phone_number = phone_number;}

    public String getSQLInsert(){
        return "INSERT INTO Customer (customer_id, name, vatin, address_id, type_id, email_address, phone_number) VALUES ("+
                client_id + ",'" +
                name + "','" +
                vatin + "',"+
                address.getAddress_id() + ",'" +
                type.name() + "','" +
                email_address + "','" +
                phone_number + "');\n";
    }

    @Override
    public String toString() {
        return getName();
    }
}
