package org.g102._controllers.Register;

import org.g102.database.JDBC;
import org.g102.domain.ProductFamily;
import org.g102.tools.ConsoleWriter;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RegisterProductController {

    public void registerProduct(String productId,  int familyId,String productDescription, String productName) {
        String sql1 = "INSERT INTO FactoryItem VALUES ('" + productId + "', '" + productDescription + "')";
        String sql2 = "INSERT INTO FinalProduct VALUES ('" + productId + "', " + familyId + ", '" + productName + "')";
        try {
            Connection conn = JDBC.getConn();
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql1);
            stmt.executeUpdate(sql2);
            stmt.close();
            System.out.println();
            ConsoleWriter.displaySuccess("Product:  " + productId  + " registered successfully.");
            System.out.println();
        } catch (Exception e) {
            ConsoleWriter.displayError("Error registering product: " + e.getMessage() );
        }
    }

    public List<ProductFamily> getProductFamilies() {
        List<ProductFamily> productFamilies = new ArrayList<>();
        try {
            String sql = "SELECT * FROM ProductFamily";
            Connection conn = JDBC.getConn();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                productFamilies.add(new ProductFamily(rs.getInt("family_id"), rs.getString("family_name")));
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            ConsoleWriter.displayError("Error fetching product families: " + e.getMessage() );
        }
        return productFamilies;
    }
}

