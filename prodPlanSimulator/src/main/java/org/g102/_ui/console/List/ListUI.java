package org.g102._ui.console.List;

import org.g102.database.JDBC;
import org.g102.tools.ConsoleWriter;
import org.g102.tools.InputReader;
import org.g102._ui.console._Menu.MenuUI;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListUI implements Runnable{

    @Override
    public void run() {
        showMenu();
    }

    public void showMenu(){
        ConsoleWriter.displayHeader("List");
        int selectedOption = InputReader.showListReturnIndex(List.of(
                "Workstations"
                , "Customers"
                , "Products"
                , "Materials"
                , "Customer Orders"
                , "Operations the factory supports"
                , "Go Back"), null);
        switch (selectedOption) {
            case 1:
                showWorkstations();
                break;
            case 2:
                showCustomers();
                break;
            case 3:
                showProducts();
                break;
            case 4:
                showMaterials();
                break;
            case 5:
                showCustomerOrders();
                break;
            case 6:
                showOperations();
                break;
            case 7:
                MenuUI.showMenu();
                break;
        }
    }

    private void showCustomerOrders() {
        ConsoleWriter.displayHeader("Customer Orders");
        String [] columns = {"order_id", "name", "date_order", "date_delivery", "num_products"};
        ResultSet rs = null;
        try{
            rs = JDBC.getConn().createStatement().executeQuery("SELECT o.order_id, c.name, o.date_order, o.date_delivery, count(opl.order_id) as num_products FROM Customer c, CustomerOrder o, OrderProductList opl Where c.customer_id = o.customer_id AND o.order_id = opl.order_id GROUP BY o.order_id, c.name, o.date_order, o.date_delivery");
        }catch (SQLException e){
            ConsoleWriter.displayError("Error while reading the result set.");
            showMenu();
        }
        System.out.println(String.format("%-15s | %-30s | %-20s | %-20s | %-25s", "Order ID", "Customer Name", "Order Date", "Delivery Date", "Number of Products ordered"));
        System.out.println("---------------------------------------------------------------------------------------------------------------------------------");
        try {
            if(rs.getFetchSize() > 0) {
                while (rs.next()) {
                    System.out.println(String.format("%-15s | %-30s | %-20s | %-20s | %-25s", rs.getInt(columns[0]), rs.getString(columns[1]), rs.getDate(columns[2]), rs.getDate(columns[3]), rs.getInt(columns[4])));
                }
            }else {
                System.out.println("No orders found.");
            }
            rs.close();
        } catch (SQLException e) {
            ConsoleWriter.displayError("Error while reading the result set.");
        }
        System.out.println();
        showMenu();
    }

    private void showMaterials() {
        ConsoleWriter.displayHeader("Materials");
        String [] columns = {"description", "qt_in_stock", "min_stock_size"};
        ResultSet rs = null;
        try{
            rs = JDBC.getConn().createStatement().executeQuery("SELECT fi.description, st.qt_in_stock, st.min_stock_size FROM FactoryItem fi, Material m, MaterialStock st WHERE fi.item_id = m.material_id AND m.material_id = st.material_id");
        }catch (SQLException e){
            ConsoleWriter.displayError("Error while reading the result set.");
            showMenu();
        }
        System.out.println(String.format("%-40s | %-20s | %-20s", "Material Description", "Quantity in Stock", "Minimum Stock Size"));
        System.out.println("---------------------------------------------------------------------------------------------------------------------------------");
        try {
            if(rs.getFetchSize() > 0) {
                while (rs.next()) {
                    System.out.println(String.format("%-40s | %-20s | %-20s", rs.getString(columns[0]), rs.getString(columns[1]) + " units", rs.getString(columns[2]) + " units"));
                }
            }else {
                System.out.println("No materials found.");
            }
            rs.close();
        } catch (SQLException e) {
            ConsoleWriter.displayError("Error while reading the result set.");
        }
        System.out.println();
        showMenu();
    }

    private void showOperations() {
        ConsoleWriter.displayHeader("Operations");
        String [] columns = {"operation_name"};
        ResultSet rs = null;
        try{
            rs = JDBC.getConn().createStatement().executeQuery("SELECT operation_name FROM OperationType WHERE op_type_id IN (SELECT op_type_id FROM Operation)");
        }catch (SQLException e){
            ConsoleWriter.displayError("Error while reading the result set.");
            showMenu();
        }
        System.out.println(String.format("%-30s", "Operation Name"));
        System.out.println("-----------------------------------------");
        try {
            if(rs.getFetchSize() > 0) {
                while (rs.next()) {
                    System.out.println(String.format("%-30s", rs.getString(columns[0])));
                }
            }else {
                System.out.println("No operations found.");
            }
            rs.close();
        } catch (SQLException e) {
            ConsoleWriter.displayError("Error while reading the result set.");
        }
        System.out.println();
        showMenu();
    }

    private void showCustomers() {
        ConsoleWriter.displayHeader("Customers");
        String [] columns = {"name", "vatin", "address_name", "type","email_address", "phone_number", "status"};
        ResultSet rs = null;
        try{
            rs = JDBC.getConn().createStatement().executeQuery("SELECT c.name, c.vatin, a.address_name, ct.type, c.email_address, c.phone_number, c.status FROM Customer c, Address a, CustomerType ct Where c.address_id = a.address_id AND c.type_id = ct.type_id");
        }catch (SQLException e){
            ConsoleWriter.displayError("Error while reading the result set.");
            showMenu();
        }
        System.out.println(String.format("%-25s | %-15s | %-60s | %-10s | %-25s | %-20s | %-15s", "Name", "VATIN", "Address", "Type", "Email", "Phone", "Status"));
        System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------------------------");
        try {
            if(rs.getFetchSize() > 0) {
                while (rs.next()) {
                    String status = "Active";
                    if(rs.getInt(columns[6]) == 0){
                        status = "Inactive";
                    }
                    System.out.println(String.format("%-25s | %-15s | %-60s | %-10s | %-25s | %-20s | %-15s", rs.getString(columns[0]), rs.getString(columns[1]), rs.getString(columns[2]), rs.getString(columns[3]), rs.getString(columns[4]), rs.getString(columns[5]), status));
                }
            }else {
                System.out.println("No customers found.");
            }
            rs.close();
        } catch (SQLException e) {
            ConsoleWriter.displayError("Error while reading the result set.");
        }
        System.out.println();
        showMenu();
    }

    private void showWorkstations() {
        ConsoleWriter.displayHeader("Workstations");
        String [] columns = {"workstation_id", "type_name", "name"};
        ResultSet rs = null;
        try{
            rs = JDBC.getConn().createStatement().executeQuery("SELECT w.workstation_id, wt.type_name, w.name FROM Workstation w, WorkstationType wt Where w.workstation_type_id = wt.type_id");
        }catch (SQLException e){
            ConsoleWriter.displayError("Error while reading the result set.");
            showMenu();
        }
        System.out.println(String.format("%-20s | %-60s | %-20s", "Workstation ID", "Type", "Name"));
        System.out.println("-----------------------------------------------------------------------------");
        try {
            if(rs.getFetchSize() > 0) {
                while (rs.next()) {
                    System.out.println(String.format("%-20s | %-60s | %-20s", rs.getInt(columns[0]), rs.getString(columns[1]), rs.getString(columns[2])));
                }
            }else {
                System.out.println("No workstations found.");
            }
            rs.close();
        } catch (SQLException e) {
            ConsoleWriter.displayError("Error while reading the result set.");
        }
        System.out.println();
        showMenu();
    }

    private void showProducts() {
        ConsoleWriter.displayHeader("Products");
        String [] columns = {"product_name", "family_name", "description"};
        ResultSet rs = null;
        try{
            rs = JDBC.getConn().createStatement().executeQuery("SELECT fp.product_name, pf.family_name, fi.description FROM FinalProduct fp, ProductFamily pf, FactoryItem fi Where fp.family_id = pf.family_id AND fp.final_product_id = fi.item_id");
        }catch (SQLException e){
            ConsoleWriter.displayError("Error while reading the result set.");
            showMenu();
        }
        System.out.println(String.format("%-30s | %-30s | %-50s", "Product Name", "Family", "Description"));
        System.out.println("---------------------------------------------------------------------------------------------------------------------------");
        try {
            if(rs.getFetchSize() > 0) {
                while (rs.next()) {
                    System.out.println(String.format("%-30s | %-30s | %-50s", rs.getString(columns[0]), rs.getString(columns[1]), rs.getString(columns[2])));
                }
            }else {
                System.out.println("No Products found.");
            }
            rs.close();
        } catch (SQLException e) {
            ConsoleWriter.displayError("Error while reading the result set.");
        }
        System.out.println();
        showMenu();
    }


}
