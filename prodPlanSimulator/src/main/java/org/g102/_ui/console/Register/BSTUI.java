package org.g102._ui.console.Register;

import org.g102._controllers.Register.BSTController;

import org.g102.tools.InputReader;
import org.g102._ui.console._Menu.MenuUI;

import java.util.List;

public class BSTUI implements Runnable {

    BSTController controller;

    public BSTUI() {
        controller = new BSTController();
    }

    @Override
    public void run() {
        int selectedOption = InputReader.showListReturnIndex(List.of(
                "Show Materials in descending order"
                , "Show Materials in ascending order"
                , "Go Back"), null);
        switch (selectedOption) {
            case 1:
                System.out.println(controller.showAscendingOrder());
                break;
            case 2:
                System.out.println(controller.showDescendingOrder());
                break;
            case 9:
                MenuUI.showMenu();
                break;
        }
    }
}

