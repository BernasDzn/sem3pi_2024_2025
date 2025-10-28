package org.g102._controllers.Register;

import org.g102.database.JDBC;
import org.g102.domain.Workstation;
import org.g102.tools.ConsoleWriter;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RegisterWorkstationController {

    public List<Workstation> getWorkstationTypes() {
        List<Workstation> workstationTypes = new ArrayList<>();
        String sql = "SELECT WorkstationType.type_id, WorkstationType.type_name FROM WorkstationType";

        try{
            JDBC.tryConnect();
            ResultSet rs = JDBC.getConn().createStatement().executeQuery(sql);

            // Iterate through the ResultSet and add each name to the list
            while (rs.next()) {
                workstationTypes.add(new Workstation(rs.getString("type_id"), rs.getString("type_name")));
            }

        } catch (Exception e) {
            ConsoleWriter.displayLog("Error fetching workstation types: " + e.getMessage());
        }

        return workstationTypes;
    }

    public void registerWorkstation(String workstationId, String workstationName, String workstationDescription, Workstation typeID) {
        String sql = "INSERT INTO Workstation VALUES (" + workstationId + ",'" + typeID.getDescription() + "', '" + workstationName + "', '" + workstationDescription + "')";
        try {
            Connection conn = JDBC.getConn();
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
            System.out.println();
            ConsoleWriter.displaySuccess("Workstation " + workstationName + " registered successfully.");
            System.out.println();
        } catch (Exception e) {
            ConsoleWriter.displayError("Error registering workstation: " + e.getMessage() + " " + sql);
        }
    }
}
