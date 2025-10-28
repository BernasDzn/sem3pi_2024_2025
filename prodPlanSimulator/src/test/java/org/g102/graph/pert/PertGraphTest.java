package org.g102.graph.pert;

import org.g102.domain.Activity;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class PertGraphTest {

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
    public void testEmptyCalculateTimes() {
        PertGraph emptyGraph = new PertGraph();
        assertFalse(emptyGraph.calculateTimes());
    }

    @Test
    public void testForwardPass() {
        instance.calculateTimes();
        assertEquals(0, actA.getEarliestStart());
        assertEquals(20, actA.getEarliestFinish());
        assertEquals(0, actB.getEarliestStart());
        assertEquals(50, actB.getEarliestFinish());
        assertEquals(20, actC.getEarliestStart());
        assertEquals(45, actC.getEarliestFinish());
        assertEquals(45, actD.getEarliestStart());
        assertEquals(60, actD.getEarliestFinish());
        assertEquals(20, actE.getEarliestStart());
        assertEquals(80, actE.getEarliestFinish());
    }

    @Test
    public void testBackwardPass() {
        instance.calculateTimes();
        assertEquals(0, actA.getLatestStart());
        assertEquals(20, actA.getLatestFinish());
        assertEquals(30, actB.getLatestStart());
        assertEquals(80, actB.getLatestFinish());
        assertEquals(40, actC.getLatestStart());
        assertEquals(65, actC.getLatestFinish());
        assertEquals(65, actD.getLatestStart());
        assertEquals(80, actD.getLatestFinish());
        assertEquals(20, actE.getLatestStart());
        assertEquals(80, actE.getLatestFinish());
    }

    @Test
    public void testGetCriticalPathsEmptyGraph() {
        PertGraph emptyGraph = new PertGraph();
        assertNull(emptyGraph.getCriticalPaths());
    }

    @Test
    public void testGetCriticalPaths() {
        ArrayList<LinkedList<Activity>> criticalPaths = instance.getCriticalPaths();
        assertEquals(2, criticalPaths.get(0).size());
        assertEquals("A", criticalPaths.get(0).get(0).getId());
        assertEquals("E", criticalPaths.get(0).get(1).getId());
    }

    @Test
    public void testGetBottleneckActivitiesEmptyGraph() {
        PertGraph emptyGraph = new PertGraph();
        assertNull(emptyGraph.getBottleneckActivities());
    }

    @Test
    public void testGetBottleneckActivities() {
        LinkedList<Activity> bottleneckActivities = instance.getBottleneckActivities();
        System.out.println(instance);
        assertEquals(1, bottleneckActivities.size());
        assertEquals("A", bottleneckActivities.get(0).getId());
    }

    @Test
    void delayActivity() {
        instance.delayActivity(actA, 5);
        assertEquals(25, actA.getDuration());
    }

    @Test
    void delayActivityNegativeValue() {
        instance.delayActivity(actA, -10);
        assertEquals(20, actA.getDuration());
    }

    @Test
    public void testTopologicallySortedGraph() {
     LinkedList<Activity> topSortedGraph = instance.getTopologicalSort();

     assertEquals(5, topSortedGraph.size(),"topologically sorted graph contains 5 activities");
    }

    @Test
    public void testTopologicallySortedEmptyGraph() {
        PertGraph emptyGraph = new PertGraph();
        LinkedList<Activity> topSortedGraph = emptyGraph.getTopologicalSort();
        assertTrue(topSortedGraph.isEmpty(), "topological sort of an empty graph is empty");
    }
}