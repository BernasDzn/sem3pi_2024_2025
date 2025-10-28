package org.g102._controllers;

import org.g102.domain.Item;
import org.g102.domain.Machine;
import org.g102.domain.Time;
import org.g102.tools.ConsoleWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class TimelineController {
    //static List<Machine> machines = SimulationController.machines;
    //static List<Item> items = SimulationController.items;
    //static int simulationTime = SimulationController.getSimulationTime();
    public static void timelinePlantUML(List<Machine> machines, boolean isTest) {
        machines.sort(Comparator.comparing(Machine::getId));
        try {
            Map<Integer, List<Machine>> orderedList = getOrderedPrintList(machines);
            File f;

            if (isTest) {
                f = new File("../prodPlanSimulator/src/main/resources/timeline.puml");
            }
            else {
                f = new File("prodPlanSimulator/src/main/resources/timeline.puml");
            }

            FileWriter fw = new FileWriter(f);
            fw.write("@startuml\n");
            fw.write("scale 100 as 200 pixels\n");
            for (Machine machine : machines) {
                fw.write("concise \"Machine " + machine.getId() + "\" as " + machine.getId() + "\n");
            }
            for (Integer i : orderedList.keySet()) {
                fw.write("@" + i + "\n");
                if (i != 0) {
                    for (Machine machine : orderedList.get(i)) {

                        if (machine.getTimeActiveInSeconds().containsKey(i)) {
                            fw.write(machine.getId() + " is 1 #palegreen\n");
                        } else {
                            fw.write(machine.getId() + " is 0 #pink\n");
                        }
                    }
                }
                if (i == 0) {
                    for (Machine machine : machines) {
                        if (machine.getTimeActiveInSeconds().containsKey(i)) {
                            fw.write(machine.getId() + " is 1 #palegreen\n");
                        } else {
                            fw.write(machine.getId() + " is 0 #pink\n");
                        }
                    }
                }
            }
            fw.write("@enduml\n");
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static TreeMap<Integer, List<Machine>> getOrderedPrintList(List<Machine> machines) {
        TreeMap<Integer, List<Machine>> printList = new TreeMap<>();
        for (Machine machine : machines) {
            for (Time i : machine.getTimeActive().keySet()) {
                printList.put(i.toSeconds(), null);
            }
            for (Time i : machine.getTimeActive().values()) {
                printList.put(i.toSeconds(), null);
            }
        }
        for (Integer i : printList.keySet()) {
            List<Machine> machineList = new ArrayList<>();
            for (Machine machine : machines) {
                if (machine.getTimeActiveInSeconds().containsKey(i) || machine.getTimeActiveInSeconds().containsValue(i)) {
                    machineList.add(machine);
                }
            }
            printList.put(i, machineList);
        }
        return printList;
    }


    public static void itemTimeLinePlantUML(List<Machine> machines, List<Item> items, boolean isTest) {
        machines.sort(Comparator.comparing(Machine::getId));
        try {
            File f;
            if (isTest) {
                f = new File("../prodPlanSimulator/src/main/resources/timelineItems.puml");
            }
            else {
                f = new File("prodPlanSimulator/src/main/resources/timelineItems.puml");
            }

            FileWriter fw = new FileWriter(f);
            fw.write("@startuml\n");
            fw.write("scale 100 as 400 pixels\n");
            Map<Item, String> itemNames = new LinkedHashMap<>();

            items.sort(Comparator.comparing(Item::getId));

            for (Item item : items) {
                int i = 0;
                while (itemNames.containsValue("item_" + item.getId() +"_"+item.getPriority() + "_" + i)) {
                    i++;
                }
                itemNames.put(item, "item_" + item.getId() + "_" + item.getPriority() + "_" + i);
            }
            for (Item item : items) {
                fw.write("concise \"" + itemNames.get(item) + "\" as " + itemNames.get(item) + "\n");
            }
            for (Item i : items) {
                for (Machine machine : machines) {
                    if (i.getMachinesUsed().contains(machine)) {
                        if (i.getMachinesUsed().getLast() == machine) {
                            fw.write("@" + machine.getItemProcessedTime(i) + "\n");
                            fw.write(itemNames.get(i) + " is " + machine.getOperationName() + "_" + machine.getId() + "\n");
                            fw.write("@" + (machine.getItemEndTime(i).toSeconds() + machine.getOperationTime().toSeconds()) + "\n");
                            fw.write(itemNames.get(i) + " is DONE #palegreen\n");
                        } else {
                            if(machine.getItemEndTime(i).toSeconds()+machine.getOperationTime().toSeconds() != i.getMachinesUsed().get(i.getMachinesUsed().indexOf(machine)+1).getItemProcessedTime(i)) {
                                fw.write("@" + machine.getItemProcessedTime(i) + "\n");
                                fw.write(itemNames.get(i) + " is " + machine.getOperationName() + "_" + machine.getId() + "\n");
                                fw.write("@" + (machine.getItemEndTime(i).toSeconds() + machine.getOperationTime().toSeconds()) + "\n");
                                fw.write(itemNames.get(i) + " is WAIT #cornsilk\n");
                            }
                            else{
                                fw.write("@" + machine.getItemProcessedTime(i) + "\n");
                                fw.write(itemNames.get(i) + " is " + machine.getOperationName() + "_" + machine.getId() + "\n");
                            }
                        }
                    }
                }
            }

            fw.write("@enduml\n");
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void printTimelineConsole(List<Machine> machines, int simulationTime) {
        machines.sort(Comparator.comparing(Machine::getId));
        StringBuilder timeline = new StringBuilder();
        int maxTime = simulationTime;
        int scale = calculateScaleToTwentyFiveIntervals(maxTime);
        System.out.println("Scale: " + scale);
        Scanner sc = new Scanner(System.in);
        ConsoleWriter.displayCommand("ENTER TO DISPLAY TIMELINE");
        sc.nextLine();
        for (int i = 0; i <= maxTime; i += scale) {
            if (i != 0) {
                timeline.append("___");
            }
            timeline.append(i);
            if (i + scale > maxTime) {
                timeline.append("___");
                timeline.append(i + scale);
            }
        }
        List<String> lines = new ArrayList<>();
        List<String> lineEntries = new ArrayList<>();
        ArrayList<Integer> timelinePositions = new ArrayList<>();
        for (String s : timeline.toString().split("___")) {
            timelinePositions.add(Integer.valueOf(s));
        }
        for (Machine machine : machines) {
            if (!machine.getTimeActive().isEmpty()) {
                lineEntries.add("Machine " + machine.getId() + "|");
                StringBuilder line = new StringBuilder();
                List<Integer> startPos = new ArrayList<>();
                List<Integer> endPos = new ArrayList<>();
                for (Time start : machine.getTimeActive().keySet()) {
                    startPos.add(start.toSeconds());
                    endPos.add(machine.getTimeActive().get(start).toSeconds());
                }
                List<Integer> startPosCharacters = new ArrayList<>();
                List<Integer> endPosCharacters = new ArrayList<>();
                for (Integer pos : startPos) {
                    startPosCharacters.add(pos * maxTime / timeline.length());
                }
                for (Integer pos : endPos) {
                    endPosCharacters.add(pos * maxTime / timeline.length());
                }
                for (int i = 0; i < timeline.length(); i++) {
                    if (i >= startPosCharacters.getFirst() && i <= endPosCharacters.getFirst()) {
                        line.append("#");
                    } else {
                        line.append(" ");
                    }
                }
                lines.add(line.toString());
            }
        }
        int max = 0;
        for (String entry : lineEntries) {
            if (entry.length() > max) {
                max = entry.length();
            }
        }
        for (int i = 0; i < max - 1; i++) {
            timeline.insert(0, " ");
        }
        timeline.insert(max - 1, "|");

        System.out.println("Timeline:");
        System.out.println(timeline);
        for (int i = 0; i < lines.size(); i++) {
            System.out.println(lineEntries.get(i) + lines.get(i));
        }
    }

    public static int calculateScaleToTwentyFiveIntervals(int maxTime) {
        int scale = 1;
        while (maxTime / scale > 2000) {
            scale++;
        }
        return scale;
    }

}
