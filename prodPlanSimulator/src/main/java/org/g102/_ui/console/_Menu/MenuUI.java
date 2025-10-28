package org.g102._ui.console._Menu;

import org.g102.tools.InputReader;
import org.g102._ui._AuthDomain.UserRoles;
import org.g102._ui._Session.AppSession;
import org.g102._ui.console.*;
import org.g102._ui.console.List.ListUI;
import org.g102._ui.console.Register.RegisterUI;

import java.util.*;

public class MenuUI {

    private static final List<MenuOption> ADMIN_ONLY_OPTIONS = List.of(
            new MenuOption("Visualize a product structure", new ProductStructureUI())
    );
    private static final List<MenuOption> PRODUCTION_MANAGER_OPTIONS = List.of(
            new MenuOption("Register", new RegisterUI()),
            new MenuOption("List", new ListUI()),
            new MenuOption("Schedule Management", new ScheduleUI()),
            new MenuOption("Export orders to CSV file", new OrdersToFileUI()),
            new MenuOption("Simulate order batch production", new SimulateBatchProductionUI())
    );

    public static List<MenuOption> options = new ArrayList<MenuOption>();

    public static void showMenu(){
        options.clear();
        getOptions();
        int selectedOption = InputReader.showListReturnIndex(options, "MENU");
        options.get(selectedOption-1).run();
    }

    private static void getOptions() {
        List<UserRoles> loggedUserRoles = AppSession.getInstance().getUser().getRoles();
        boolean isAdmin = loggedUserRoles.contains(UserRoles.ADMINISTRATOR);
        if (loggedUserRoles.contains(UserRoles.PRODUCTION_MANAGER) || isAdmin){
            options.addAll(PRODUCTION_MANAGER_OPTIONS);
        }
        if (loggedUserRoles.contains(UserRoles.PLANT_FLOOR_MANAGER) || isAdmin){
            //not implemented
        }
        if(isAdmin){
            options.addAll(ADMIN_ONLY_OPTIONS);
        }
        options.add(new MenuOption("Logout", new AuthenticationUI()));
    }


}
