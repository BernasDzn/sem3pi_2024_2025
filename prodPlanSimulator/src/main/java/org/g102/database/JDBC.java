package org.g102.database;

import oracle.jdbc.OracleDriver;
import org.g102.domain.Workstation;
import org.g102.tools.ConsoleWriter;

import java.sql.*;

public class JDBC {

    private static final String URL = "jdbc:oracle:thin:@localhost:1521:XE";
    private static final String USER = "system";
    private static final String PASSWORD = "system";

    private static Connection conn;

    public static Connection getConn() {
        tryConnect();
        return conn;
    }

    public static boolean tryConnect() {
        if (conn != null) return true; // Return true if already connected
        try {
            DriverManager.registerDriver(new OracleDriver());
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            try (Statement stmt = conn.createStatement()) {
                stmt.executeQuery("SELECT * FROM Dual");
            }catch (SQLException e){
                return false;
            }
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public static boolean insertWorkstation(Workstation ws) {
        String command = "BEGIN AddWorkstation(" + ws.getNumber() + ", '" + ws.getWorkstationType().getName() + "', '" + ws.getName() + "'); END;";
        boolean result = JDBC.tryExecuteSQL(command);
        if (result) {
            ConsoleWriter.displayError("Workstation " + ws.getName() + " inserted successfully.");
        } else {
            ConsoleWriter.displayError("Workstation " + ws.getName() + " could not be inserted.");
        }
        return result;
    }

    public static ResultSet getListFromTable(String table_name){
        try {
            Statement stmt = getConn().createStatement();
            return stmt.executeQuery("SELECT * FROM " + table_name);
        }catch (Exception e){
            return null;
        }
    }

    /**
     * tryExecuteSQL executes a SQL or PL/SQL query and returns true if it was successful, false otherwise.
     * @param sql
     * @return
     */
    public static boolean tryExecuteSQL(String sql){
        try {
            Statement stmt = getConn().createStatement();
            stmt.executeQuery(sql);
            return true;
        }catch (Exception e){
            return false;
        }
    }

}
