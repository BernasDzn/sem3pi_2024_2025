package org.g102.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ContactTest {

    Contact contact = new Contact(123456789, 987654321, "primary@example.com", "secondary@example.com");

    @Test
    void mobileNumberShouldBeSetCorrectly() {
        assertEquals(123456789, contact.getMobileNumber());
    }

    @Test
    void fixedNumberShouldBeSetCorrectly() {
        assertEquals(987654321, contact.getFixedNumber());
    }

    @Test
    void primaryEmailShouldBeSetCorrectly() {
        assertEquals("primary@example.com", contact.getPrimaryEmail());
    }

    @Test
    void secondaryEmailShouldBeSetCorrectly() {
        assertEquals("secondary@example.com", contact.getSecondaryEmail());
    }

    @Test
    void setMobileNumber() {
        contact.setMobileNumber("987654321");
        assertEquals(987654321, contact.getMobileNumber());
    }

    @Test
    void setFixedNumber() {
        contact.setFixedNumber(987654321);
        assertEquals(987654321, contact.getFixedNumber());
    }

    @Test
    void setPrimaryEmail() {
        contact.setPrimaryEmail("new_primary@example.com");
        assertEquals("new_primary@example.com", contact.getPrimaryEmail());
    }

    @Test
    void setSecondaryEmail() {
        contact.setSecondaryEmail("new_secondary@example.com");
        assertEquals("new_secondary@example.com", contact.getSecondaryEmail());
    }

    @Test
    void setMobileNumberRejectInvalidNumberLength() {
        contact.setMobileNumber("12345");
        assertEquals(123456789, contact.getMobileNumber());
    }

    @Test
    void setMobileNumberRejectInvalidOperator() {
        contact.setMobileNumber("123456789");
        assertEquals(123456789, contact.getMobileNumber());
    }
}