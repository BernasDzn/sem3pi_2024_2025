package org.g102._ui.console;

import org.g102._controllers.USLP06_controller;
import org.g102._ui.console._Menu.MenuUI;
import org.g102.tools.ConsoleWriter;

import java.io.File;

public class SimulateBatchProductionUI implements Runnable {

    USLP06_controller controller;

    public SimulateBatchProductionUI(){
        controller = new USLP06_controller();
    }

    @Override
    public void run() {
        System.out.println("Select the csv with the orders to simulate batch production:");
        File ordersFile = controller.requestOrdersFile();
        if(ordersFile != null){
            if(controller.simulateBatchProduction(ordersFile)){
                ConsoleWriter.displaySuccess("Batch production simulated successfully");
            }else {
                ConsoleWriter.displayError("Error simulating batch production");
            }
        }else {
            ConsoleWriter.displayError("Error reading orders file");
        }
        MenuUI.showMenu();
    }




}
