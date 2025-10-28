package org.g102.domain;

import org.apache.commons.math3.analysis.function.Add;

public class Address {
    private int address_id;
    private String addressName, zip_code, town_name, country_name;

    private static int address_id_counter = 0;

    public Address(String addressName, String zip_code, String town_name, String country_name) {
        this.address_id = address_id_counter;
        address_id_counter++;
        this.addressName = addressName;
        this.zip_code = zip_code;
        this.town_name = town_name;
        this.country_name = country_name;
    }

    public Address(int address_id, String addressName) {
        this.address_id = address_id;
        this.addressName = addressName;
    }

    public int getAddress_id() {return address_id;}
    public String getAddressName() {return addressName;}
    public String getZip_code() {return zip_code;}
    public String getTown_name() {return town_name;}
    public String getCountry_name() {return country_name;}

    public void setAddress_id(int address_id) {this.address_id = address_id;}
    public void setAddressName(String addressName) {this.addressName = addressName;}
    public void setZip_code(String zip_code) {this.zip_code = zip_code;}
    public void setTown_name(String town_name) {this.town_name = town_name;}
    public void setCountry_name(String country_name) {this.country_name = country_name;}

    public String getSQLInsert(){
        return "INSERT INTO Address (address_id, address_name, zip_code, town_name, country_name) VALUES ("+
                address_id+","+
                "'"+addressName+"',"+
                "'"+zip_code+"',"+
                "'"+town_name+"',"+
                "'"+ country_name +"');\n";
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Address)){
            return false;
        }
        return this.getAddressName().equals(((Address) obj).getAddressName()) &&
                this.getZip_code().equals(((Address) obj).getZip_code()) &&
                this.getTown_name().equals(((Address) obj).getTown_name()) &&
                this.getCountry_name().equals(((Address) obj).getCountry_name());
    }

    @Override
    public String toString() {
        return getAddressName();
    }
}
