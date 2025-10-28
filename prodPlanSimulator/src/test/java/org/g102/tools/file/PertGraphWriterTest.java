package org.g102.tools.file;

import org.g102.domain.Activity;
import org.g102.graph.pert.PertGraph;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class PertGraphWriterTest {

    static PertGraph instance = new PertGraph();
    static Activity actA = new Activity("A", "A", 20, "days");
    static Activity actB = new Activity("B", "B", 50, "days");
    static Activity actC = new Activity("C", "C", 25, "days");
    static Activity actD = new Activity("D", "D", 15, "days");
    static Activity actE = new Activity("E", "E", 60, "days");

    @BeforeAll
    public static void setUpClass() {
        instance.addActivity(actA);
        instance.addActivity(actB);
        instance.addActivity(actC);
        instance.addActivity(actD);
        instance.addActivity(actE);
        instance.addDependency(actA, actC, 1);
        instance.addDependency(actA, actE, 1);
        instance.addDependency(actC, actD, 1);
    }

    @Test
    void writeTo() throws FileNotFoundException {
        PertGraphWriter writer = new PertGraphWriter(instance);
        writer.writeTo("src/main/resources/testOutput/schedule", false);
        Scanner sc = new Scanner(new File("src/main/resources/testOutput/schedule.csv"));
        ArrayList<String> lines = new ArrayList<>();
        while (sc.hasNextLine()) {
            lines.add(sc.nextLine());
        }
        sc.close();
        assertEquals("A,0,20,0,0,20,20", lines.get(0));
        assertEquals("B,0,50,0,30,50,80", lines.get(1));
        assertEquals("C,0,25,20,40,45,65,A", lines.get(2));
        assertEquals("D,0,15,45,65,60,80,C", lines.get(3));
        assertEquals("E,0,60,20,20,80,80,A", lines.get(4));
    }

    @Test
    void emptyWriteToHeader() throws FileNotFoundException {
        PertGraph instance2 = new PertGraph();
        PertGraphWriter writer = new PertGraphWriter(instance2);
        writer.writeTo("src/main/resources/testOutput/scheduleEmptyHeader", true);
        Scanner sc = new Scanner(new File("src/main/resources/testOutput/scheduleEmptyHeader.csv"));
        ArrayList<String> lines = new ArrayList<>();
        while (sc.hasNextLine()) {
            lines.add(sc.nextLine());
        }
        sc.close();
        assertEquals(writer.getHeader(), lines.get(0));
        assertEquals(1, lines.size());
    }

    @Test
    void emptyWriteTo() throws FileNotFoundException {
        PertGraph instance2 = new PertGraph();
        PertGraphWriter writer = new PertGraphWriter(instance2);
        writer.writeTo("src/main/resources/testOutput/scheduleEmpty", false);
        Scanner sc = new Scanner(new File("src/main/resources/testOutput/scheduleEmpty.csv"));
        ArrayList<String> lines = new ArrayList<>();
        while (sc.hasNextLine()) {
            lines.add(sc.nextLine());
        }
        sc.close();
        assertEquals(0, lines.size());
    }
}