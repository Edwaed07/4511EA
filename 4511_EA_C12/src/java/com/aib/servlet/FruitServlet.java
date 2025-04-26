package com.aib.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.aib.bean.Fruit;
import com.aib.util.DatabaseConnection;

@WebServlet(name = "FruitServlet", urlPatterns = {"/FruitServlet"})
public class FruitServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            session.setAttribute("connectionStatus", "Connected to database successfully");
            System.out.println("Connected to database: " + conn.getCatalog());

            String page = request.getParameter("page");
            if (page == null || page.isEmpty() || "fruitList".equals(page)) {
                String country = request.getParameter("country");
                String sourceCity = request.getParameter("sourceCity");
                String[] branches = request.getParameterValues("branches");
                String minStock = request.getParameter("minStock");
                String maxStock = request.getParameter("maxStock");

                List<Fruit> fruits = new ArrayList<>();
                String sql = "SELECT DISTINCT f.fruit_id, f.fruit_name, f.source_location, f.country, " +
                        "COALESCE(bi.stock_level, 0) as stock_level " +
                        "FROM fruits f " +
                        "LEFT JOIN branch_inventory bi ON f.fruit_name = bi.fruit_name AND f.source_location = bi.source_city " +
                        "WHERE 1=1";

                if (country != null && !country.isEmpty()) {
                    sql += " AND f.country = ?";
                }
                if (sourceCity != null && !sourceCity.isEmpty()) {
                    sql += " AND f.source_location = ?";
                }
                if (branches != null && branches.length > 0) {
                    sql += " AND bi.branch IN (";
                    for (int i = 0; i < branches.length; i++) {
                        sql += "?";
                        if (i < branches.length - 1) sql += ",";
                    }
                    sql += ")";
                }
                if (minStock != null && !minStock.isEmpty()) {
                    sql += " AND COALESCE(bi.stock_level, 0) >= ?";
                }
                if (maxStock != null && !maxStock.isEmpty()) {
                    sql += " AND COALESCE(bi.stock_level, 0) <= ?";
                }

                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    int paramIndex = 1;
                    if (country != null && !country.isEmpty()) {
                        stmt.setString(paramIndex++, country);
                    }
                    if (sourceCity != null && !sourceCity.isEmpty()) {
                        stmt.setString(paramIndex++, sourceCity);
                    }
                    if (branches != null && branches.length > 0) {
                        for (String branch : branches) {
                            stmt.setString(paramIndex++, branch);
                        }
                    }
                    if (minStock != null && !minStock.isEmpty()) {
                        stmt.setInt(paramIndex++, Integer.parseInt(minStock));
                    }
                    if (maxStock != null && !maxStock.isEmpty()) {
                        stmt.setInt(paramIndex++, Integer.parseInt(maxStock));
                    }

                    ResultSet rs = stmt.executeQuery();
                    Map<String, Fruit> fruitMap = new HashMap<>();
                    while (rs.next()) {
                        String key = rs.getString("fruit_name") + ":" + rs.getString("source_location");
                        Fruit fruit = fruitMap.get(key);
                        if (fruit == null) {
                            fruit = new Fruit(
                                rs.getInt("fruit_id"),
                                rs.getString("fruit_name"),
                                rs.getString("source_location"),
                                rs.getString("country"),
                                rs.getInt("stock_level")
                            );
                            fruitMap.put(key, fruit);
                        }
                    }
                    fruits.addAll(fruitMap.values());
                }
                request.setAttribute("fruits", fruits);
                request.getRequestDispatcher("fruitList.jsp").forward(request, response);
            } else if (page.equals("borrowFruit")) {
                String country = request.getParameter("country");
                String sourceCity = request.getParameter("sourceCity");
                String[] branches = request.getParameterValues("branch");
                String minStock = request.getParameter("minStock");
                String maxStock = request.getParameter("maxStock");
                
                // 获取当前员工所在分支
                String employeeBranch = (String) session.getAttribute("employeeBranch");

                List<String> countries = new ArrayList<>();
                try (PreparedStatement stmt = conn.prepareStatement("SELECT DISTINCT f.country FROM fruits f JOIN branch_inventory bi ON f.fruit_name = bi.fruit_name ORDER BY f.country")) {
                    ResultSet rs = stmt.executeQuery();
                    while (rs.next()) {
                        countries.add(rs.getString("country"));
                    }
                }

                List<String> sourceCities = new ArrayList<>();
                try (PreparedStatement stmt = conn.prepareStatement("SELECT DISTINCT source_city FROM branch_inventory ORDER BY source_city")) {
                    ResultSet rs = stmt.executeQuery();
                    while (rs.next()) {
                        sourceCities.add(rs.getString("source_city"));
                    }
                }

                List<String> allBranches = new ArrayList<>();
                try (PreparedStatement stmt = conn.prepareStatement("SELECT DISTINCT branch FROM branch_inventory ORDER BY branch")) {
                    ResultSet rs = stmt.executeQuery();
                    while (rs.next()) {
                        allBranches.add(rs.getString("branch"));
                    }
                }

                List<Fruit> fruits = new ArrayList<>();
                String sql = "SELECT DISTINCT f.fruit_id, f.fruit_name, f.source_location, f.country, " +
                        "bi.stock_level, bi.branch " +
                        "FROM fruits f " +
                        "JOIN branch_inventory bi ON f.fruit_name = bi.fruit_name AND f.source_location = bi.source_city " +
                        "WHERE 1=1";

                if (country != null && !country.isEmpty()) {
                    sql += " AND f.country = ?";
                }
                if (sourceCity != null && !sourceCity.isEmpty()) {
                    sql += " AND f.source_location = ?";
                }
                if (branches != null && branches.length > 0) {
                    sql += " AND bi.branch IN (";
                    for (int i = 0; i < branches.length; i++) {
                        sql += "?";
                        if (i < branches.length - 1) sql += ",";
                    }
                    sql += ")";
                }
                if (minStock != null && !minStock.isEmpty()) {
                    sql += " AND bi.stock_level >= ?";
                }
                if (maxStock != null && !maxStock.isEmpty()) {
                    sql += " AND bi.stock_level <= ?";
                }

                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    int paramIndex = 1;
                    if (country != null && !country.isEmpty()) {
                        stmt.setString(paramIndex++, country);
                    }
                    if (sourceCity != null && !sourceCity.isEmpty()) {
                        stmt.setString(paramIndex++, sourceCity);
                    }
                    if (branches != null && branches.length > 0) {
                        for (String branch : branches) {
                            stmt.setString(paramIndex++, branch);
                        }
                    }
                    if (minStock != null && !minStock.isEmpty()) {
                        stmt.setInt(paramIndex++, Integer.parseInt(minStock));
                    }
                    if (maxStock != null && !maxStock.isEmpty()) {
                        stmt.setInt(paramIndex++, Integer.parseInt(maxStock));
                    }
                    ResultSet rs = stmt.executeQuery();
                    Map<String, Fruit> fruitMap = new HashMap<>();
                    while (rs.next()) {
                        String key = rs.getString("fruit_name") + ":" + rs.getString("source_location");
                        Fruit fruit = fruitMap.get(key);
                        if (fruit == null) {
                            fruit = new Fruit(
                                    rs.getInt("fruit_id"),
                                    rs.getString("fruit_name"),
                                    rs.getString("source_location"),
                                    rs.getString("country"),
                                    rs.getInt("stock_level")
                            );
                            fruitMap.put(key, fruit);
                            fruits.add(fruit);
                        }
                        fruit.addBranchStock(rs.getString("branch"), rs.getInt("stock_level"));
                    }

                    for (Fruit fruit : fruits) {
                        String branchSql = "SELECT branch FROM shops WHERE source_city = ? AND branch != ?";
                        try (PreparedStatement branchStmt = conn.prepareStatement(branchSql)) {
                            branchStmt.setString(1, fruit.getSourceCity());
                            branchStmt.setString(2, employeeBranch != null ? employeeBranch : "");
                            ResultSet branchRs = branchStmt.executeQuery();
                            List<String> availableBranches = new ArrayList<>();
                            while (branchRs.next()) {
                                String branchOption = branchRs.getString("branch");
                                if (!branchOption.equals(employeeBranch)) {
                                    availableBranches.add(branchOption);
                                }
                            }
                            fruit.setAvailableBranches(availableBranches);
                        }
                    }
                }

                request.setAttribute("fruits", fruits);
                request.setAttribute("countries", countries);
                request.setAttribute("sourceCities", sourceCities);
                request.setAttribute("branches", allBranches);
                request.setAttribute("selectedCountry", country);
                request.setAttribute("selectedSourceCity", sourceCity);
                request.setAttribute("selectedBranches", branches);
                request.setAttribute("minStock", minStock);
                request.setAttribute("maxStock", maxStock);
                request.getRequestDispatcher("borrowFruit.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            session.setAttribute("connectionStatus", "Database error: " + e.getMessage());
            request.setAttribute("error", "Failed to retrieve data: " + e.getMessage());
            System.out.println("Database error: " + e.getMessage());
            e.printStackTrace();
            request.getRequestDispatcher("error.jsp").forward(request, response);
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
            conn.setAutoCommit(false);

            String page = request.getParameter("page");
            if ("borrowFruit".equals(page)) {
                int fruitId = Integer.parseInt(request.getParameter("fruitId"));
                String lenderBranch = request.getParameter("branch");
                int quantity = Integer.parseInt(request.getParameter("quantity"));
                String borrowBranch = (String) session.getAttribute("employeeBranch");
                String borrowCity = (String) session.getAttribute("employeeCity");
                Integer employeeId = (Integer) session.getAttribute("employeeId");

                if (borrowBranch == null || borrowCity == null || employeeId == null) {
                    throw new SQLException("Employee branch, city, or ID not found. Please login as shopStaff.");
                }

                if (lenderBranch.equals(borrowBranch)) {
                    throw new SQLException("Borrow failed: Cannot borrow from the same branch (" + borrowBranch + ").");
                }

                String lenderCity = null;
                try (PreparedStatement stmt = conn.prepareStatement(
                        "SELECT source_city FROM shops WHERE branch = ?")) {
                    stmt.setString(1, lenderBranch);
                    ResultSet rs = stmt.executeQuery();
                    if (rs.next()) {
                        lenderCity = rs.getString("source_city");
                    }
                }
                if (lenderCity == null || !lenderCity.equals(borrowCity)) {
                    throw new SQLException("Borrow failed: Distance too far. Can only borrow from same city (" + borrowCity + ").");
                }

                String checkSql = "SELECT bi.stock_level, f.fruit_name, bi.source_city " +
                        "FROM branch_inventory bi JOIN fruits f ON bi.fruit_name = f.fruit_name AND bi.source_city = f.source_location " +
                        "WHERE f.fruit_id = ? AND bi.branch = ?";
                int availableStock = 0;
                String fruitName = null;
                String sourceCity = null;
                try (PreparedStatement stmt = conn.prepareStatement(checkSql)) {
                    stmt.setInt(1, fruitId);
                    stmt.setString(2, lenderBranch);
                    ResultSet rs = stmt.executeQuery();
                    if (rs.next()) {
                        availableStock = rs.getInt("stock_level");
                        fruitName = rs.getString("fruit_name");
                        sourceCity = rs.getString("source_city");
                    }
                }

                if (fruitName == null) {
                    throw new SQLException("Fruit not found in branch " + lenderBranch);
                }
                if (quantity <= 0 || quantity > availableStock) {
                    throw new SQLException("Invalid quantity or insufficient stock in branch " + lenderBranch);
                }

                // 不再直接更新庫存，等待審批通過後再更新
                String insertSql = "INSERT INTO borrow_records (borrow_branch, lender_branch, fruit_id, quantity, borrow_date, status) " +
                        "VALUES (?, ?, ?, ?, NOW(), 'PENDING')";
                try (PreparedStatement stmt = conn.prepareStatement(insertSql)) {
                    stmt.setString(1, borrowBranch);
                    stmt.setString(2, lenderBranch);
                    stmt.setInt(3, fruitId);
                    stmt.setInt(4, quantity);
                    stmt.executeUpdate();
                }

                conn.commit();
                request.setAttribute("success", "Borrow request for " + quantity + " " + fruitName + " from branch " + lenderBranch + " to " + borrowBranch + " submitted. Awaiting approval.");
            } else if ("reserveFruit".equals(page)) {
                int fruitId = Integer.parseInt(request.getParameter("fruitId"));
                int quantity = Integer.parseInt(request.getParameter("quantity"));
                String employeeBranch = (String) session.getAttribute("employeeBranch");
                String employeeCity = (String) session.getAttribute("employeeCity");
                Integer employeeId = (Integer) session.getAttribute("employeeId");

                if (employeeBranch == null || employeeCity == null || employeeId == null) {
                    throw new SQLException("Employee branch, city, or ID not found. Please login as shopStaff.");
                }

                String checkSql = "SELECT stock_level, fruit_name, source_location, country FROM fruits WHERE fruit_id = ?";
                int availableStock = 0;
                String fruitName = null;
                String sourceCity = null;
                String country = null;
                try (PreparedStatement stmt = conn.prepareStatement(checkSql)) {
                    stmt.setInt(1, fruitId);
                    ResultSet rs = stmt.executeQuery();
                    if (rs.next()) {
                        availableStock = rs.getInt("stock_level");
                        fruitName = rs.getString("fruit_name");
                        sourceCity = rs.getString("source_location");
                        country = rs.getString("country");
                    }
                }

                if (fruitName == null) {
                    throw new SQLException("Fruit not found with ID " + fruitId);
                }
                if (quantity <= 0 || quantity > availableStock) {
                    throw new SQLException("Invalid quantity or insufficient stock in Source City (" + sourceCity + ")");
                }

                // 不再直接更新庫存，等待審批通過後再更新
                String insertReserveSql = "INSERT INTO reserve_records (branch, fruit_id, quantity, source_city, reserve_date, status) " +
                                         "VALUES (?, ?, ?, ?, NOW(), 'PENDING')";
                try (PreparedStatement stmt = conn.prepareStatement(insertReserveSql)) {
                    stmt.setString(1, employeeBranch);
                    stmt.setInt(2, fruitId);
                    stmt.setInt(3, quantity);
                    stmt.setString(4, sourceCity);
                    stmt.executeUpdate();
                }

                conn.commit();
                request.setAttribute("success", "Reserve request for " + quantity + " " + fruitName + " from Source City " + sourceCity + " to branch " + employeeBranch + " submitted. Awaiting approval.");
            }

            doGet(request, response);
        } catch (SQLException e) {
            session.setAttribute("connectionStatus", "Database error: " + e.getMessage());
            request.setAttribute("error", e.getMessage());
            System.out.println("Database error in doPost: " + e.getMessage());
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    System.out.println("Error during rollback: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
            doGet(request, response);
        } catch (Exception e) {
            session.setAttribute("connectionStatus", "Unexpected error: " + e.getMessage());
            request.setAttribute("error", "Unexpected error: " + e.getMessage());
            System.out.println("Unexpected error in doPost: " + e.getMessage());
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    System.out.println("Error during rollback: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
            doGet(request, response);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    System.out.println("Error closing connection: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }
}