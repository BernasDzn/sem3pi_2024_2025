package org.g102.tools.file;

import org.g102.tools.ConsoleWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FileReader {

    static Scanner inputReader;

    public static String[][] readCSV(String fileName, boolean hasHeader, String delimiter) {
        try {
            File file = new File(fileName);
            if (!file.exists()) {
                ConsoleWriter.displayError("File not found: " + fileName);
                return new String[0][0];
            }
            inputReader = new Scanner(file);
            String[][] csvValues = new String[getFileLength(file)][getMaxFileColumns(file, delimiter)];
            int row = 0;
            if (hasHeader) {
                inputReader.nextLine();
            }
            while (inputReader.hasNextLine()) {
                String[] line = inputReader.nextLine().split(delimiter);
                for (int i = 0; i < line.length; i++) {
                    csvValues[row][i] = line[i].trim();
                }
                row++;
            }
            return csvValues;
        }catch (FileNotFoundException e){
            ConsoleWriter.displayError("Error reading file: " + fileName);
            return new String[0][0];
        }
    }

    public static boolean fileExists(String fileName) {
        File file = new File(fileName);
        return file.exists();
    }

    private static int getFileLength(File file){
        int length = 0;
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                scanner.nextLine();
                length++;
            }
        } catch (Exception e) {
            System.out.println("Error reading file: " + file.getName());
        }
        return length - 1;
    }

    private static int getMaxFileColumns(File file, String delimiter){
        int maxColumns = 0;
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String[] line = scanner.nextLine().split(delimiter);
                if (line.length > maxColumns) {
                    maxColumns = line.length;
                }
            }
        } catch (Exception e) {
            System.out.println("Error reading file: " + file.getName());
        }
        return maxColumns;
    }

}
