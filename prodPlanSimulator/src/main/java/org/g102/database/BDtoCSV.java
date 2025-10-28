package org.g102.database;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class BDtoCSV {

    String systemPath = System.getProperty("user.dir");

    public static String ITEMS_CSV_PATH = System.getProperty("user.dir") + "/src/main/resources/boo_csvs/items.csv";
    public static String OPERATIONS_CSV_PATH = System.getProperty("user.dir") + "/src/main/resources/boo_csvs/operations.csv";
    public static String BOO_CSV_PATH = System.getProperty("user.dir") + "/src/main/resources/boo_csvs/boo.csv";

    public static File itemsFile = new File(ITEMS_CSV_PATH);
    public static File operationsFile = new File(OPERATIONS_CSV_PATH);
    public static File booFile = new File(BOO_CSV_PATH);

    public static void generateCSVS(String product_id) throws Exception {

        String systemPath = System.getProperty("user.dir");
        if (Arrays.stream(systemPath.split("/")).toList().stream().noneMatch(x -> x.equals("prodPlanSimulator"))) {
            ITEMS_CSV_PATH = systemPath + "/prodPlanSimulator/src/main/resources/boo_csvs/items.csv";
            itemsFile = new File(ITEMS_CSV_PATH);
            OPERATIONS_CSV_PATH = systemPath + "/prodPlanSimulator/src/main/resources/boo_csvs/operations.csv";
            operationsFile = new File(OPERATIONS_CSV_PATH);
            BOO_CSV_PATH = systemPath + "/prodPlanSimulator/src/main/resources/boo_csvs/boo.csv";
            booFile = new File(BOO_CSV_PATH);
        }

        FileWriter itemsWriter = new FileWriter(itemsFile);
        FileWriter operationsWriter = new FileWriter(operationsFile);
        FileWriter booWriter = new FileWriter(booFile);
        itemsWriter.write("id_item;item_name\n");
        operationsWriter.write("op_id;op_name\n");
        booWriter.write("op_id;item_id;item_qtd;(;op1;op_qtd1;op2;op_qtd2;opN;op_qtdN;);(;item_id1;item_qtd1;item_id1;item_qtd1;item_id1;item_qtd1;)\n");

        LinkedHashMap<String, String> allItems = new LinkedHashMap<>();
        LinkedHashMap<Integer, String> allOperations = new LinkedHashMap<>();
        LinkedHashMap<Integer, String> operation = new LinkedHashMap<>();
        LinkedHashMap<Integer, String> usedItems = new LinkedHashMap<>();
        LinkedHashMap<Integer, String> usedOperations = new LinkedHashMap<>();

        writeLine(product_id, usedItems, usedOperations, itemsWriter, operationsWriter, booWriter);

        itemsWriter.close();
        operationsWriter.close();
        booWriter.close();
    }

    public static void writeLine(String product_id, LinkedHashMap<Integer, String> usedItems, LinkedHashMap<Integer, String> usedOperations, FileWriter itemsWriter, FileWriter operationsWriter, FileWriter booWriter) throws SQLException, IOException {

        ResultSet operation_productRS = JDBC.getConn().createStatement().executeQuery("SELECT PO.operation_id, O.description, PO.output_product_id, PO.quantity, P.product_name FROM Product_Operation PO JOIN Product P ON PO.output_product_id = P.product_id JOIN Operation O ON O.operation_id = PO.operation_id WHERE PO.output_product_id = '"+product_id+"'");
        while (operation_productRS.next()) {
            int op_id = operation_productRS.getInt("operation_id");
            String output_prod_id = operation_productRS.getString("output_product_id");
            String op_qtd = String.valueOf(operation_productRS.getFloat("quantity"));
            String prod_name = operation_productRS.getString("product_name");
            String op_name = operation_productRS.getString("description");

            String operation_prod = op_id + ";" + output_prod_id + ";" + op_qtd + ";";
            itemsWriter.write(output_prod_id + ";" + prod_name + "\n");
            operationsWriter.write(op_id + ";" + op_name + "\n");

            ResultSet input_materials = JDBC.getConn().createStatement().executeQuery("SELECT pm.material_id, pm.quantity, m.material_name FROM Product_Materials pm JOIN Material m ON m.material_id = pm.material_id WHERE pm.product_id = '"+product_id+"'");
            usedItems.put(op_id, ";(;");
            int count = 0;
            while (input_materials.next()) {
                usedItems.put(op_id,usedItems.get(op_id).concat(input_materials.getString("material_id") + ";" + input_materials.getFloat("quantity") + ";"));
                itemsWriter.write(input_materials.getString("material_id") + ";" + input_materials.getString("material_name") + "\n");
                count++;
            }
            for (int i = count; i < 3; i++) {
                usedItems.put(op_id,usedItems.get(op_id).concat(";;"));
            }
            ResultSet input_subProds = JDBC.getConn().createStatement().executeQuery("SELECT * FROM Product_SubProducts WHERE product_id = '"+product_id+"'");
            List<String> subProd_ids = new ArrayList<>();
            usedOperations.put(op_id, "(;");
            count = 0;
            while(input_subProds.next()) {
                String subProd_id = input_subProds.getString("subproduct_id");
                ResultSet subProd_op = JDBC.getConn().createStatement().executeQuery("SELECT * FROM Product_Operation WHERE output_product_id = '"+subProd_id+"'");
                while(subProd_op.next()) {
                    subProd_ids.add(subProd_id);
                    usedOperations.put(op_id,usedOperations.get(op_id).concat(subProd_op.getString("operation_id") + ";" + subProd_op.getFloat("quantity") + ";"));
                    count++;
                }
                subProd_op.close();
            }
            for (int i = count; i < 3; i++) {
                usedOperations.put(op_id,usedOperations.get(op_id).concat(";;"));
            }
            usedOperations.put(op_id,usedOperations.get(op_id).concat(")"));
            booWriter.write(operation_prod + usedOperations.get(op_id) + usedItems.get(op_id) + ")\n");
            for (String subProdids : subProd_ids) {
                writeLine(subProdids, usedItems, usedOperations, itemsWriter, operationsWriter, booWriter);
            }
            input_materials.close();
            input_subProds.close();
        }
        operation_productRS.close();

    }

}
