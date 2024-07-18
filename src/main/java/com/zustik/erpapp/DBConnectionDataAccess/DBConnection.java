package com.zustik.erpapp.DBConnectionDataAccess;

import java.sql.*;

public class DBConnection {
    private final static String CONN_STRING = "jdbc:mysql://localhost:3306/erp_items";
    private static DBConnection dbConnection;
    private Connection connection;

    private DBConnection() {
        try {
            this.connection = DriverManager.getConnection(CONN_STRING, "root", "asdasdasd333");
        } catch (SQLException e) {
            throw new RuntimeException("Failed to connect to database: " + e.getMessage(), e);
        }
    }

    public static DBConnection getInstance() {
        if (dbConnection == null) {
            dbConnection = new DBConnection();
        }
        return dbConnection;
    }

    public Connection getConnection() {
        return this.connection;
    }

    public void closeConnection() {
        if (this.connection != null) {
            try {
                this.connection.close();
            } catch (SQLException e) {
                System.err.println("Failed to close the database connection: " + e.getMessage());
            }
        }
    }
}


