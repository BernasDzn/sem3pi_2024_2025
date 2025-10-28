package org.g102.tree;

import org.g102.domain.*;
import org.g102.database.BDtoCSV;
import org.g102.tools.ANSIColors;
import org.g102.tools.ConsoleWriter;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.util.*;

public class CSVtoTree {

    private static File itemsCSV;
    private static File operationsCSV;
    private static File BOO_CSV;

    public static NAryTree<ProductionNode> getProductionTree(String productID) {
        NAryTree<ProductionNode> productionTree = new NAryTree<>();

        try{
            BDtoCSV.generateCSVS(productID);
        }catch (Exception e){
            ConsoleWriter.displayError("Error generating CSVs");
            return null;
        }

        String systemPath = System.getProperty("user.dir");
        if (Arrays.stream(systemPath.split("/")).toList().stream().noneMatch(x -> x.equals("prodPlanSimulator"))) {
            String ITEMS_CSV_PATH = systemPath + "/prodPlanSimulator/src/main/resources/boo_csvs/items.csv";
            itemsCSV = new File(ITEMS_CSV_PATH);
            String OPERATIONS_CSV_PATH = systemPath + "/prodPlanSimulator/src/main/resources/boo_csvs/operations.csv";
            operationsCSV = new File(OPERATIONS_CSV_PATH);
            String BOO_CSV_PATH = systemPath + "/prodPlanSimulator/src/main/resources/boo_csvs/boo.csv";
            BOO_CSV = new File(BOO_CSV_PATH);
        }

        HashMap<Integer, Operation> operations;
        try{ operations = readOperations(operationsCSV);}
        catch (Exception e){
            ConsoleWriter.displayError("Error reading operation file.");
            return null;
        }

        HashMap<String, GenericItem> items;
        try {items = readItems(itemsCSV);}
        catch (Exception e){
            ConsoleWriter.displayError("Error reading items file.");
            return null;
        }

        LinkedHashMap<Integer, String> perOP_BOOCSV_line;
        try{
            perOP_BOOCSV_line = readBoo(BOO_CSV);
        }catch (Exception e){
            ConsoleWriter.displayError("Error reading BOO file.");
            return null;
        }

        HashMap<Integer, NAryTree.Node<ProductionNode>> operationNodes = new HashMap<>();
        for(Operation operation : operations.values()){
            operationNodes.put(operation.getOperation_id(), new NAryTree.Node<ProductionNode>(operation, 0));
        }

        if(perOP_BOOCSV_line.firstEntry() != null){
            productionTree.setRoot(operationNodes.get(perOP_BOOCSV_line.firstEntry().getKey()));
            fillTreeWithNodes(productionTree, operationNodes, operations, items, perOP_BOOCSV_line);
        }

        return productionTree;
    }

    /**
     * Fills the tree with the nodes from the csv files
     * @param productionTree the tree to be filled
     * @param operationNodes the nodes of the operations
     * @param operations the operations
     * @param items the items
     * @param perOPBoocsvLine the lines from the BOO csv file
     */
    private static void fillTreeWithNodes(NAryTree<ProductionNode> productionTree, HashMap<Integer, NAryTree.Node<ProductionNode>> operationNodes, HashMap<Integer, Operation> operations, HashMap<String, GenericItem> items, HashMap<Integer, String> perOPBoocsvLine) {
        for(Integer operationID : perOPBoocsvLine.keySet()){
            Operation operation = operations.get(operationID);
            String BOO_line = perOPBoocsvLine.get(operationID);
            String[] itemsAndQuantities = BOO_line.split(";");
            operation.setOutputProduct( items.get(itemsAndQuantities[0]));
            operation.setOutputQuantity(Float.parseFloat(itemsAndQuantities[1]));
            operationNodes.get(operationID).setElement(operation);
            for(int i = 11; i < 16; i+=2) {
                if (itemsAndQuantities[i].equals("")) {
                    break;
                }
                NAryTree.Node<ProductionNode> materialNode = new NAryTree.Node<ProductionNode>(items.get(itemsAndQuantities[i]), Float.parseFloat(itemsAndQuantities[i + 1].replace(",", ".")));
                operationNodes.get(operationID).addChild(materialNode);
            }
            for(int i = 3; i < 8; i+=2){
                if(itemsAndQuantities[i].equals("")){
                    break;
                }
                operationNodes.get(Integer.parseInt(itemsAndQuantities[i])).setWeight(Float.parseFloat(itemsAndQuantities[i+1].replace(",",".")));
                operationNodes.get(operationID).addChild(operationNodes.get(Integer.parseInt(itemsAndQuantities[i])));
            }
        }
    }

    /**
     * Reads the BOO csv file and returns a HashMap with the operationID as the key
     * and the rest of the line as the value
     * @param BOO_CSV the file to be read
     * @return a HashMap with the operationID as the key and the rest of the line as the value
     */
    public static LinkedHashMap<Integer, String> readBoo(File BOO_CSV) {
        LinkedHashMap<Integer, String> operationBOOLine = new LinkedHashMap<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(BOO_CSV));
            String line;
            while ((line = br.readLine()) != null) {
                if(line.equals("op_id;item_id;item_qtd;(;op1;op_qtd1;op2;op_qtd2;opN;op_qtdN;);(;item_id1;item_qtd1;item_id1;item_qtd1;item_id1;item_qtd1;)")){
                    continue;
                }
                String[] values = line.split(";");
                int operationID = Integer.parseInt(values[0]);
                String restAfterOperationID = line.substring(values[0].length() + 1);
                operationBOOLine.put(operationID, restAfterOperationID);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return operationBOOLine;
    }

    /**
     * Reads the operations from the CSV file containing the operations
     * @return a HashMap with the operationID as the key and the Operation object as the value
     */
    private static HashMap<Integer, Operation> readOperations(File operationsCSV) {
        HashMap<Integer, Operation> operations = new HashMap<>();
        try{
            BufferedReader br = new BufferedReader(new FileReader(operationsCSV));
            String line;
            while ((line = br.readLine()) != null) {
                if (line.equals("op_id;op_name")) {
                    continue;
                }
                String[] values = line.split(";");
                int id = Integer.parseInt(values[0]);
                String name = values[1];
                operations.put(id, new Operation(id, name));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return operations;
    }

    /**
     * Reads the items from the CSV file containing the items.
     * An item (GenericItem) is either a product meaning it has a BOM,
     * or a material meaning it is a leaf in the tree.
     * Since we don't know if an item is a product or a material by the csv file,
     * we create all Products and Materials as GenericItems.
     * @return a HashMap with the itemID as the key and the Item object as the value
     */
    private static HashMap<String, GenericItem> readItems(File itemsCSV) {
        HashMap<String, GenericItem> items = new HashMap<>();
        try{
            BufferedReader br = new BufferedReader(new FileReader(itemsCSV));
            String line;
            while ((line = br.readLine()) != null) {
                if (line.equals("id_item;item_name")) {
                    continue;
                }
                String[] values = line.split(";");
                String id = values[0];
                String name = values[1];
                items.put(id, new GenericItem(id, name));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return items;
    }

    /**
     * Asks the user for a file to be read
     * @return the file selected by the user
     */
    public static File askForFile(String header) {
        System.out.printf("\nPlease select the " + header + " file: ");
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select " + header + " file");
        fileChooser.setFileFilter(new FileNameExtensionFilter("CSV Files", "csv"));
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            System.out.printf(ANSIColors.paint(ANSIColors.paint(fileChooser.getSelectedFile().getName(), ANSIColors.ANSI_GREEN), ANSIColors.ANSI_BOLD));
            return fileChooser.getSelectedFile();
        } else {
            System.out.printf(ANSIColors.paint(ANSIColors.paint("No file selected", ANSIColors.ANSI_RED), ANSIColors.ANSI_BOLD));
            return null;
        }
    }

    public static BST<QuantityNode<GenericItem>>  getMaterialQuantityList() {
        BST<QuantityNode<GenericItem>> materialsQuantity = new BST<>();
        HashMap<GenericItem, Float> materials= new HashMap<>();
        try{
            itemsCSV = new File(System.getProperty("user.dir") + "/prodPlanSimulator/src/main/resources/boo_csvs/items.csv");
            BOO_CSV = new File(System.getProperty("user.dir") + "/prodPlanSimulator/src/main/resources/boo_csvs/boo.csv");
        }catch (Exception e){
            ConsoleWriter.displayError("Error reading files");
            return null;
        }

        HashMap<String, GenericItem> items;
        try {items = readItems(itemsCSV);}
        catch (Exception e){
            ConsoleWriter.displayError("Error reading items file.");
            return null;
        }

        LinkedHashMap<Integer, String> perOPBoocsvLine;
        try{
            perOPBoocsvLine = readBoo(BOO_CSV);
        }catch (Exception e){
            ConsoleWriter.displayError("Error reading BOO file.");
            return null;
        }

        for (Integer operationID : perOPBoocsvLine.keySet()) {
            String BOO_line = perOPBoocsvLine.get(operationID);
            String[] itemsAndQuantities = BOO_line.split(";");
            for (int i = 11; i < 16; i += 2) {
                if (itemsAndQuantities[i].isEmpty()) {
                    break;
                }
                String materialId = itemsAndQuantities[i];
                float materialQuantity = Float.parseFloat(itemsAndQuantities[i + 1].replace(",", "."));
                GenericItem materialItem = items.get(materialId);
                if (materialItem != null) {
                    if(materials.containsKey(materialItem)){
                        materials.put(materialItem, materials.get(materialItem) + materialQuantity);
                    }else{
                        materials.put(materialItem, materialQuantity);
                    }
                }
            }
        }

        for (Map.Entry<GenericItem, Float> entry : materials.entrySet()) {
            ArrayList<GenericItem> materialQuantitiesList = new ArrayList<>();
            GenericItem material= entry.getKey();
            Float weight = entry.getValue();

            for(Map.Entry<GenericItem, Float> entry2 : materials.entrySet()){
                if(entry2.getValue().equals(weight)){
                    materialQuantitiesList.add(entry2.getKey());
                }
            }

            materialsQuantity.insert(new QuantityNode<>(weight, materialQuantitiesList));
        }
        return materialsQuantity;
    }
}



