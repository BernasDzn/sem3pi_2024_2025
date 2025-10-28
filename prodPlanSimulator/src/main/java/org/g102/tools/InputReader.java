package org.g102.tools;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.Console;

public class InputReader {

    static Scanner inputReader = new Scanner(System.in);

    /**
     * Reads a user input from the console and returns it as a String
     * @param message Message to be displayed
     * @return User input as a String
     */
    public static String read(String message) {
        System.out.printf("\033[32;1;1m>\033[0m " + message + ": ");
        return inputReader.nextLine();
    }

    public static boolean readBoolean(String message) {
        String input = read(message);
        if (input.equalsIgnoreCase("y") || input.equalsIgnoreCase("Y")) {
            return true;
        } else if (input.equalsIgnoreCase("n") || input.equalsIgnoreCase("N")) {
            return false;
        } else {
            ConsoleWriter.displayError("Invalid input, please enter 'y' or 'n'.");
            return readBoolean(message);
        }
    }

    public static int showListReturnIndex(List<?> list, String header) {
        if(header != null){
            System.out.printf("\n\033[34;1;1m======== " + header + " ======== \033[0m\n");
        }
        ConsoleWriter.displayList(list);
        int option = 0;
        try {
            option = Integer.parseInt(read("Option"));
        }catch (NumberFormatException e){
            ConsoleWriter.displayError("Please enter a valid option from the list.");
            return showListReturnIndex(list, header);
        }
        if(option <= 0 || option > list.size()){
            ConsoleWriter.displayError("Please enter a valid option from the list.");
            return showListReturnIndex(list, header);
        }
        return option;
    }

    public static <T> int showListReturnIndexWithExit(List<T> list, String header) {
        ArrayList<String> listAsString = new ArrayList<>();
        for (T item : list) {
            listAsString.add(item.toString());
        }
        listAsString.add("Exit");
        int option = showListReturnIndex(listAsString, header);
        if (listAsString.get(option-1).equals("Exit")) {
            return -1;
        }
        return option;
    }

    public static String readDate(String message) {
        String date = read(message);
        if (date.matches("\\d{2}-\\d{2}-\\d{4}")) {
            return date;
        } else {
            ConsoleWriter.displayError("Please enter a valid date in the format 'DD-MM-YYYY'.");
            return readDate(message);
        }
    }

    public static String readPassword(String message) {
        Console console = System.console();
        if (console == null) {
            return read(message);
        } else {
            System.out.printf("\033[32;1;1m>\033[0m " + message + ": ");
            char[] password = console.readPassword();
            return new String(password);
        }
    }

    public static float getFloat(String message) {
        try {
            return Float.parseFloat(read(message));
        } catch (NumberFormatException e) {
            ConsoleWriter.displayError("Please enter a valid number.");
            return getFloat(message);
        }
    }

    public static int readInt(String message) {
        try {
            int num = Integer.parseInt(read(message));
            if (num < 0) {
                ConsoleWriter.displayError("Please enter a positive number.");
                return readInt(message);
            }else {
                return num;
            }
        } catch (NumberFormatException e) {
            ConsoleWriter.displayError("Please enter a valid number.");
            return readInt(message);
        }
    }
}