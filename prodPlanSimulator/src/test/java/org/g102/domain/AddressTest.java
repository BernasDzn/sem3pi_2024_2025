package org.g102.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AddressTest {

    Address address = new Address("addressName", "4000-111", "townName",  "city");

    @Test
    void getAddressName() {
        assertEquals("addressName", address.getAddressName());
    }

    @Test
    void getZip_code() {
        assertEquals("4000-111", address.getZip_code());
    }

    @Test
    void getCity() {
        assertEquals("city", address.getCountry_name());
    }

    @Test
    void getTown_name() {
        assertEquals("townName", address.getTown_name());
    }

    @Test
    void getAddress_id() {
        assertEquals(9, address.getAddress_id());
    }

    @Test
    void setAddressName() {
        address.setAddressName("newAddressName");
        assertEquals("newAddressName", address.getAddressName());
    }

    @Test
    void setZip_code() {
        address.setZip_code("4000-222");
        assertEquals("4000-222", address.getZip_code());
    }

    @Test
    void setCity_name() {
        address.setCountry_name("newCity");
        assertEquals("newCity", address.getCountry_name());
    }

    @Test
    void setTown_name() {
        address.setTown_name("newTown");
        assertEquals("newTown", address.getTown_name());
    }

    @Test
    void setAddress_id() {
        address.setAddress_id(1);
        assertEquals(1, address.getAddress_id());
    }

    @Test
    void getSQLInsert() {
        assertEquals("INSERT INTO Address (address_id, address_name, zip_code, town_name, city_name) VALUES (2,'addressName','4000-111','townName','city');\n", address.getSQLInsert());
    }

    @Test
    void equals() {
        Address address2 = new Address("addressName", "4000-111", "townName",  "city");
        assertTrue(address.equals(address2));
    }
}