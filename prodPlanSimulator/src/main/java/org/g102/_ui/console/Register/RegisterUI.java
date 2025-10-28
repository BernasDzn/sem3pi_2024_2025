package org.g102._ui.console.Register;

import org.g102.tools.ConsoleWriter;
import org.g102.tools.InputReader;
import org.g102._ui.console._Menu.MenuUI;

import java.util.List;

public class RegisterUI implements Runnable{

        @Override
        public void run() {
            showMenu();
        }

    public void showMenu(){
        ConsoleWriter.displayHeader("Register");
        int selectedOption = InputReader.showListReturnIndex(List.of(
                "Workstation"
                ,"Product"
                ,"Order"
                ,"Deactivate Customer"
                , "Go Back"), null);
        switch (selectedOption) {
            case 1:
                new RegisterWorkstationUI().run();
                showMenu();
                break;
            case 2:
                new RegisterProductUI().run();
                showMenu();
                break;
            case 3:
                new RegisterOrderUI().run();
                showMenu();
                break;
            case 4:
                new DeactivateCustomerUI().run();
                showMenu();
                break;
            case 5:
                MenuUI.showMenu();
                break;
        }
    }
}
