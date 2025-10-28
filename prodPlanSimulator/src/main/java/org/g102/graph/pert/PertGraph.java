package org.g102.graph.pert;

import org.g102.graph.Edge;
import org.g102.domain.Activity;
import org.g102.graph.map.MapGraph;
import org.g102.graph.Algorithms;

import java.util.*;

/**
 * Class that represents a PERT/CPM graph, meaning that it is a DAG (directed acyclic graph) with
 * a start and an end vertex, and the edges represent the activities of a project.
 * This implementation is using a MapGraph with Activities as vertices and Integers as edges.
 * This implementation follows an AON (Activity on Node) representation.
 */
public class PertGraph {

    /**
     * Graph of the project using MapGraph implementation with Activities as vertices and Integers as edges
     */
    private MapGraph<Activity, Integer> graph = new MapGraph<>(true);
    /**
     * Reference to the start activity of the graph
     */
    private final Activity start = new Activity("start", "start", 0, "days");
    /**
     * Reference to the final activity of the graph
     */
    private final Activity end = new Activity("end", "end", 0, "days");

    /**
     * Constructor of a PertGraph
     */
    public PertGraph() {
        graph.addVertex(start);
        graph.addVertex(end);
    }

    public Collection<Edge<Activity, Integer>> getIncomingActivities(Activity act){
        return graph.incomingEdges(act);
    }

    /**
     * Add an activity to the graph, connecting it to the start and end activities
     * @param newAct the activity to be added
     * @return true if the activity was added, false otherwise
     */
    public boolean addActivity(Activity newAct) {
        if(graph.addVertex(newAct)){
            if(newAct == start || newAct == end){
                addDependency(start, end, 1);
                return true;
            }else{
                addDependency(start, newAct, 1);
                addDependency(newAct, end, 1);
                return true;
            }
        }
        return false;
    }

    /**
     * Add a dependency (edge) to the graph, connecting two activities
     * removing unnecessary/redundant dependencies if needed
     *
     * @param fromAct  Activity as source vertex
     * @param toAct  Activity as destination vertex
     * @param weight the weight of the edge
     */
    public void addDependency(Activity fromAct, Activity toAct, Integer weight) {
        addActivity(fromAct);
        addActivity(toAct);
        if(graph.adjVertices(fromAct).contains(end)){
            graph.removeEdge(fromAct, end);
        }
        if(graph.adjVertices(start).contains(toAct)){
            graph.removeEdge(start, toAct);
        }
        graph.addEdge(fromAct, toAct, 1);
    }

    /**
     * check if the graph is empty excluding the start and end vertices
     * @return true if the graph is empty, false otherwise
     */
    public boolean isEmpty() {
        return graph.numVertices()-2 == 0;
    }

    /**
     * Check if the PERT/CPM graph is valid, meaning that it is not empty and it is not cyclic
     * @return true if the graph is valid, false otherwise
     */
    public boolean isValid() {
        return !isEmpty() && !Algorithms.isCyclicGraph(graph);
    }

    /**
     * Get vertexes of the graph one of the possible topological orders possible
     * @return a list with the vertexes in topological order
     */
    public LinkedList<Activity> getTopologicalSort() {
        LinkedList<Activity> topsort = Algorithms.KahnsTopologicalSort(graph);
        topsort.remove(start);
        topsort.remove(end);
        return topsort;
    }

    /**
     * Calculate the earliest and latest start and finish times of the activities
     * @return true if the times were calculated, false otherwise
     */
    public boolean calculateTimes(){
        if (!isValid()) return false;
        forwardPass(start,null);
        backwardPass(end,null);
        return true;
    }

    /**
     * Get the total duration of the project
     * @return the total duration of the project
     */
    public int getTotalDuration() {
        if(calculateTimes()){
            return end.getEarliestStart();
        }
        return 0;
    }

    /**
     * Does a forward pass on the graph, calculating the earliest start and finish times of the activities
     * @param currAct current activity
     * @param prevAct previous activity
     */
    private void forwardPass(Activity currAct, Activity prevAct) {
        if(prevAct == null) {
            currAct.setEarliestStart(0);
            currAct.setEarliestFinish(currAct.getDuration());
        }else{
            int possibleES = prevAct.getEarliestFinish();
            int possibleEF = possibleES + currAct.getDuration();
            boolean possibleGreater = possibleES >= currAct.getEarliestStart()
                                      || possibleEF >= currAct.getEarliestFinish();
            boolean firstTime = currAct.getEarliestFinish() == 0;
            if(possibleGreater || firstTime) {
                currAct.setEarliestStart(possibleES);
                currAct.setEarliestFinish(possibleEF);
            }
        }
        for (Activity outVertex : graph.adjVertices(currAct)) {
            forwardPass(outVertex, currAct);
        }
    }

    /**
     * Does a backward pass on the graph, calculating the latest start and finish times of the activities
     * @param currAct current activity
     * @param prevAct previous activity
     */
    private void backwardPass(Activity currAct, Activity prevAct) {
        if(prevAct == null) {
            currAct.setLatestFinish(currAct.getEarliestFinish());
            currAct.setLatestStart(currAct.getLatestFinish() - currAct.getDuration());
        }else{
            // LF = ES of the next activity
            int possibleLF = prevAct.getLatestStart();
            // LS = LF - duration
            int possibleLS = possibleLF - currAct.getDuration();
            boolean possibleSmaller = possibleLF <= currAct.getLatestFinish() || possibleLS <= currAct.getLatestStart();
            boolean firstTime = currAct.getLatestFinish() == 0;
            // If the possible values are smaller than the current values, or it's the first time calculating
            if((possibleSmaller || firstTime) && !currAct.equals(start)) {
                // Update the values
                currAct.setLatestFinish(possibleLF);
                currAct.setLatestStart(possibleLS);
            }
        }
        // Recursive call for the incoming edges until the start vertex is reached
        for (Edge<Activity, Integer> inEdge : graph.incomingEdges(currAct)) {
            backwardPass(inEdge.getVOrig(), currAct);
        }
    }

    /**
     * Gets one of the possible critical paths of the graph
     * @return a linked list with the activities of the critical path
     */
    public ArrayList<LinkedList<Activity>> getCriticalPaths() {
        if(!isValid()) return null;
        calculateTimes();
        ArrayList<LinkedList<Activity>> criticalPaths = new ArrayList<>();
        ArrayList<LinkedList<Activity>> allPaths = Algorithms.allPaths(graph, start, end);
        for (LinkedList<Activity> path : allPaths) {
            if (isCriticalPath(path, 0)){
                if(path.contains(start)) path.remove(start);
                if(path.contains(end)) path.remove(end);
                criticalPaths.add(path);
            }
        }
        return criticalPaths;
    }

    /**
     * Check if a path is a critical path
     * @param path the path to be checked
     * @param currIndex the current index of the path
     * @return true if the path is a critical path, false otherwise
     */
    private boolean isCriticalPath(LinkedList<Activity> path, int currIndex) {
        if (currIndex == path.size() - 1) {
            return true;
        }
        Activity currAct = path.get(currIndex);
        if (currAct.getSlack() != 0){
            return false;
        }
        return isCriticalPath(path, currIndex + 1);
    }

    /**
     * Get the bottleneck activities of the project.
     * Bottleneck activities can be defined as the activities that are most likely to delay the project
     * therefore, they are the activities that are in the most paths
     * @return a linked list with the bottleneck activities
     */
    public LinkedList<Activity> getBottleneckActivities() {
        if(!isValid()) return null;
        ArrayList<LinkedList<Activity>> paths = Algorithms.allPaths(graph, start, end);
        Map<Activity, Integer> verticesInPaths = Algorithms.verticesInPathsCount(paths);
        LinkedList<Activity> bottleNecks = new LinkedList<>();
        Activity maxActivity = null;

        for (Activity activity : verticesInPaths.keySet()) {
            if (maxActivity == null || verticesInPaths.get(activity) > verticesInPaths.get(maxActivity) && activity != start && activity != end) {
                maxActivity = activity;
            }
        }

        for (Activity activity : verticesInPaths.keySet()) {
            if (verticesInPaths.get(activity).equals(verticesInPaths.get(maxActivity)) && activity != start && activity != end) {
                bottleNecks.add(activity);
            }
        }

        return bottleNecks;
    }

    /**
     * Delay an activity of the project
     * @param activity the activity to be delayed
     * @param delay the delay to be added to the activity
     * @return true if the activity was delayed, false otherwise
     */
    public boolean delayActivity(Activity activity, int delay){
        // validate if graph is empty
        if(isEmpty()) return false;
        // validate if activity exists
        int activityKey = graph.key(activity);
        if(graph.vertex(activityKey) == null) return false;
        // change activity duration according to delay, if it is not possible return false
        if(!graph.vertex(activityKey).doDelay(delay)) return false;
        // recalculate the graph
        calculateTimes();
        return true;
    }

    /**
     * Get the activities of the project
     * @return an array list with the activities of the project
     */
    public ArrayList<Activity> getActivities() {
        ArrayList<Activity> vertices = graph.vertices();
        vertices.remove(start);
        vertices.remove(end);
        return vertices;
    }

    /**
     * Clone the PertGraph
     * @return a new PertGraph with the same vertices and edges
     */
    @Override
    public PertGraph clone() {
        PertGraph newGraph = new PertGraph();
        for (Activity vertex : graph.vertices()) {
            newGraph.addActivity(vertex.clone());
        }
        for (Edge<Activity, Integer> edge : graph.edges()) {
            newGraph.addDependency(edge.getVOrig().clone(), edge.getVDest().clone(), edge.getWeight());
        }
        return newGraph;
    }

}
