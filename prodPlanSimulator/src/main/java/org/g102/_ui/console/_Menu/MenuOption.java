package org.g102._ui.console._Menu;

public class MenuOption implements Runnable{

    private String optionName;
    private Runnable ui;

    public MenuOption(String option, Runnable ui) {
        this.optionName = option;
        this.ui = ui;
    }

    public String getName() {
        return optionName;
    }

    @Override
    public String toString() {
        return optionName;
    }

    @Override
    public void run() {
        ui.run();
    }
}
