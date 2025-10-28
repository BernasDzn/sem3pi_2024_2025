package org.g102.tools.file;

import org.g102.domain.Activity;
import org.g102.graph.Edge;
import org.g102.graph.pert.PertGraph;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

public class PertGraphWriter {

    private PertGraph graph;
    private String headerString = "act_id,cost,duration,es,ls,ef,lf,prev_act_id1,...,prev_act_idN";

    public PertGraphWriter(PertGraph graph) {
        this.graph = graph;
    }

    public String getHeader() {
        return headerString;
    }

    public void setHeader(String header) {
        this.headerString = header;
    }

    public boolean writeTo(String filepath, boolean header){
        try {
            graph.calculateTimes();
            FileWriter writer = new FileWriter(filepath+".csv");
            if(header) {
                writer.write(headerString+"\n");
            }
            ArrayList<Activity> activities = graph.getActivities();
            activities.sort(Comparator.comparing(Activity::getId));
            for(Activity a : activities) {
                if(!a.getId().equals("start") && !a.getId().equals("end")) {
                    String line = "";
                    line += a.getId() + ","+a.getCost()+","+a.getDuration()+","+a.getEarliestStart()+","
                            +a.getLatestStart()+","+a.getEarliestFinish()+","+a.getLatestFinish()+",";
                    for(Edge e : graph.getIncomingActivities(a)) {
                        if(!((Activity)e.getVOrig()).getId().equals("start")) {
                            line+=((Activity)e.getVOrig()).getId()+",";
                        }
                    }
                    line=line.substring(0,line.length()-1);
                    line+="\n";
                    writer.write(line);
                }
            }
            writer.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

}
