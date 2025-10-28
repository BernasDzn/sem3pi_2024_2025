package org.g102._ui.console;

import org.g102._controllers.USLP06_controller;
import org.g102._ui.console._Menu.MenuUI;
import org.g102.tools.ConsoleWriter;
import org.g102.tools.InputReader;

import java.util.ArrayList;
import java.util.List;

public class OrdersToFileUI implements Runnable {

    USLP06_controller controller;

    public OrdersToFileUI(){
        controller = new USLP06_controller();
    }

    @Override
    public void run() {
        List<String> orderIds = controller.getAllOrdersIds();
        orderIds.addFirst("Export All");
        orderIds.addFirst("Export Selected");
        if(!orderIds.isEmpty()){
            ConsoleWriter.displayHeader("Select the order(s) to export to a file:");
            int selectedOption = 0;
            List<String> selectedOrders = new ArrayList<>();
            while(selectedOption != -1 ){
                selectedOption = InputReader.showListReturnIndex(orderIds, "Select Order (0 to finish)");
                if(selectedOption > 2){
                    selectedOrders.add(orderIds.get(selectedOption-1));
                    orderIds.remove(selectedOption-1);
                }
                if (orderIds.size() == 2){
                    selectedOption = -1;
                    System.out.println("No more orders available to select.");
                }
                if (selectedOption == 2){
                    selectedOrders = orderIds.subList(2, orderIds.size());
                    selectedOption = -1;
                    System.out.println("All orders selected.");
                }
                if (selectedOption == 1 && !selectedOrders.isEmpty()){
                    selectedOption = -1;
                }else if(selectedOption == 1){
                    System.out.println("No orders selected.");
                }
                if (selectedOption == -1){
                    ConsoleWriter.displaySuccess("Choose folder to save the file");
                }
            }
            if(controller.exportOrdersToFile(selectedOrders)){
                ConsoleWriter.displaySuccess("Orders exported successfully");
                MenuUI.showMenu();
            }else{
                ConsoleWriter.displayError("Error exporting orders to file");
                MenuUI.showMenu();
            }
        }else {
            ConsoleWriter.displayError("No orders found");
            MenuUI.showMenu();
        }
    }



}
