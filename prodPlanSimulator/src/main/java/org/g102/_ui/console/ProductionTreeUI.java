package org.g102._ui.console;

import org.g102._controllers.ProductionTreeController;
import org.g102.domain.GenericItem;
import org.g102.tools.file.FileReader;
import org.g102.tree.HeapPriorityQueue;
import org.g102.database.JDBC;
import org.g102.tools.ConsoleWriter;
import org.g102.tools.InputReader;
import org.g102._ui.console._Menu.MenuUI;

import java.io.Console;
import java.sql.ResultSet;
import java.util.*;

public class ProductionTreeUI implements Runnable {

    public ProductionTreeController productionTreeController;

    @Override
    public void run() {
        ConsoleWriter.displayHeader("Product Production Tree Options");
        String filepath = InputReader.read("Order file path (empty to fetch from DB or 'exit' to go back)");
        if(filepath == null || filepath.equals("")){
            ResultSet rs = null;
            try{
                rs = JDBC.getConn().createStatement().executeQuery("SELECT * FROM FinalProduct");
            }catch (Exception e){
                ConsoleWriter.displayError("cannot fetch data from database");
                new ProductionTreeUI().run();
            }

            HashMap<String, String> products = new HashMap<>();
            HashSet<String> productsList = new HashSet<>(products.values());
            try{
                while (rs.next()) {
                    products.put(rs.getString("product_name"), rs.getString("final_product_id"));
                    productsList.add(rs.getString("product_name"));
                }
                rs.close();
            }catch (Exception e){
                ConsoleWriter.displayError("cannot fetch data from database");
                new ProductionTreeUI().run();
            }

            int selectedOption = InputReader.showListReturnIndex(productsList.stream().toList(), "Select Product");
            String selectedProduct = products.get(productsList.toArray()[selectedOption-1]);
            productionTreeController = new ProductionTreeController(selectedProduct);
            showMenu();
        }
        else if(FileReader.fileExists(filepath)){
            String[][] data = FileReader.readCSV(filepath, true, ",");
            List<String> orders = new ArrayList<>();
            for(int i = 0; i < data.length; i++){
                if(!orders.contains(data[i][0])){
                    orders.add(data[i][0]);
                }
            }
            int selectedOption = InputReader.showListReturnIndex(orders, "Select Order");
            String selectedOrder = orders.get(selectedOption-1);
            HashMap<String, Integer> products = new HashMap<>();
            for(int i = 0; i < data.length; i++){
                if(data[i][0].equals(selectedOrder)){
                    if(!products.containsKey(data[i][1])){
                        products.put(data[i][1], i);
                    }
                    else
                        products.put(data[i][1], products.get(data[i][1])+1);
                }
            }
            ConsoleWriter.displayHeader("Products in this order");
            ConsoleWriter.displayList(products.keySet().stream().toList());
            for(String s : products.keySet()){
                try{
                    productionTreeController = new ProductionTreeController(s);
                    productionTreeController.runSimulator();
                }catch (Exception e){
                    ConsoleWriter.displayError("Error in processing product: " + s);
                }
            }
        }
        else if (filepath.equals("exit")){
            MenuUI.showMenu();
        }
        else {
            ConsoleWriter.displayError("File not found: " + filepath);
            run();
        }
    }

    public void showMenu(){
        int selectedOption = InputReader.showListReturnIndex(List.of(
                "Show Tree"
                , "Search for Operation or Material in production tree"
                , "List quantity of materials used in a product"
                , "Show Priority Checks"
                , "Change quantity of Material"
                , "Get total production cost"
                , "Display critical path operations in order of their importance based on depth"
                , "Run simulator with AVT and BST ?"
                , "Go Back"), null);
        switch (selectedOption) {
            case 1:
                System.out.println(productionTreeController.showTree());
                showMenu();
                break;
            case 2:
                productionTreeController.searchOperationOrMaterial();
                showMenu();
                break;
            case 3:
                productionTreeController.listMaterialsUsedInProduct();
                showMenu();
                break;
            case 4:
                showPriorityChecks();
                showMenu();
                break;
            case 5:
                changeQuantityOfMaterial();
                showMenu();
                break;
            case 6:
                productionTreeController.getTotalProductionCost();
                showMenu();
                break;
            case 7:
                System.out.println(productionTreeController.displayCriticalPathOperations());
                showMenu();
                break;
            case 8:
                productionTreeController.runSimulator();
                showMenu();
                break;
            case 9:
                MenuUI.showMenu();
                break;
        }
    }

    public void changeQuantityOfMaterial(){
        ConsoleWriter.displayHeader("Select a Material:");
        int n = InputReader.showListReturnIndex(productionTreeController.getMaterials(), "Materials")-1;
        ConsoleWriter.displayHeader("Change quantity of [" + productionTreeController.getMaterials().get(n).getName() + "]");
        Float quantity = InputReader.getFloat("Quantity");
        productionTreeController.changeQuantityOfMaterial(quantity, productionTreeController.getMaterials().get(n));
    }

    public void showPriorityChecks(){
        HeapPriorityQueue<Integer, GenericItem> queue = productionTreeController.getPriorityQueue();
        Scanner sc = new Scanner(System.in);
        List<GenericItem> trueList = new ArrayList<>();
        List<GenericItem> falseList = new ArrayList<>();

        if(queue.isEmpty()){
            ConsoleWriter.displayError("No items to check! The selected item might not have a production tree.");
            return;
        }

        while (!queue.isEmpty()) {
            ConsoleWriter.displayHeader("Quality Check");
            System.out.println(queue);
            ConsoleWriter.displayTitle("Item with highest priority");
            System.out.println(queue.min().getValue());

            ConsoleWriter.displayQuetion("Did this item pass the quality check? (Y/N):");
            String input = sc.nextLine().trim().toLowerCase();

            if (input.equals("y")) {
                trueList.add(queue.removeMin().getValue());
            } else if (input.equals("n")) {
                falseList.add(queue.removeMin().getValue());
            } else {
                ConsoleWriter.displayError("Invalid Answer!");
            }
        }

        ConsoleWriter.displayHeader("Quality Check Results");
        if (!trueList.isEmpty()) {
            ConsoleWriter.displayTitle("Items that passed the quality check");
            System.out.print("\n");
            for (GenericItem item : trueList) {
                System.out.println(item);
            }
        } else {
            ConsoleWriter.displayFailure("No items passed the quality check");
        }

        if (!falseList.isEmpty()) {
            ConsoleWriter.displayTitle("Items that did not pass the quality check");
            System.out.print("\n");
            for (GenericItem item : falseList) {
                System.out.println(item);
            }
        } else {
            ConsoleWriter.displaySuccess("No items failed the quality check");
        }

        int selectedOption = InputReader.showListReturnIndex(List.of(
                "Check Again","Go Back"), "Quality Check Priority Queue");

        if (selectedOption == 1) {
            showPriorityChecks();
        }
    }
}
