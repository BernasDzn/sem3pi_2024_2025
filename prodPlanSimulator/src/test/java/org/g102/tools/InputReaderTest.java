package org.g102.tools;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InputReaderTest {

    @Test
    void readShouldReturnUserInput() {
        // Simulate user input
        InputReader.inputReader = new java.util.Scanner("test input");
        assertEquals("test input", InputReader.read("Enter something"));
    }

    @Test
    void readBooleanShouldReturnTrueForY() {
        InputReader.inputReader = new java.util.Scanner("y");
        assertTrue(InputReader.readBoolean("Enter yes or no"));
    }

    @Test
    void readBooleanShouldReturnFalseForN() {
        InputReader.inputReader = new java.util.Scanner("n");
        assertFalse(InputReader.readBoolean("Enter yes or no"));
    }

    @Test
    void readBooleanShouldHandleInvalidInput() {
        InputReader.inputReader = new java.util.Scanner("invalid\ny");
        assertTrue(InputReader.readBoolean("Enter yes or no"));
    }

    @Test
    void showListReturnIndexShouldReturnCorrectIndex() {
        List<String> list = Arrays.asList("Option 1", "Option 2", "Option 3");
        InputReader.inputReader = new java.util.Scanner("2");
        assertEquals(2, InputReader.showListReturnIndex(list, "Choose an option"));
    }

    @Test
    void showListReturnIndexShouldHandleInvalidOption() {
        List<String> list = Arrays.asList("Option 1", "Option 2", "Option 3");
        InputReader.inputReader = new java.util.Scanner("invalid\n2");
        assertEquals(2, InputReader.showListReturnIndex(list, "Choose an option"));
    }

    @Test
    void showListReturnIndexShouldHandleOutOfRangeOption() {
        List<String> list = Arrays.asList("Option 1", "Option 2", "Option 3");
        InputReader.inputReader = new java.util.Scanner("4\n2");
        assertEquals(2, InputReader.showListReturnIndex(list, "Choose an option"));
    }
}