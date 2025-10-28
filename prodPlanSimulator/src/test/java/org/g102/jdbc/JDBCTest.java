package org.g102.jdbc;

import org.g102.database.JDBC;
import org.g102.domain.Workstation;
import org.g102.domain.WorkstationType;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class JDBCTest {

    @Test
    void TestTryConnection() throws SQLException {
        boolean connected = JDBC.tryConnect();
        assertTrue(connected);
    }

    @Test
    void TestTryExecuteSQL() throws SQLException {
        String sql = "SELECT * FROM Dual";
        boolean executed = JDBC.tryExecuteSQL(sql);
        assertTrue(executed);
    }

    @Test
    void insertWorkstation() {
        WorkstationType workstationType = new WorkstationType("AS000", "Type1");
        Workstation ws = new Workstation(999, workstationType, "ws test name", "desc");
        //String sql = workstationType.getSQLInsert();
        //JDBC.tryExecuteSQL(sql);
        boolean inserted = JDBC.insertWorkstation(ws);
        String sql = "DELETE FROM Workstation WHERE Workstation.workstation_id = 999";
        JDBC.tryExecuteSQL(sql);
        sql = "DELETE FROM WorkstationType WHERE WorkstationType.typeid = 'AS000'";
        JDBC.tryExecuteSQL(sql);
    }

  /*  @Test
    void insertProduct() {
        boolean inserted = RegisterProductController.insertProduct(999, "pr test name", "desc", );

        String sql = "DELETE FROM Product WHERE Product.product_id = 999";
        JDBC.tryExecuteSQL(sql);
        sql = "DELETE FROM WorkstationType WHERE WorkstationType.typeid = 'AS000'";
        JDBC.tryExecuteSQL(sql);
    }
    */
}