package com.aib.servlet;

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

@WebServlet(name = "updateStockServlet", urlPatterns = {"/updateStockServlet"})
public class updateStockServlet extends HttpServlet {

    public static class Stock {
        private String fruitName;
        private int stockLevel;

        public Stock(String fruitName, int stockLevel) {
            this.fruitName = fruitName;
            this.stockLevel = stockLevel;
        }

        public String getFruitName() { return fruitName; }
        public int getStockLevel() { return stockLevel; }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String employeeBranch = (String) session.getAttribute("employeeBranch");

        if (employeeBranch == null) {
            request.setAttribute("error", "Please login as shop staff to view stock.");
            request.getRequestDispatcher("updateStock.jsp").forward(request, response);
            return;
        }

        Connection conn = null;
        List<Stock> stockList = new ArrayList<>();
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "SELECT fruit_name, stock_level FROM branch_inventory WHERE branch = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, employeeBranch);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    stockList.add(new Stock(
                        rs.getString("fruit_name"),
                        rs.getInt("stock_level")
                    ));
                }
            }
            request.setAttribute("stockList", stockList);
        } catch (SQLException e) {
            request.setAttribute("error", "Failed to retrieve stock data: " + e.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        request.getRequestDispatcher("updateStock.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String employeeBranch = (String) session.getAttribute("employeeBranch");

        if (employeeBranch == null) {
            request.setAttribute("error", "Please login as shop staff to update stock.");
            doGet(request, response);
            return;
        }

        String fruitName = request.getParameter("fruitName");
        int newStockLevel;
        try {
            newStockLevel = Integer.parseInt(request.getParameter("newStockLevel"));
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid stock level entered.");
            doGet(request, response);
            return;
        }

        if (newStockLevel < 0) {
            request.setAttribute("error", "Stock level cannot be negative.");
            doGet(request, response);
            return;
        }

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "UPDATE branch_inventory SET stock_level = ? WHERE branch = ? AND fruit_name = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, newStockLevel);
                stmt.setString(2, employeeBranch);
                stmt.setString(3, fruitName);
                int rowsUpdated = stmt.executeUpdate();
                if (rowsUpdated > 0) {
                    request.setAttribute("success", "Stock for " + fruitName + " updated successfully.");
                } else {
                    request.setAttribute("error", "Failed to update stock for " + fruitName + ".");
                }
            }
        } catch (SQLException e) {
            request.setAttribute("error", "Failed to update stock: " + e.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        doGet(request, response);
    }
}