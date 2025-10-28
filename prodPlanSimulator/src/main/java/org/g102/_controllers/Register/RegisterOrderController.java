package org.g102._controllers.Register;

import org.g102.database.JDBC;
import org.g102.domain.Address;
import org.g102.domain.Customer;
import org.g102.domain.Product;
import org.g102.tools.ConsoleWriter;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RegisterOrderController {

    public void registerOrder(int orderID, int customerId, int addressId, String orderDate, String deliveryDate, HashMap<Product, Integer> products_list) {
        String sql = "INSERT INTO CustomerOrder VALUES("+ orderID + "," + customerId + ",TO_DATE('" + orderDate + "','DD-MM-YYYY'),TO_DATE('" + deliveryDate + "','DD-MM-YYYY')," + addressId + ")";
        try {
            Connection conn = JDBC.getConn();
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
            for (Product product : products_list.keySet()) {
                String sql2 = "INSERT INTO OrderProductList VALUES(" + orderID + ",'" + product.getPid() + "'," + products_list.get(product) + ")";
                Statement stmt2 = conn.createStatement();
                stmt2.executeUpdate(sql2);
                stmt2.close();
            }
            ConsoleWriter.displaySuccess("Order registered successfully.");
        } catch (Exception e) {
            ConsoleWriter.displayError("Error registering order: " + e.getMessage() + " " + sql);
            e.printStackTrace();
        }
    }

    public List<Customer> getCustomers() {
        String sql = "SELECT * FROM Customer WHERE status = 1";
        List<Customer> customers = new ArrayList<>();
        try{
            Connection conn = JDBC.getConn();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                customers.add(new Customer(rs.getInt("customer_id"), rs.getString("name")));
            }
            rs.close();
            stmt.close();
        }catch (Exception e){
            ConsoleWriter.displayError("Error getting customers: " + e.getMessage());
            e.printStackTrace();
        }
        return customers;
    }

    public List<Address> getAddresses() {
        String sql = "SELECT * FROM Address";
        List<Address> addresses = new ArrayList<>();
        try{
                Connection conn = JDBC.getConn();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                while(rs.next()){
                    addresses.add(new Address(rs.getInt("address_id"), rs.getString("address_name")));
                }
                rs.close();
                stmt.close();
            }catch (Exception e){
                ConsoleWriter.displayError("Error getting addresses: " + e.getMessage());
                e.printStackTrace();
        }
        return addresses;
    }

    public List<Product> getProducts() {
        String sql = "SELECT * FROM FinalProduct";
        List<Product> products = new ArrayList<>();
        try{
            Connection conn = JDBC.getConn();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                products.add(new Product(rs.getString("final_product_id"), rs.getString("product_name"), ""));
            }
            rs.close();
            stmt.close();
        }catch (Exception e){
            ConsoleWriter.displayError("Error getting products: " + e.getMessage());
            e.printStackTrace();
        }
        return products;
    }

}
