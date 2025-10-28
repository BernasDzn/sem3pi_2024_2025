package org.g102.tools;

import org.g102._ui._Session.AppSession;

import java.util.List;

public class ConsoleWriter {

    public static void displayError(String message) {
        System.out.printf(ANSIColors.paint(ANSIColors.paint("<Error> ", ANSIColors.ANSI_RED), ANSIColors.ANSI_BOLD) + message + "\n");
    }

    public static void displayLog(String message){
        if(AppSession.getInstance().getProperties().getProperty(AppSession.LOGGING_ENABLED).equals("true")){
            System.out.printf(ANSIColors.paint(ANSIColors.paint("<Log> ", ANSIColors.ANSI_YELLOW), ANSIColors.ANSI_BOLD) + message + "\n");
        }
    }

    public static void displayHeader(String message) {
        System.out.printf(ANSIColors.paint(ANSIColors.paint("\n======== " + message + " ========\n", ANSIColors.ANSI_BOLD), ANSIColors.ANSI_BLUE));
    }

    public static void displayList(List<?> list) {
        for (int i = 0; i < list.size(); i++) {
            System.out.println((i + 1) + " - " + list.get(i));
        }
    }

    public static void displayTitle(String message) {
        System.out.print(ANSIColors.paint(ANSIColors.paint(message+": ", ANSIColors.ANSI_BOLD), ANSIColors.ANSI_BLUE));
    }

    public static void displayQuetion(String message) {
        System.out.print(ANSIColors.paint(ANSIColors.paint(message+" ", ANSIColors.ANSI_BOLD), ANSIColors.ANSI_BOLD));
    }

    public static void displayCommand(String message) {
        System.out.println(ANSIColors.paint(ANSIColors.paint("-"+message+"-", ANSIColors.ANSI_BOLD), ANSIColors.ANSI_BLACK_BACKGROUND));
    }

    public static void displaySuccess(String s) {
        System.out.println(ANSIColors.paint(ANSIColors.paint("<Success> ", ANSIColors.ANSI_GREEN), ANSIColors.ANSI_BOLD) + s);
    }

    public static void displayFailure(String message) {
        System.out.println(ANSIColors.paint(ANSIColors.paint("<Failure> ", ANSIColors.ANSI_RED), ANSIColors.ANSI_BOLD) + message);
    }

}
