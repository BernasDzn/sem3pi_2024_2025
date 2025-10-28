package org.g102.domain;

import org.g102.domain.Operation;
import org.g102.domain.Time;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class OperationTest {

    @Test
    void operationNameShouldBeSetCorrectly() {
        Operation operation = new Operation("Cutting", 10);
        assertEquals("Cutting", operation.getName());
    }

    @Test
    void operationTimeShouldBeSetCorrectly() {
        Operation operation = new Operation("Cutting", 10);
        assertEquals(10, operation.getTime());
    }

    @Test
    void setNameShouldUpdateOperationName() {
        Operation operation = new Operation("Cutting", 10);
        operation.setName("Welding");
        assertEquals("Welding", operation.getName());
    }

    @Test
    void setTimeShouldUpdateOperationTime() {
        Operation operation = new Operation("Cutting", 10);
        operation.setTime(15);
        assertEquals(15, operation.getTime().toSeconds());
    }

    @Test
    void operationNameShouldHandleEmptyString() {
        Operation operation = new Operation("", 10);
        assertEquals("", operation.getName());
    }

    @Test
    void operationTimeShouldHandleZero() {
        Operation operation = new Operation("Cutting", 0);
        assertEquals(0, operation.getTime());
    }

    @Test
    void operationTimeShouldHandleNegativeValue() {
        Operation operation = new Operation("Cutting", -5);
        assertEquals(-5, operation.getTime());
    }
}