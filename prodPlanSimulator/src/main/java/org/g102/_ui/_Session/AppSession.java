package org.g102._ui._Session;

import org.g102.tools.ConsoleWriter;
import org.g102._ui._AuthDomain.UserRepository;
import org.g102._ui._AuthDomain.User;
import org.g102._ui._AuthDomain.UserRoles;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

public class AppSession {

    private static final String CONFIGURATION_FILENAME = "prodPlanSimulator/src/main/resources/config.properties";
    public static final String LOGGING_ENABLED = "logging_enabled";
    private static AppSession instance;
    private User loggedUser;

    public static AppSession getInstance(){
        if(instance == null){
            instance = new AppSession();
        }
        return instance;
    }

    public User getUser(){
        return loggedUser;
    }

    public boolean login(String email, String password) throws NoSuchAlgorithmException {
        User attemptUser = UserRepository.getInstance().isValidUser(email, password);
        if(attemptUser != null){
            loggedUser = attemptUser;
            return true;
        }else{
            ConsoleWriter.displayError("Invalid credentials");
            return false;
        }
    }

    public void logout(){
        loggedUser = null;
    }

    public boolean isLogged(){
        return loggedUser != null;
    }

    public boolean isAdmin(){
        return loggedUser.getRoles().contains(UserRoles.ADMINISTRATOR);
    }

    public Properties getProperties() {
        Properties props = new Properties();

        props.setProperty(LOGGING_ENABLED, "true");

        try {
            InputStream in = new FileInputStream(CONFIGURATION_FILENAME);
            props.load(in);
            in.close();
        } catch (IOException ex) {
            System.out.println("Error reading configuration file: " + CONFIGURATION_FILENAME);
        }
        return props;
    }
}
