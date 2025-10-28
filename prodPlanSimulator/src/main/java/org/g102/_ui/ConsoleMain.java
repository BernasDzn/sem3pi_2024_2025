package org.g102._ui;
import org.g102._ui.console.AuthenticationUI;

public class ConsoleMain {

    public static void main(String[] args) {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.run();
        try {
            AuthenticationUI login = new AuthenticationUI();
            login.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
