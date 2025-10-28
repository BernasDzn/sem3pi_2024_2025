package org.g102._controllers.Register;

import org.g102.database.JDBC;
import org.g102.domain.Customer;
import org.g102.tools.ConsoleWriter;
import smetana.core.__struct__;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DeactivateCustomerController {
    public void deactivateCustomer(int customerId) {
        String sql = "UPDATE Customer SET status = 0 WHERE customer_id = " + customerId;
        try {
            Connection conn = JDBC.getConn();
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
            System.out.println();
            ConsoleWriter.displaySuccess("Customer deactivated successfully");
            System.out.println();
        } catch (Exception e) {
            ConsoleWriter.displayError("Error deactivating customer: " + e.getMessage());
        }
    }

    public List<Customer> getCustomersActive() {
        String sql = "SELECT * FROM Customer WHERE status = 1";
        List<Customer> customers = new ArrayList<>();
        try {
            Connection conn = JDBC.getConn();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                customers.add(new Customer(rs.getInt("customer_id"), rs.getString("name")));
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            ConsoleWriter.displayError("Error getting active customers: " + e.getMessage());
            return null;
        }
        return customers;
    }
}
