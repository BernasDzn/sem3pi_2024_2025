package org.g102.graph;

import org.g102.graph.map.MapGraph;
import org.g102.graph.pert.PertGraph;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class AlgorithmsTest {

    final ArrayList<String> co = new ArrayList<>(Arrays.asList( "A", "A", "B", "C", "C", "D", "E", "E"));
    final ArrayList <String> cd = new ArrayList<>(Arrays.asList("B", "C", "D", "D", "E", "A", "D", "E"));
    final ArrayList <Integer> cw = new ArrayList<>(Arrays.asList( 1,  2 ,  3 ,  4 ,  5 ,  6 ,  7 ,  8 ));
    final ArrayList<String> co2 = new ArrayList<>(Arrays.asList( "A", "A", "B", "C", "C", "D"));
    final ArrayList <String> cd2 = new ArrayList<>(Arrays.asList("B", "C", "D", "D", "E", "E"));
    final ArrayList <String> co3 = new ArrayList<>(Arrays.asList("A", "A", "B", "B", "C", "D", "E"));
    final ArrayList <String> cd3 = new ArrayList<>(Arrays.asList("B", "C", "D", "E", "F", "F", "G"));

    final ArrayList <String> ov = new ArrayList<>(Arrays.asList( "A",  "B",  "C" ,  "D" ,  "E" ));
    MapGraph<String, Integer> instance = null;
    MapGraph<String, Integer> instance2 = null;

    @BeforeEach
    public void initializeTestGraphs() {
        instance = new MapGraph<>(true);
        instance2 = new MapGraph<>(true);
    }

    @Test
    public void TestinputToPertGraph() {
        PertGraph g= new PertGraph();
        String[][] graphInput = {{"A1", "Activity 1", "10"," days", "0", "", "", "", ""},
                {"A2",  "Activity 2", "5"," days", "0", "A1", "", ""},
                {"A3", "Activity 3", "2"," days", "0", "A1", "A2", ""}
        };
        boolean success= Algorithms.inputToPertGraph(graphInput, g);
        assertTrue(success);
        assertEquals(3, g.getActivities().size());
    }

    @Test //wrong parameters
    public void TestInvalidInputToPertGraph() {
        PertGraph g = new PertGraph();
        String[][] graphInput = {
                {"A1", "Activity 1", "AA", "days", "0",""}
        };
        boolean success = Algorithms.inputToPertGraph(graphInput, g);
        assertFalse(success);
    }

    @Test //missing parameters
    public void TestInvalidInputToPertGraph2() {
        PertGraph g = new PertGraph();
        String[][] graphInput = {
                {"A1", "Activity 1", "10", "days", "0"}
        };
        boolean success = Algorithms.inputToPertGraph(graphInput, g);
        assertFalse(success);
    }

    @Test//wrong vertice input
    public void TestInvalidInputToPertGraph3() {
        PertGraph g = new PertGraph();
        String[][] graphInput = {
                {"A1", "Activity 1", "10", "days", "0", "InvalidKey"} // Invalid predecessor
        };
        boolean success = Algorithms.inputToPertGraph(graphInput, g);
        assertFalse(success);
    }

    @Test
    public void TestIsCyclicGraphTrue() {
        for (int i = 0; i < co.size(); i++)
            instance.addEdge(co.get(i), cd.get(i), cw.get(i));

        assertTrue(Algorithms.isCyclicGraph(instance), "Graph has a cycle");
    }

    @Test
    public void TestIsCyclicGraphFalse() {
        for (int i = 0; i < co2.size(); i++)
            instance.addEdge(co2.get(i), cd2.get(i), cw.get(i));

        assertFalse(Algorithms.isCyclicGraph(instance), "Graph doesn't have a cycle");
    }

    @Test
    public void TestDFSTopologicalSort(){
        for (int i = 0; i < co2.size(); i++)
            instance.addEdge(co2.get(i), cd2.get(i), cw.get(i));

        LinkedList<String> expectedRes = new LinkedList<>();
        expectedRes.addAll(List.of("A", "B", "D", "E", "C"));
        LinkedList<String> res = Algorithms.DepthFirstSearch(instance, instance.vertices().get(0));
        assertEquals(expectedRes, res);
    }

    @Test
    public void TestTopologicalSort(){
        MapGraph<String, Integer> g= new MapGraph<>(true);
        for (int i = 0; i < co2.size(); i++)
            g.addEdge(co2.get(i), cd2.get(i), cw.get(i));

        List<LinkedList<String>> validResults = new ArrayList<>();
        validResults.add(new LinkedList<>(List.of("A", "B", "C", "D", "E")));
        validResults.add(new LinkedList<>(List.of("A", "C", "B", "D", "E")));

        boolean isValid = false;
        LinkedList<String> res = Algorithms.topologicalSort(g);

        for(int i= 0;i<validResults.size();i++){
            if(res.equals(validResults.get(i))){
                isValid=true;
            }
        }
        assertTrue(isValid);
    }

    @Test
    public void TestTopologicalSort2(){
        MapGraph<String, Integer> g= new MapGraph<>(true);
        for (int i = 0; i < co3.size(); i++)
            g.addEdge(co3.get(i), cd3.get(i), cw.get(i));

        List<LinkedList<String>> validResults = new ArrayList<>();
        validResults.add(new LinkedList<>(List.of("A", "B", "D", "E", "G","C", "F")));
        validResults.add(new LinkedList<>(List.of("A", "B", "E", "G", "D","C", "F")));
        validResults.add(new LinkedList<>(List.of("A", "C", "B", "D", "F","E", "G")));
        validResults.add(new LinkedList<>(List.of("A", "C", "B", "E", "G","D", "F")));
        validResults.add(new LinkedList<>(List.of("A", "B", "C", "D", "F","E", "G")));
        validResults.add(new LinkedList<>(List.of("A", "B", "C", "E", "G","D", "F")));

        boolean isValid = false;
        LinkedList<String> res = Algorithms.topologicalSort(g);

        for(int i= 0;i<validResults.size();i++){
            if(res.equals(validResults.get(i))){
                isValid=true;
            }
        }
        assertTrue(isValid);
    }

    @Test
    public void TestTopologicalSortSingleVertex() {
        MapGraph<String, Integer> g = new MapGraph<>(true);
        g.addVertex("A");

        LinkedList<String> res = Algorithms.topologicalSort(g);

        LinkedList<String> expectedRes = new LinkedList<>(List.of("A"));
        assertEquals(expectedRes, res);
    }

    @Test
    public void TestTopologicalSortLinearGraph() {
        MapGraph<String, Integer> g = new MapGraph<>(true);
        g.addEdge("A", "B", 1);
        g.addEdge("B", "C", 1);
        g.addEdge("C", "D", 1);

        LinkedList<String> res = Algorithms.topologicalSort(g);

        LinkedList<String> expectedRes = new LinkedList<>(List.of("A", "B", "C", "D"));
        assertEquals(expectedRes, res);
    }

    @Test
    public void TestKahnTopologicalSort(){
        MapGraph<String, Integer> g= new MapGraph<>(true);
        for (int i = 0; i < co2.size(); i++)
            g.addEdge(co2.get(i), cd2.get(i), cw.get(i));

        List<LinkedList<String>> validResults = new ArrayList<>();
        validResults.add(new LinkedList<>(List.of("A", "B", "C", "D", "E")));
        validResults.add(new LinkedList<>(List.of("A", "C", "B", "D", "E")));

        boolean isValid = false;
        LinkedList<String> res= Algorithms.KahnsTopologicalSort(g);

        for(int i= 0;i<validResults.size();i++){
            if(res.equals(validResults.get(i))){
                isValid=true;
            }
        }
        assertTrue(isValid);
    }

    @Test
    public void TestKahnTopologicalSortEmptyGraph(){
        MapGraph<String, Integer> g= new MapGraph<>(true);
        LinkedList<String> res= Algorithms.KahnsTopologicalSort(g);

        LinkedList<String> expectedRes = new LinkedList<>();
        assertEquals(expectedRes, res);
    }

    //with cycle
    @Test
    public void TestKahnTopologicalSortCyclicalGraph(){
        MapGraph<String, Integer> g= new MapGraph<>(true);
        for (int i = 0; i < co2.size(); i++)
            g.addEdge(co2.get(i), cd2.get(i), cw.get(i));

        g.addEdge(cd2.get(4), co2.get(4), cw.get(1));

        IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Algorithms.KahnsTopologicalSort(g);
        });

        Assertions.assertEquals("The graph has a cycle.", thrown.getMessage());
    }


    @Test
    public void TestAllPaths(){
        for (int i = 0; i < co.size(); i++)
            instance.addEdge(co.get(i), cd.get(i), cw.get(i));

        ArrayList<LinkedList<String>> expectedRes = new ArrayList<>();
        expectedRes.add(new LinkedList<>(List.of("A", "B", "D")));
        expectedRes.add(new LinkedList<>(List.of("A", "C", "D")));
        expectedRes.add(new LinkedList<>(List.of("A", "C", "E", "D")));

        ArrayList<LinkedList<String>> res = Algorithms.allPaths(instance, "A", "D");
        assertEquals(expectedRes, res);
    }

    @Test
    public void TestVerticesInPathsCount(){
        for (int i = 0; i < co.size(); i++)
            instance.addEdge(co.get(i), cd.get(i), cw.get(i));

        ArrayList<LinkedList<String>> paths = Algorithms.allPaths(instance, "A", "D");
        Map<String, Integer> expectedRes = new HashMap<>();
        expectedRes.put("A", 3);
        expectedRes.put("B", 1);
        expectedRes.put("C", 2);
        expectedRes.put("D", 3);
        expectedRes.put("E", 1);

        Map<String, Integer> res = Algorithms.verticesInPathsCount(paths);
        assertEquals(expectedRes, res);
    }
}