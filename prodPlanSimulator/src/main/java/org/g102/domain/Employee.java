package org.g102.domain;

public class Employee {
    private String name;
    private String contact;
    private Address address;

    public Employee(String name, String contact, Address address){
        this.name = name;
        this.contact = contact;
        this.address = address;
    }

    public String getName(){
        return name;
    }

    public String getContact(){
        return contact;
    }

    public Address getAddress(){
        return address;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setContact(String contact){
        this.contact = contact;
    }

    public void setAddress(Address address){
        this.address = address;
    }
}
