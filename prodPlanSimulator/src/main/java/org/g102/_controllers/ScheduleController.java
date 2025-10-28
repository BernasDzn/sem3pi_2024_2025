package org.g102._controllers;

import org.g102.domain.Activity;
import org.g102.graph.pert.PertGraph;
import org.g102.graph.Algorithms;
import org.g102.tools.file.FileReader;
import org.g102.tools.file.PertGraphWriter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ScheduleController {

    private PertGraph pertGraph;
    private PertGraph delayedpertGraph;

    public ScheduleController(){
        pertGraph = new PertGraph();
        delayedpertGraph = new PertGraph();
    }

    public boolean generatePertCPM(String csvFileLocation){
        String[][] csvFileMatrix;
        try {
            csvFileMatrix = FileReader.readCSV(csvFileLocation, true, ",");
        }catch (Exception e){
            return false;
        }
        return Algorithms.inputToPertGraph(csvFileMatrix, pertGraph);
    }

    public void startDelay(){
        delayedpertGraph = pertGraph.clone();
    }

    public boolean delayActivity(Activity activity, int delay){
        return delayedpertGraph.delayActivity(activity, delay);
    }

    public List<Activity> getActivityListDelayed(){
        return delayedpertGraph.getActivities();
    }

    public LinkedList<Activity> getActivitiesSorted(boolean delayed) {
        if(delayed) return delayedpertGraph.getTopologicalSort();
        return pertGraph.getTopologicalSort();
    }

    public ArrayList<LinkedList<Activity>> getCriticalPath(boolean delayed) {
        if(delayed) return delayedpertGraph.getCriticalPaths();
        return pertGraph.getCriticalPaths();
    }

    public LinkedList<Activity> getBottleneckActivities(boolean delayed) {
        if(delayed) return delayedpertGraph.getBottleneckActivities();
        return pertGraph.getBottleneckActivities();
    }

    public boolean tryExportToCSV(String fileLocation) {
        PertGraphWriter writer = new PertGraphWriter(pertGraph);
        return writer.writeTo(fileLocation, true);
    }

    public int getTotalDuration(boolean delayed) {
        if(delayed) return delayedpertGraph.getTotalDuration();
        return pertGraph.getTotalDuration();
    }

    public void reset() {
        pertGraph = new PertGraph();
        delayedpertGraph = new PertGraph();
    }

}
