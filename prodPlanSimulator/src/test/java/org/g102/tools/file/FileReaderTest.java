package org.g102.tools.file;

import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

class FileReaderTest {

    @Test
    void readCSVShouldReturnCorrectValuesItem() throws FileNotFoundException {
        String[][] result = FileReader.readCSV("src/main/resources/articles.csv", true, ";");
        assertEquals("1", result[0][0]);
        assertEquals("NORMAL", result[0][1]);
    }

    @Test
    void readCSVShouldReturnCorrectValuesMachines() throws FileNotFoundException {
        String[][] result = FileReader.readCSV("src/main/resources/workstations.csv", true, ";");
        assertEquals("ws11", result[0][0]);
        assertEquals("CUT", result[0][1]);
    }

    @Test
    void readCSVShouldReturnCorrectValuesMachinesWithoutHeader() throws FileNotFoundException{
        String[][] result = FileReader.readCSV("src/main/resources/machines.csv", true, ";");
        assertEquals("2, Additive Manufacturing, 16.8", result[0][0]);
    }

    @Test
    void readCSVShouldReturnCorrectValuesItemsWithoutHeader() throws FileNotFoundException{
        String[][] result = FileReader.readCSV("src/main/resources/artigos.csv", true, ";");
        assertEquals("2, MEDIUM, Engraving, Etching", result[0][0]);
    }

    @Test
    void readCSVShouldHandleEmptyFile() throws FileNotFoundException {
        String[][] result = FileReader.readCSV("empty_file.csv", false, ";");
        assertEquals(0, result.length);
    }

    @Test
    void readCSVShouldHandleFileNotFound() {
        String[][] result = FileReader.readCSV("non_existent_file.csv", false, ";");
        assertEquals(0, result.length);
    }
}