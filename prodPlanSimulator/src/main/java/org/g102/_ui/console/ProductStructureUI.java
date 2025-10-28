package org.g102._ui.console;

import org.g102._controllers.ProductStructureController;
import org.g102.database.JDBC;
import org.g102.tools.ConsoleWriter;
import org.g102.tools.InputReader;
import org.g102._ui.console._Menu.MenuUI;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class ProductStructureUI implements Runnable{

    private ProductStructureController productStructureController;

    public ProductStructureUI(){
        productStructureController = new ProductStructureController();
    }

    @Override
    public void run() {
        String sql = "SELECT final_product_id, product_name FROM FinalProduct";
        ResultSet resultSet = null;
        Statement stmt = null;
        LinkedHashMap<String, String> products = new LinkedHashMap<>();
        try{
            stmt = JDBC.getConn().createStatement();
            resultSet = stmt.executeQuery(sql);
        }catch (Exception e){
            ConsoleWriter.displayError("Error while connecting to the database");
        }
        try{
            while ( resultSet.next() ) {
                products.put(resultSet.getString("final_product_id"), resultSet.getString("product_name"));
            }
        }catch (Exception e){
            ConsoleWriter.displayError("Error while reading the database");
        }
        try {
            stmt.close();
            resultSet.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        ConsoleWriter.displayHeader("Visualize Product Structure");
        String answer = InputReader.read("Search for a product, press Enter to show all products or type 'exit' to return to the main menu");
        switch (answer){
            case "":
                List<String> peo = new ArrayList<>(products.values());
                int productId = InputReader.showListReturnIndexWithExit(peo, "Select a product to visualize its structure");
                if (productId == -1){
                    new ProductStructureUI().run();
                }else {
                    List<String> options = List.of("Bill Of Materials", "Bill of Operations","Exit");
                    int booOrBom = InputReader.showListReturnIndex(options, "Select an option");
                    switch (booOrBom){
                        case 1:
                            productStructureController.visualizeBOM(products.keySet().toArray()[productId-1].toString());
                            break;
                        case 2:
                            productStructureController.visualizeBOO(products.keySet().toArray()[productId-1].toString());
                            break;
                        case 3:
                            new ProductStructureUI().run();
                            break;
                    }
                }
                break;
            case "exit":
                MenuUI.showMenu();
                break;
            default:
                HashMap<String, String> productsWithSearchPrompt = new HashMap<>();
                List<String> productsWithSearchPromptList = new ArrayList<>();
                for(String product : products.values()){
                    if(product.toLowerCase().contains(answer.toLowerCase())){
                        productsWithSearchPrompt.put(product, products.entrySet().stream().filter(entry -> entry.getValue().equals(product)).findFirst().get().getKey());
                        productsWithSearchPromptList.add(product);
                    }
                }
                if(productsWithSearchPrompt == null){
                    ConsoleWriter.displayError("No products found with the search prompt: " + answer);
                    new ProductStructureUI().run();
                }else {
                    int productIdPrompt = InputReader.showListReturnIndexWithExit(productsWithSearchPromptList, "Select a product to visualize its structure");
                    if(productIdPrompt == -1){
                        new ProductStructureUI().run();
                    }else {
                        List<String> options = List.of("Bill Of Materials", "Bill of Operations","Exit");
                        int booOrBom = InputReader.showListReturnIndex(options, "Select an option");
                        switch (booOrBom){
                            case 1:
                                productStructureController.visualizeBOM(productsWithSearchPrompt.get(productsWithSearchPromptList.get(productIdPrompt-1)));
                                break;
                            case 2:
                                productStructureController.visualizeBOO(productsWithSearchPrompt.get(productsWithSearchPromptList.get(productIdPrompt-1)));
                                break;
                            case 3:
                                new ProductStructureUI().run();
                                break;
                        }
                    }
                }
                break;
        }
    }
}
