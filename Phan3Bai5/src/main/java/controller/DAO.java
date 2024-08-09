package controller;

import java.sql.*;
import java.util.Properties;

public class DAO {
    private static final String CONNECTION_URL = "jdbc:mysql://localhost:3306/employees?serverTimezone=UTC";
    private static DAO instance;
    private Connection conn;

    private DAO(){
        Properties properties = new Properties();
        properties.setProperty("user", "root");
        properties.setProperty("password", "123456");

        try {
            conn = DriverManager.getConnection(CONNECTION_URL, properties);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static synchronized DAO getInstance() {
        if (instance == null) {
            instance = new DAO();
        }

        return instance;
    }

    public void closeConnection() {
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
