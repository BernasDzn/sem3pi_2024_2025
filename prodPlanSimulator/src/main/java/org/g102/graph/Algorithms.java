package org.g102.graph;

import org.g102.domain.Activity;
import org.g102.graph.map. MapGraph;
import org.g102.graph.pert.PertGraph;

import java.util.*;
import java.util.function.BinaryOperator;

public class Algorithms {

    /**
     * Converts a csv file as a String Matrix to a PertGraph
     * @param graphInput String Matrix with the graph information
     * @param g PertGraph instance
     * @return true if the graph is valid, false otherwise
     */
    public static boolean inputToPertGraph(String[][] graphInput, PertGraph g){
        //ActivKey,descr,duration,duration-unit,tot-cost,predecessors
        try{
             HashMap<String, Activity> activities = new HashMap<>();
             for (int i = 0; i < graphInput.length; i++) {
                 Activity act = new Activity(graphInput[i][0], graphInput[i][1], Integer.parseInt(graphInput[i][2]), graphInput[i][3], Integer.parseInt(graphInput[i][4]), " ");
                 activities.put(graphInput[i][0], act);
             }
             for (int i = 0; i < graphInput.length; i++) {
                 Activity act = activities.get(graphInput[i][0]);
                 if (graphInput[i].length > 4) {
                     if (graphInput[i][5] == null) {
                         g.addActivity(act);
                     }else if(graphInput[i][5].isEmpty()){
                         g.addActivity(act);
                     }else{
                         for (int j = 5; j < graphInput[i].length; j++) {
                             if (graphInput[i][j] != null) {
                                 if(!graphInput[i][j].isEmpty()){
                                     graphInput[i][j] = graphInput[i][j].replace("\"", "");
                                     g.addDependency(activities.get(graphInput[i][j]), act, 1);
                                 }
                             }
                         }
                     }
                 }
             }
             if(g.isValid()){
                 return true;
             }else{
                 return false;
             }
         }catch(Exception E){
            System.out.println(E.getMessage());
             return false;
         }
    }

    /** Performs breadth-first search of a Graph starting in a vertex
     *
     * @param g Graph instance
     * @param vert vertex that will be the source of the search
     * @return a LinkedList with the vertices of breadth-first search
     */
    public static <V, E> LinkedList<V> BreadthFirstSearch(Graph<V, E> g, V vert) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Performs backtrack to one possible start vertex with no incoming edges
     * @param g Graph instance
     * @return the start vertex
     */
    public static <V, E> List<V> GetStartVertices(Graph<V, E> g) {
        List<V> startVertices = new ArrayList<>();
        for (V v : g.vertices()){
            if(g.incomingEdges(v).isEmpty()){
                startVertices.add(v);
            }
        }
        return startVertices;
    }

    /** Performs depth-first search starting in a vertex
     *
     * @param g Graph instance
     * @param vert vertex of graph g that will be the source of the search
     * @return a LinkedList with the vertices of depth-first search
     */
    public static <V, E> LinkedList<V> DepthFirstSearch(Graph<V, E> g, V vert) {
        LinkedList<V> dfs = new LinkedList<>();
        DepthFirstSearch(g, vert, dfs);
        return dfs;
    }

    /** Performs depth-first search starting in a vertex
     *
     * @param g Graph instance
     * @param vOrig vertex of graph g that will be the source of the search
     * @param qdfs a Quasi DFS LinkedList with vertices of depth-first search
     */
    private static <V, E> void DepthFirstSearch(Graph<V, E> g, V vOrig, LinkedList<V> qdfs) {
        if(qdfs.contains(vOrig)) return;
        qdfs.add(vOrig);
        for(V vAdj : g.adjVertices(vOrig)) {
            DepthFirstSearch(g, vAdj, qdfs);
        }
    }

    public static <V, E> LinkedList<V> topologicalSort(Graph<V, E> g){
        if(isCyclicGraph(g)){
            throw new IllegalArgumentException("The graph has a cycle.");
        }
        LinkedList<V> topsort = new LinkedList<>();
        List<V> visited = new ArrayList<>();
        List<V> originVerts = GetStartVertices(g);
        for(V v : originVerts){
            if(!visited.contains(v)){
                topologicalSort(g, v, topsort,visited);
            }}
        return topsort;
    }

    private static <V,E> void topologicalSort(Graph<V, E> g, V vOrig, LinkedList<V> topsort,List<V> visited){
        visited.add(vOrig);
        for(V vAdj : g.adjVertices(vOrig)){
            if(!visited.contains(vAdj)){
                topologicalSort(g, vAdj, topsort,visited);
            }
        }
        topsort.push(vOrig);
    }

    /**
     * Returns the graph topologically sorted using Kahn's Topological Sort Algorithm
     * @param g Graph instance
     * @return a LinkedList with the vertices topologically sorted
     */
    public static <V, E> LinkedList<V> KahnsTopologicalSort(Graph<V, E> g){
        LinkedList<V> topsort = new LinkedList<>();
        Map<V,Integer> inDegree =  new HashMap<>();
        for( V v : g.vertices()){
            inDegree.put(v,0);
        }
        for(V v : g.vertices()){
            for(V adj : g.adjVertices(v)){
                inDegree.put(adj, inDegree.get(adj) + 1); // all the vertices( here named adj) outgoing v get added one to their degree
            }
        }

        Queue<V> queue = new LinkedList<>();
        for(Map.Entry<V, Integer> v : inDegree.entrySet()){
            if(v.getValue()==0){ //if the v degree is 0, it's added to the queue
                queue.add(v.getKey());
            }}

        int numProcessed= 0;
        while(!queue.isEmpty()){
            V v = queue.poll(); //remove head
            topsort.add(v);
            numProcessed++;

            for(V adj : g.adjVertices(v)){
                inDegree.put(adj, inDegree.get(adj)-1); //remove one from the adj of v (now added to the topsort and removed from queue)
                if(inDegree.get(adj) == 0){
                    queue.add(adj); // Add to the queue if adj degree becomes 0
                }
            }
        }
        if(numProcessed!=g.numVertices()) {
            throw new IllegalArgumentException("The graph has a cycle.");
        }
        return topsort;
    }

    /**
     * Checks if the graph has a cycle
     * @param g Graph instance
     * @return true if the graph has a cycle, false otherwise
     */
    public static <V, E> boolean isCyclicGraph(Graph<V, E> g){
        HashMap<V, Boolean> visited = new HashMap<>();
        List<V> startVertices = GetStartVertices(g);
        if(startVertices.isEmpty()) return true;
        for(V v : startVertices){
            if(isCyclicGraph(g, v, visited)){
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the graph has a cycle
     * @param g Graph instance
     * @param vOrig vertex of graph g that will be the source of the search
     * @param visited array with the visited vertices
     * @return true if the graph has a cycle, false otherwise
     */
    private static <V, E> boolean isCyclicGraph(Graph<V,E> g, V vOrig, HashMap<V, Boolean> visited) {
        visited.put(vOrig, true);
        for(V vAdj : g.adjVertices(vOrig)) {
            if(visited.get(vAdj) == null) {
                return isCyclicGraph(g, vAdj, visited);
            }else if(visited.get(vAdj)) {
                return true;
            }
        }
        return false;
    }

    /** Returns all paths from vOrig to vDest
     *
     * @param g     Graph instance
     * @param vOrig information of the Vertex origin
     * @param vDest information of the Vertex destination
     * @return paths ArrayList with all paths from vOrig to vDest
     */
    public static <V, E> ArrayList<LinkedList<V>> allPaths(Graph<V, E> g, V vOrig, V vDest) {
        ArrayList<LinkedList<V>> paths = new ArrayList<>();
        LinkedList<V> path = new LinkedList<>();
        boolean[] visited = new boolean[g.numVertices()];

        allPaths(g, vOrig, vDest, visited, path, paths);
        return paths;
    }

    /** Returns all paths from vOrig to vDest
     *
     * @param g       Graph instance
     * @param vOrig   Vertex that will be the source of the path
     * @param vDest   Vertex that will be the end of the path
     * @param visited set of discovered vertices
     * @param path    stack with vertices of the current path (the path is in reverse order)
     * @param paths   ArrayList with all the paths (in correct order)
     */
    private static <V, E> void allPaths(Graph<V, E> g, V vOrig, V vDest, boolean[] visited,
                                        LinkedList<V> path, ArrayList<LinkedList<V>> paths) {
        visited[g.key(vOrig)] = true;
        path.add(vOrig);

        if (vOrig.equals(vDest)) {
            paths.add(new LinkedList<>(path));
        } else {
            for (V vAdj : g.adjVertices(vOrig)) {
                if (!visited[g.key(vAdj)]) {
                    allPaths(g, vAdj, vDest, visited, path, paths);
                }
            }
        }

        path.removeLast();
        visited[g.key(vOrig)] = false;
    }

    /** Returns the number of times each vertex appears in the paths
     *
     * @param paths ArrayList with all paths
     * @return HashMap with the number of times each vertex appears in the paths
     */
    public static <V> Map<V, Integer> verticesInPathsCount(ArrayList<LinkedList<V>> paths) {
        Map<V, Integer> verticesCount = new HashMap<>();
        for (LinkedList<V> path : paths) {
            for (V vertex : path) {
                if (verticesCount.containsKey(vertex)) {
                    verticesCount.put(vertex, verticesCount.get(vertex) + 1);
                } else {
                    verticesCount.put(vertex, 1);
                }
            }
        }
        return verticesCount;
    }


    /**
     * Computes shortest-path distance from a source vertex to all reachable
     * vertices of a graph g with non-negative edge weights
     * This implementation uses Dijkstra's algorithm
     *
     * @param g        Graph instance
     * @param vOrig    Vertex that will be the source of the path
     * @param visited  set of previously visited vertices
     * @param pathKeys minimum path vertices keys
     * @param dist     minimum distances
     */
    private static <V, E> void shortestPathDijkstra(Graph<V, E> g, V vOrig,
                                                    Comparator<E> ce, BinaryOperator<E> sum, E zero,
                                                    boolean[] visited, V [] pathKeys, E [] dist) {

        throw new UnsupportedOperationException("Not supported yet.");
    }


    /** Shortest-path between two vertices
     *
     * @param g graph
     * @param vOrig origin vertex
     * @param vDest destination vertex
     * @param ce comparator between elements of type E
     * @param sum sum two elements of type E
     * @param zero neutral element of the sum in elements of type E
     * @param shortPath returns the vertices which make the shortest path
     * @return if vertices exist in the graph and are connected, true, false otherwise
     */
    public static <V, E> E shortestPath(Graph<V, E> g, V vOrig, V vDest,
                                        Comparator<E> ce, BinaryOperator<E> sum, E zero,
                                        LinkedList<V> shortPath) {

        throw new UnsupportedOperationException("Not supported yet.");
    }

    /** Shortest-path between a vertex and all other vertices
     *
     * @param g graph
     * @param vOrig start vertex
     * @param ce comparator between elements of type E
     * @param sum sum two elements of type E
     * @param zero neutral element of the sum in elements of type E
     * @param paths returns all the minimum paths
     * @param dists returns the corresponding minimum distances
     * @return if vOrig exists in the graph true, false otherwise
     */
    public static <V, E> boolean shortestPaths(Graph<V, E> g, V vOrig,
                                               Comparator<E> ce, BinaryOperator<E> sum, E zero,
                                               ArrayList<LinkedList<V>> paths, ArrayList<E> dists) {

        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Extracts from pathKeys the minimum path between voInf and vdInf
     * The path is constructed from the end to the beginning
     *
     * @param g        Graph instance
     * @param vOrig    information of the Vertex origin
     * @param vDest    information of the Vertex destination
     * @param pathKeys minimum path vertices keys
     * @param path     stack with the minimum path (correct order)
     */
    private static <V, E> void getPath(Graph<V, E> g, V vOrig, V vDest,
                                       V [] pathKeys, LinkedList<V> path) {

        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Forward path
     */
    public static <V, E> HashMap<Activity, int[]> backwardPass(MapGraph<Activity, E> g, Activity vDest) {
        HashMap<Activity, int[]> latestStartLatestFinish = new HashMap<>();
        recursiveBackwardPass(g, vDest, latestStartLatestFinish);
        return latestStartLatestFinish;
    }

    private static <V, E> void recursiveBackwardPass(MapGraph<Activity, E> g, Activity currentVertice, HashMap<Activity, int[]> latestStartLatestFinish) {
        int latestStart = currentVertice.getDuration();
        int latestFinish = currentVertice.getDuration();
        int min = Integer.MAX_VALUE;
        for(Edge<Activity, E> e : g.outgoingEdges(currentVertice)){
            if(latestStartLatestFinish.get(e.getVDest())==null) return;
            min = Math.min(latestStartLatestFinish.get(e.getVDest())[0], min);
        }
        if(min != Integer.MAX_VALUE){
            latestFinish = min;
            latestStart = latestFinish - currentVertice.getDuration();
        }
        int[] latestStartLatestFinishValues = {latestStart, latestFinish};
        latestStartLatestFinish.put(currentVertice, latestStartLatestFinishValues);
        for(Edge<Activity, E> e : g.incomingEdges(currentVertice)){
            recursiveBackwardPass(g, e.getVOrig(), latestStartLatestFinish);
        }
    }

}
