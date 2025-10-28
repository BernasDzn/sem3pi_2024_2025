package org.g102._ui.console;

import org.g102.tools.ConsoleWriter;
import org.g102.tools.InputReader;
import org.g102._ui._Session.AppSession;
import org.g102._ui.console._Menu.MenuUI;

import java.security.NoSuchAlgorithmException;
import java.util.List;

public class AuthenticationUI implements Runnable{

    public AuthenticationUI() {

    }

    public void run(){
        AppSession.getInstance().logout();
        List<String> options = List.of("Login", "Exit");
        int selectedOption = InputReader.showListReturnIndex(options, "Sign In");
        switch (selectedOption){
            case 1:
                try {
                    atemptLogin("", "", 0);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                System.out.println("Quitting...");
                break;
        }
    }

    public void atemptLogin(String email, String password, int attempts) throws NoSuchAlgorithmException {
        if(attempts == 3){
            ConsoleWriter.displayError("You have reached the maximum number of attempts. Please try again later.");
            return;
        }else if(attempts != 0){
            ConsoleWriter.displayHeader("Login (attempts: " + attempts + " / 3)");
        }else {
            ConsoleWriter.displayHeader("Login");
        }
        email = InputReader.read("Email");
        password = InputReader.readPassword("Password");
        if(AppSession.getInstance().login(email, password)){
            MenuUI.showMenu();
        } else {
            atemptLogin(email, password, attempts + 1);
        }
    }

}
