package com.aib.servlet;

import com.aib.util.DatabaseConnection;
import com.aib.bean.Fruit;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/FruitServlet")
public class FruitServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 測試資料庫連接
        String connectionStatus = DatabaseConnection.testConnection();
        request.setAttribute("connectionStatus", connectionStatus);

        // 查詢水果數據
        List<Fruit> fruits = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM fruits")) {
            while (rs.next()) {
                Fruit fruit = new Fruit();
                fruit.setId(rs.getInt("id"));
                fruit.setName(rs.getString("name"));
                fruit.setSourceCity(rs.getString("source_city"));
                fruit.setStockLevel(rs.getInt("stock_level"));
                fruits.add(fruit);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Unable to fetch fruits: " + e.getMessage());
        }
        request.setAttribute("fruits", fruits);
        request.getRequestDispatcher("fruitList.jsp").forward(request, response);
    }
}