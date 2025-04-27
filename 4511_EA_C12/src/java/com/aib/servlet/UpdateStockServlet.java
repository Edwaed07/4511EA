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
public class UpdateStockServlet extends HttpServlet {

    public static class Stock {
        private String fruitName;
        private int stockLevel;
        private String sourceCity;
        private String country;

        public Stock(String fruitName, int stockLevel, String sourceCity, String country) {
            this.fruitName = fruitName;
            this.stockLevel = stockLevel;
            this.sourceCity = sourceCity;
            this.country = country;
        }

        public String getFruitName() { return fruitName; }
        public int getStockLevel() { return stockLevel; }
        public String getSourceCity() { return sourceCity; }
        public String getCountry() { return country; }
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
            String sql = "SELECT fruit_name, stock_level, source_city, country FROM branch_inventory WHERE branch = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, employeeBranch);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    stockList.add(new Stock(
                        rs.getString("fruit_name"),
                        rs.getInt("stock_level"),
                        rs.getString("source_city"),
                        rs.getString("country")
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
}