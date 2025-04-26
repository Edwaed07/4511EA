package com.aib.servlet;

import com.aib.bean.Fruit;
import com.aib.util.DatabaseConnection;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "FruitManagementServlet", urlPatterns = {"/FruitManagementServlet"})
public class FruitManagementServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            session.setAttribute("connectionStatus", "Connected to database successfully");
            System.out.println("Connected to database: " + conn.getCatalog());

            String employeeBranch = (String) session.getAttribute("employeeBranch");
            if (employeeBranch == null) {
                request.setAttribute("error", "Employee branch not found. Please login as shopStaff.");
                request.getRequestDispatcher("FruitManagement.jsp").forward(request, response);
                return;
            }

            String action = request.getParameter("action");
            if ("edit".equals(action)) {
                // 獲取要編輯的分店庫存記錄
                String fruitName = request.getParameter("fruitName");
                String sourceCity = request.getParameter("sourceCity");
                String sql = "SELECT fruit_name, source_city, country, stock_level FROM branch_inventory WHERE branch = ? AND fruit_name = ? AND source_city = ?";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, employeeBranch);
                    stmt.setString(2, fruitName);
                    stmt.setString(3, sourceCity);
                    ResultSet rs = stmt.executeQuery();
                    if (rs.next()) {
                        Fruit inventory = new Fruit(
                            0, // ID 這裡不需要
                            rs.getString("fruit_name"),
                            rs.getString("source_city"),
                            rs.getString("country"),
                            rs.getInt("stock_level")
                        );
                        request.setAttribute("editInventory", inventory);
                    }
                }
            }

            // 獲取所有可用的水果（用於新增時選擇）
            List<Fruit> availableFruits = new ArrayList<>();
            String fruitSql = "SELECT name, source_city, country FROM fruits";
            try (PreparedStatement stmt = conn.prepareStatement(fruitSql)) {
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    availableFruits.add(new Fruit(
                        0, // ID 這裡不需要
                        rs.getString("name"),
                        rs.getString("source_city"),
                        rs.getString("country"),
                        0 // stockLevel 這裡不需要
                    ));
                }
            }
            request.setAttribute("availableFruits", availableFruits);

            // 獲取分店庫存清單
            List<Fruit> inventory = new ArrayList<>();
            String inventorySql = "SELECT fruit_name, source_city, country, stock_level FROM branch_inventory WHERE branch = ?";
            try (PreparedStatement stmt = conn.prepareStatement(inventorySql)) {
                stmt.setString(1, employeeBranch);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    inventory.add(new Fruit(
                        0, // ID 這裡不需要
                        rs.getString("fruit_name"),
                        rs.getString("source_city"),
                        rs.getString("country"),
                        rs.getInt("stock_level")
                    ));
                }
            }
            request.setAttribute("inventory", inventory);
            request.getRequestDispatcher("FruitManagement.jsp").forward(request, response);
        } catch (SQLException e) {
            session.setAttribute("connectionStatus", "Database error: " + e.getMessage());
            request.setAttribute("error", "Failed to retrieve data: " + e.getMessage());
            System.out.println("Database error: " + e.getMessage());
            e.printStackTrace();
            request.getRequestDispatcher("FruitManagement.jsp").forward(request, response);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    System.out.println("Error closing connection: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            session.setAttribute("connectionStatus", "Connected to database successfully");
            System.out.println("Connected to database: " + conn.getCatalog());

            String employeeBranch = (String) session.getAttribute("employeeBranch");
            if (employeeBranch == null) {
                request.setAttribute("error", "Employee branch not found. Please login as shopStaff.");
                doGet(request, response);
                return;
            }

            String action = request.getParameter("action");
            if ("create".equals(action)) {
                // 新增分店庫存
                String fruitSelection = request.getParameter("fruit");
                String[] fruitParts = fruitSelection.split(":");
                String fruitName = fruitParts[0];
                String sourceCity = fruitParts[1];
                int stockLevel = Integer.parseInt(request.getParameter("stockLevel"));

                // 獲取 country
                String countrySql = "SELECT country FROM fruits WHERE name = ? AND source_city = ?";
                String country = null;
                try (PreparedStatement stmt = conn.prepareStatement(countrySql)) {
                    stmt.setString(1, fruitName);
                    stmt.setString(2, sourceCity);
                    ResultSet rs = stmt.executeQuery();
                    if (rs.next()) {
                        country = rs.getString("country");
                    }
                }

                if (country == null) {
                    request.setAttribute("error", "Selected fruit not found.");
                    doGet(request, response);
                    return;
                }

                // 檢查是否已存在該水果的分店庫存記錄
                String checkSql = "SELECT stock_level FROM branch_inventory WHERE branch = ? AND fruit_name = ? AND source_city = ?";
                boolean exists = false;
                try (PreparedStatement stmt = conn.prepareStatement(checkSql)) {
                    stmt.setString(1, employeeBranch);
                    stmt.setString(2, fruitName);
                    stmt.setString(3, sourceCity);
                    ResultSet rs = stmt.executeQuery();
                    if (rs.next()) {
                        exists = true;
                        int currentStock = rs.getInt("stock_level");
                        // 更新庫存
                        String updateSql = "UPDATE branch_inventory SET stock_level = stock_level + ? WHERE branch = ? AND fruit_name = ? AND source_city = ?";
                        try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                            updateStmt.setInt(1, stockLevel);
                            updateStmt.setString(2, employeeBranch);
                            updateStmt.setString(3, fruitName);
                            updateStmt.setString(4, sourceCity);
                            updateStmt.executeUpdate();
                            request.setAttribute("success", "Inventory updated successfully.");
                        }
                    }
                }

                if (!exists) {
                    // 新增庫存記錄
                    String insertSql = "INSERT INTO branch_inventory (branch, fruit_name, source_city, country, stock_level) VALUES (?, ?, ?, ?, ?)";
                    try (PreparedStatement stmt = conn.prepareStatement(insertSql)) {
                        stmt.setString(1, employeeBranch);
                        stmt.setString(2, fruitName);
                        stmt.setString(3, sourceCity);
                        stmt.setString(4, country);
                        stmt.setInt(5, stockLevel);
                        stmt.executeUpdate();
                        request.setAttribute("success", "Fruit added to inventory successfully.");
                    }
                }
            } else if ("update".equals(action)) {
                // 更新分店庫存
                String fruitName = request.getParameter("fruitName");
                String sourceCity = request.getParameter("sourceCity");
                int stockLevel = Integer.parseInt(request.getParameter("stockLevel"));

                String sql = "UPDATE branch_inventory SET stock_level = ? WHERE branch = ? AND fruit_name = ? AND source_city = ?";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setInt(1, stockLevel);
                    stmt.setString(2, employeeBranch);
                    stmt.setString(3, fruitName);
                    stmt.setString(4, sourceCity);
                    int rows = stmt.executeUpdate();
                    if (rows > 0) {
                        request.setAttribute("success", "Inventory updated successfully.");
                    } else {
                        request.setAttribute("error", "Failed to update inventory.");
                    }
                }
            } else if ("delete".equals(action)) {
                // 刪除分店庫存
                String fruitName = request.getParameter("fruitName");
                String sourceCity = request.getParameter("sourceCity");

                String sql = "DELETE FROM branch_inventory WHERE branch = ? AND fruit_name = ? AND source_city = ?";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, employeeBranch);
                    stmt.setString(2, fruitName);
                    stmt.setString(3, sourceCity);
                    int rows = stmt.executeUpdate();
                    if (rows > 0) {
                        request.setAttribute("success", "Inventory item deleted successfully.");
                    } else {
                        request.setAttribute("error", "Failed to delete inventory item.");
                    }
                }
            }

            // 重新獲取分店庫存清單
            doGet(request, response);
        } catch (SQLException e) {
            session.setAttribute("connectionStatus", "Database error: " + e.getMessage());
            request.setAttribute("error", "Failed to process request: " + e.getMessage());
            System.out.println("Database error: " + e.getMessage());
            e.printStackTrace();
            doGet(request, response);
        } catch (Exception e) {
            session.setAttribute("connectionStatus", "Unexpected error: " + e.getMessage());
            request.setAttribute("error", "Unexpected error: " + e.getMessage());
            System.out.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
            doGet(request, response);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    System.out.println("Error closing connection: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }
}