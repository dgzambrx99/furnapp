package com.furnaceapp;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

//from http://stackoverflow.com/questions/1862283/help-me-create-a-jtds-connection-string
public class DriverTest {
    public static void main(String[] args) throws SQLException {
        Connection conn = null;
        ResultSet rs = null;
        String url = "jdbc:jtds:sqlserver://127.0.0.1;instance=SQLEXPRESS;DatabaseName=furnapp1";
        String driver = "net.sourceforge.jtds.jdbc.Driver";
        String userName = "sa";
        String password = "712Bond";
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, userName, password);
            System.out.println("Connected to the database!!! Getting table list...");
            DatabaseMetaData dbm = conn.getMetaData();
            rs = dbm.getTables(null, null, "%", new String[] { "TABLE" });
            while (rs.next()) { System.out.println(rs.getString("TABLE_NAME")); }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.close();
            rs.close();
        }
    }
}
