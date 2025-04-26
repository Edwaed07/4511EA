package com.aib.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/4511_db?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = ""; // 如果有密碼，請填入

    static {
        try {
            // 手動載入 MySQL 驅動程式
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("MySQL JDBC Driver loaded successfully.");
        } catch (ClassNotFoundException e) {
            try {
                // 尝试加载旧版驱动
                Class.forName("com.mysql.jdbc.Driver");
                System.out.println("Old MySQL JDBC Driver loaded successfully.");
            } catch (ClassNotFoundException ex) {
                System.err.println("CRITICAL ERROR: Failed to load any MySQL JDBC Driver!");
                System.err.println("Error message: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    public static Connection getConnection() throws SQLException {
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Database connection established successfully.");
            return conn;
        } catch (SQLException e) {
            System.err.println("Failed to connect to database: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public static String testConnection() {
        try (Connection conn = getConnection()) {
            return "Database connection successful!";
        } catch (SQLException e) {
            return "Database connection failed: " + e.getMessage();
        }
    }
}