package org.g102._controllers;

import org.g102._ui.Bootstrap;
import org.g102.database.JDBC;
import org.g102.domain.*;
import org.g102.tools.ANSIColors;
import org.g102.tools.ConsoleWriter;
import org.g102.tools.file.FileReader;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STIconSetType;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class USLP06_controller {

    private static String get_orders_query = "SELECT CustomerOrder.order_id, OrderProductList.product_id, OrderProductList.quantity " +
            "FROM CustomerOrder " +
            "JOIN OrderProductList " +
            "ON CustomerOrder.order_id = OrderProductList.order_id";
    private static String orders_csv_path = System.getProperty("user.dir") + "/prodPlanSimulator/src/main/resources/orders.csv";

    static List<Machine> machines = new ArrayList<>();
    static List<Item> items = new ArrayList<>();
    static HashMap<String, Integer> operationTimes = new HashMap<>();
    static HashMap<String, String > operationNames = new HashMap<>();
    static HashMap<Machine, Integer> machineOperations = new HashMap<>();

    public boolean simulateBatchProduction(File ordersFile) {
        orders_csv_path = ordersFile.getAbsolutePath();
        String[][] orders_csv_data;
        try {
            orders_csv_data = FileReader.readCSV(orders_csv_path,true, ";");
            for (String[] order : orders_csv_data){
                fillData(order[1], machines, items);
            }
        }catch (Exception e){
            ConsoleWriter.displayError("Selected file is invalid.");
            return false;
        }
        Simulator simulator = new Simulator(true, machines, items);
        try {
            simulator.simulateProduction();
        }catch (Exception e){
            ConsoleWriter.displayError("Error while simulating production due to: " + e.getMessage());
            return false;
        }
        Map<String, Time> newTimes = simulator.getOperationTimes();
        System.out.println("Operation times updated:");
        for (String opid : newTimes.keySet()){
            System.out.println(ANSIColors.paint(opid + " - " + operationNames.get(opid) + ": ", ANSIColors.ANSI_BOLD) + ANSIColors.paint(String.valueOf(operationTimes.get(opid)) + " minutes ", ANSIColors.ANSI_RED) + " -> " + ANSIColors.paint(String.valueOf(newTimes.get(opid).getAllToMinutes()) + " minutes ", ANSIColors.ANSI_GREEN));
            saveNewTimesInDB(opid, newTimes.get(opid));
        }
        exportSimulatorResultsToCSVs();
        return true;
    }

    private void saveNewTimesInDB(String opid, Time time) {
        Connection conn;
        Statement stmt;
        try{
            conn = JDBC.getConn();
            stmt = conn.createStatement();
            String update_query = "UPDATE Operation " +
                    "SET expected_execution_time = " + time.getAllToMinutes() + " " +
                    "WHERE operation_id = " + opid;
            stmt.executeUpdate(update_query);
            stmt.close();
        }catch (Exception e){
            System.out.println("Error while updating operation times in database due to: " + e.getMessage());
        }
    }

    private static void fillData(String product_id, List<Machine> machines, List<Item> items){
        String get_operation_query = "SELECT operation_id, operation_name, expected_execution_time " +
                "FROM Operation " +
                "JOIN OperationType " +
                "\tON Operation.op_type_id = OperationType.op_type_id " +
                "WHERE out_product_id = '" + product_id + "'";
        Operation operation = getOperationFromDB(get_operation_query);
        operationNames.put(String.valueOf(operation.getOperation_id()), operation.getName());
        operationTimes.put(String.valueOf(operation.getOperation_id()), operation.getTime().getAllToMinutes());
        Item item = new Item(product_id.hashCode(), ItemPriority.NORMAL, new LinkedList<>(List.of(String.valueOf(operation.getOperation_id()))));
        items.add(item);
        String get_machine_query = "SELECT Workstation.workstation_id, OperationType.operation_name, MIN(Operation.expected_execution_time) " +
                "FROM Workstation " +
                "INNER JOIN WorkstationType_Operation ON " +
                "    Workstation.workstation_type_id = WorkstationType_Operation.workstation_type_id " +
                "INNER JOIN OperationType ON " +
                "    WorkstationType_Operation.op_type_id = OperationType.op_type_id " +
                "INNER JOIN Operation ON " +
                "    OperationType.op_type_id = Operation.op_type_id " +
                "WHERE Operation.operation_id = " + operation.getOperation_id() + " " +
                "GROUP BY Workstation.workstation_id, OperationType.operation_name";
        Machine machine = getMachineFromDB(get_machine_query, operation.getOperation_id());
        machineOperations.put(machine, operation.getOperation_id());
        machines.add(machine);
        String intermediary_prods_query = "SELECT input_id " +
                "FROM Operation_InputList " +
                "WHERE operation_id = " + operation.getOperation_id() + " " +
                "AND input_id NOT IN (SELECT material_id FROM Material)";
        String[] intermediary_prods = getIntermediaryProducts(intermediary_prods_query);
        for (String intermediary_prod : intermediary_prods){
            fillData(intermediary_prod, machines, items);
        }
    }

    private static void exportSimulatorResultsToCSVs(){
        System.out.println("Select the folder to save simulation result data:");
        String path_folder = requestFileLocation();
        path_folder = path_folder + "/simulation_data";
        new File(path_folder).mkdirs();

        int min_temp, max_temp, min_hum, max_hum;
        String temp_unit = "Celsius";
        String hum_unit = "Percentage";
        int buffer_size, median_window_size;

        try {
            String machine_csv_header = "id;name;min_temperature;max_temperature;temperature_unit;min_humidity;max_humidity;humidity_unit;buffer_lenght;median_window";
            String machines_csvs_path = path_folder + "/machines.csv";
            FileWriter machineWriter = new FileWriter(machines_csvs_path);
            machineWriter.write(machine_csv_header + "\n");
            List<Machine> machinesPrinted = new ArrayList<>();
            for (Machine machine : machines){
                boolean alreadyPrinted = false;
                for (Machine machinePrinted : machinesPrinted){
                    if (machine.getId().equals(machinePrinted.getId())){
                        alreadyPrinted = true;
                    }
                }
                if (alreadyPrinted){
                    continue;
                }
                machinesPrinted.add(machine);
                min_temp = ThreadLocalRandom.current().nextInt(-5, 11);
                max_temp = ThreadLocalRandom.current().nextInt(min_temp, 101);
                min_hum = ThreadLocalRandom.current().nextInt(0, 31);
                max_hum = ThreadLocalRandom.current().nextInt(min_hum, 101);
                buffer_size = ThreadLocalRandom.current().nextInt(3, 11);
                median_window_size = ThreadLocalRandom.current().nextInt(2, buffer_size);
                String machine_csv_data = machine.getId() + ";Machine " + machine.getId() + ";" + min_temp + ";" + max_temp + ";" + temp_unit + ";" + min_hum + ";" + max_hum + ";" + hum_unit + ";" + buffer_size + ";" + median_window_size;
                try {
                    machineWriter.write(machine_csv_data + "\n");
                } catch (Exception e) {
                    System.out.println("Error while writing machine to CSV due to: " + e.getMessage());
                }
            }
            machineWriter.close();
        }catch (Exception e){
            System.out.println("Error while writing machines to CSV due to: " + e.getMessage());
        }

        try {
            String mach_ops_csv_header = "machine_id;op_id;op_name;";
            String mach_ops_csv_path = path_folder + "/machine_operations.csv";
            FileWriter machOpsWriter = new FileWriter(mach_ops_csv_path);
            machOpsWriter.write(mach_ops_csv_header + "\n");
            List<Machine> machinesPrinted = new ArrayList<>();
            int machine_id_counter = 1;
            for (Machine machine : machines){
                boolean alreadyPrinted = false;
                for (Machine machinePrinted : machinesPrinted){
                    if (machine.getId().equals(machinePrinted.getId())){
                        alreadyPrinted = true;
                    }
                }
                if (alreadyPrinted){
                    continue;
                }
                machinesPrinted.add(machine);
                String mach_ops_csv_data = machine.getId() + ";" + machine_id_counter + ";" + operationNames.get(String.valueOf(machineOperations.get(machine))) + ";";
                machine_id_counter++;
                try {
                    if(machine_id_counter <= 31){
                        machOpsWriter.write(mach_ops_csv_data + "\n");
                    }
                } catch (Exception e) {
                    System.out.println("Error while writing machine operations to CSV due to: " + e.getMessage());
                }
            }
            machOpsWriter.close();
        }catch (Exception e){
            System.out.println("Error while writing machine operations to CSV due to: " + e.getMessage());
        }

    }

    private static String[] getIntermediaryProducts(String intermediaryProdsQuery) {
        Connection conn;
        Statement stmt;
        ResultSet intermediaryProds;
        List<String> intermediaryProducts = new ArrayList<>();
        try{
            conn = JDBC.getConn();
            stmt = conn.createStatement();
            intermediaryProds = stmt.executeQuery(intermediaryProdsQuery);
            while (intermediaryProds.next()){
                intermediaryProducts.add(intermediaryProds.getString("input_id"));
            }
            intermediaryProds.close();
            stmt.close();
        }catch (Exception e){
            System.out.println("Error while getting intermediary products from database due to: " + e.getMessage() + "\n Query: " + intermediaryProdsQuery);
        }
        return intermediaryProducts.toArray(new String[0]);
    }

    private static Machine getMachineFromDB(String getMachineQuery, int operation_id) {
        Connection conn;
        Statement stmt;
        ResultSet machine;
        try{
            conn = JDBC.getConn();
            stmt = conn.createStatement();
            machine = stmt.executeQuery(getMachineQuery);
            while (machine.next()){
                String machine_id = machine.getString("workstation_id");
                String machine_name = String.valueOf(operation_id);
                int exectime = Integer.parseInt(machine.getString("MIN(Operation.expected_execution_time)"));
                return new Machine(machine_id, machine_name, exectime);
            }
            machine.close();
            stmt.close();
        }catch (Exception e){
            System.out.println("Error while getting machine from database due to: " + e.getMessage() + "\n Query: " + getMachineQuery);
        }
        return null;
    }

    private static Operation getOperationFromDB(String query){
        Connection conn;
        Statement stmt;
        ResultSet operation;;
        try{
            conn = JDBC.getConn();
            stmt = conn.createStatement();
            operation = stmt.executeQuery(query);
            while (operation.next()){
                int opid = Integer.parseInt(operation.getString("operation_id"));
                String opname = operation.getString("operation_name");
                int exectime = Integer.parseInt(operation.getString("expected_execution_time"));
                return new Operation(opid, opname, exectime * 60);
            }
            operation.close();
            stmt.close();
        }catch (Exception e){
            System.out.println("Error while getting operation from database due to: " + e.getMessage() + "\n Query: " + query);
        }
        return null;
    }

    public List<String> getAllOrdersIds(){
        List<String> orderIds = new ArrayList<>();
        Connection conn;
        Statement stmt;
        ResultSet orders;
        try{
            conn = JDBC.getConn();
            stmt = conn.createStatement();
            orders = stmt.executeQuery(get_orders_query);
            while (orders.next()){
                String order_id = orders.getString("order_id");
                if (!orderIds.contains(order_id)){
                    orderIds.add(order_id);
                }
            }
            orders.close();
            stmt.close();
        }catch (Exception e){
            System.out.println("Error while getting orders from database due to: " + e.getMessage());
        }
        return orderIds;
    }

    public boolean exportOrdersToFile(List<String> selectedOrders) {
        String fileLocation = requestFileLocation();
        if(!fileLocation.isEmpty()){
            try {
                FileWriter ordersWriter = new FileWriter(fileLocation + "/orders.csv");
                ordersWriter.write("order_id;product_id;priority;quantity\n");
                for (String order_id : selectedOrders){
                    Connection conn;
                    Statement stmt;
                    ResultSet orders;
                    try{
                        conn = JDBC.getConn();
                        stmt = conn.createStatement();
                        orders = stmt.executeQuery(get_orders_query);
                        while (orders.next()){
                            if (orders.getString("order_id").equals(order_id)){
                                String product_id = orders.getString("product_id");
                                String quantity = orders.getString("quantity");
                                ordersWriter.write(order_id + ";" + product_id + ";NORMAL;" + quantity + "\n");
                            }
                        }
                        orders.close();
                        stmt.close();
                    }catch (Exception e){
                        System.out.println("Error while getting orders from database due to: " + e.getMessage());
                    }
                }
                ordersWriter.close();
                return true;
            } catch (Exception e) {
                System.out.println("Error while writing orders to CSV due to: " + e.getMessage());
                return false;
            }
        }
        return false;
    }

    public static String requestFileLocation(){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select a file");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int userSelection = fileChooser.showSaveDialog(null);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile().getAbsolutePath();
        }
        return null;
    }

    public File requestOrdersFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select a file");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int userSelection = fileChooser.showSaveDialog(null);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        }
        return null;
    }

}
