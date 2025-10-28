package org.g102._ui.console;

import org.g102._controllers.ScheduleController;
import org.g102._ui.console._Menu.MenuUI;
import org.g102.domain.Activity;
import org.g102.tools.ConsoleWriter;
import org.g102.tools.InputReader;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ScheduleUI implements Runnable {

    ScheduleController controller;

    public ScheduleUI() {
        controller = new ScheduleController();
    }

    @Override
    public void run() {
        controller.reset();
        ConsoleWriter.displayHeader("Schedule Management");
        int selectedOption = InputReader.showListReturnIndex(List.of(
                "import Schedule"
                , "Go Back"), null);
        switch (selectedOption) {
            case 1:
                String scheduleFile = requestFile();
                if(controller.generatePertCPM(scheduleFile)){
                    ConsoleWriter.displaySuccess("Schedule imported successfully");
                    showMenu();
                }else {
                    ConsoleWriter.displayError("Error importing schedule");
                    run();
                }
                break;
            case 2:
                MenuUI.showMenu();
                break;
        }
    }

    public void showMenu(){
        ConsoleWriter.displayHeader("Schedule Management");
        int selectedOption = InputReader.showListReturnIndex(List.of(
                "Get activities topologically sorted"
                ,"Get schedule critical paths"
                ,"Get bottleneck activities"
                ,"Simulate delays"
                ,"Export to CSV"
                , "Go Back"), null);
        switch (selectedOption) {
            case 1:
                try {
                    showVertexList(controller.getActivitiesSorted(false), "Activities topologically sorted");
                } catch (Exception e) {
                    ConsoleWriter.displayError("Error getting activities by priority");
                }
                showMenu();
                break;
            case 2:
                try {
                    ArrayList<LinkedList<Activity>> criticalPaths = controller.getCriticalPath(false);
                    for (LinkedList<Activity> criticalPath : criticalPaths) {
                        showListAsGraphPath(criticalPath);
                    }
                } catch (Exception e) {
                    ConsoleWriter.displayError("Error getting critical path");
                }
                showMenu();
                break;
            case 3:
                try {
                    showVertexList(controller.getBottleneckActivities(false), "Bottleneck Activities");
                } catch (Exception e) {
                    ConsoleWriter.displayError("Error getting bottleneck activities");
                }
                showMenu();
                break;
            case 4:
                controller.startDelay();
                SimulateDelaysMenu();
                break;
            case 5:
                exportToCSV();
                showMenu();
                break;
            case 6:
                MenuUI.showMenu();
                break;
        }
    }

    private void exportToCSV() {
        if(controller.tryExportToCSV(requestFile())){
            ConsoleWriter.displaySuccess("Exported to CSV successfully");
        }else{
            ConsoleWriter.displayError("Error exporting to CSV");
        }
    }

    private void showVertexList(List<?> vertexList, String title) {
        System.out.printf("\n");
        if(title != null){
            System.out.printf(title + ": \n");
        }
        StringBuilder vertexListString = new StringBuilder();
        int nextLineCounter = 0;
        for (Object vertex : vertexList) {
            if (nextLineCounter == 5) {
                vertexListString.append("\n");
                nextLineCounter = 0;
            }
            nextLineCounter++;
            vertexListString.append(vertex).append(", ");
        }
        vertexListString.deleteCharAt(vertexListString.length() - 2);
        System.out.printf(vertexListString + "\n");
    }

    private void showListAsGraphPath(LinkedList<?> listOfVertices) {
        System.out.printf("\n");
        System.out.printf("Start -> ");
        int nextLineCounter = 0;
        for (Object vertex : listOfVertices) {
            if (nextLineCounter == 5) {
                System.out.printf("\n");
                nextLineCounter = 0;
            }
            nextLineCounter++;
            System.out.printf(vertex + " -> ");
        }
        System.out.printf("End\n");
    }

    private void SimulateDelaysMenu() {
        int selectedOption = InputReader.showListReturnIndexWithExit(
                controller.getActivityListDelayed(), "Activity to delay");

        if (selectedOption == -1) showMenu();

        Activity selectedActivity = controller.getActivityListDelayed().get(selectedOption-1);

        int delay = InputReader.readInt("Delay amount");

        if(controller.delayActivity(selectedActivity, delay)){
            ConsoleWriter.displaySuccess("Activity delayed successfully");
            ConsoleWriter.displayHeader("Simulate Delays");
            int selectedOption2 = InputReader.showListReturnIndex(List.of(
                    "Delay another activity"
                    , "Simulate delay Impact"
                    , "Go Back"), null);
            switch (selectedOption2) {
                case 1:
                    SimulateDelaysMenu();
                    break;
                case 2:
                    simulateDelayImpact();
                    break;
                case 3:
                    MenuUI.showMenu();
                    break;
            }
        }else {
            ConsoleWriter.displayError("Error delaying activity");
            SimulateDelaysMenu();
        }
    }

    private void simulateDelayImpact() {
        System.out.printf("\nOriginal PertCPM topological Sort: ");
        showListAsGraphPath(controller.getActivitiesSorted(false));
        System.out.printf("Delayed PertCPM topological Sort: ");
        showListAsGraphPath(controller.getActivitiesSorted(true));
        System.out.println();
        System.out.printf("Original PertCPM bottleneck Activities: ");
        showVertexList(controller.getBottleneckActivities(false), null);
        System.out.printf("Delayed PertCPM bottleneck Activities: ");
        showVertexList(controller.getBottleneckActivities(true), null);
        System.out.println();
        System.out.printf("Original PertCPM critical Path(s): ");
        for (LinkedList<Activity> criticalPath : controller.getCriticalPath(false)) {
            showListAsGraphPath(criticalPath);
        }
        System.out.printf("Delayed PertCPM critical Path(s): ");
        for (LinkedList<Activity> criticalPath : controller.getCriticalPath(true)) {
            showListAsGraphPath(criticalPath);
        }
        System.out.println();
        System.out.printf("Overall Delay Impact: \n");
        System.out.printf("Original PertCPM Duration: " + controller.getTotalDuration(false) + " days \n");
        System.out.printf("Delayed PertCPM Duration: " + controller.getTotalDuration(true) + " days ( + " + (controller.getTotalDuration(true) - controller.getTotalDuration(false)) + " days ) \n");

        showMenu();

    }

    public File requestSaveFile(){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Specify a file to save");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int userSelection = fileChooser.showSaveDialog(null);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        }
        return null;
    }

    public String requestFile(){
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile().getAbsolutePath();
        }
        return null;
    }

}
