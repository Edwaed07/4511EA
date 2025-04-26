package com.aib.util;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseTest {
    public static void main(String[] args) {
        System.out.println("开始测试数据库连接...");
        try {
            System.out.println(DatabaseConnection.testConnection());
            
            try (Connection conn = DatabaseConnection.getConnection()) {
                System.out.println("成功获取数据库连接: " + conn);
            }
        } catch (SQLException e) {
            System.err.println("数据库连接测试失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 