package com.aib.servlet;

import com.aib.model.Fruit;
import com.aib.util.DatabaseConnection;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "FruitServlet", urlPatterns = {"/FruitServlet"})
public class FruitServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String page = request.getParameter("page");
        if (page == null || page.isEmpty()) {
            page = "fruitList";
        }

        HttpSession session = request.getSession();
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            session.setAttribute("connectionStatus", "Connected to database successfully");
            System.out.println("Connected to database: " + conn.getCatalog());

            Integer employeeId = (Integer) session.getAttribute("employeeId");
            String employeeBranch = null;
            String employeeCity = null;
            if (employeeId != null) {
                try (PreparedStatement stmt = conn.prepareStatement(
                        "SELECT e.branch, s.source_city FROM employees e JOIN shops s ON e.branch = s.branch WHERE e.id = ?")) {
                    stmt.setInt(1, employeeId);
                    ResultSet rs = stmt.executeQuery();
                    if (rs.next()) {
                        employeeBranch = rs.getString("branch");
                        employeeCity = rs.getString("source_city");
                    }
                }
                session.setAttribute("employeeBranch", employeeBranch);
                session.setAttribute("employeeCity", employeeCity);
            }

            if (page.equals("borrowFruit")) {
                String sourceCity = request.getParameter("sourceCity");
                String branch = request.getParameter("branch");
                String country = request.getParameter("country");
                String minStock = request.getParameter("minStock");
                String maxStock = request.getParameter("maxStock");

                List<String> sourceCities = new ArrayList<>();
                try (PreparedStatement stmt = conn.prepareStatement("SELECT DISTINCT source_city FROM branch_inventory ORDER BY source_city")) {
                    ResultSet rs = stmt.executeQuery();
                    while (rs.next()) {
                        sourceCities.add(rs.getString("source_city"));
                    }
                }

                List<String> branches = new ArrayList<>();
                try (PreparedStatement stmt = conn.prepareStatement("SELECT DISTINCT branch FROM branch_inventory ORDER BY branch")) {
                    ResultSet rs = stmt.executeQuery();
                    while (rs.next()) {
                        branches.add(rs.getString("branch"));
                    }
                }

                List<String> countries = new ArrayList<>();
                try (PreparedStatement stmt = conn.prepareStatement("SELECT DISTINCT f.country FROM fruits f JOIN branch_inventory bi ON f.name = bi.fruit_name ORDER BY f.country")) {
                    ResultSet rs = stmt.executeQuery();
                    while (rs.next()) {
                        countries.add(rs.getString("country"));
                    }
                }

                List<Fruit> fruits = new ArrayList<>();
                StringBuilder sql = new StringBuilder(
                        "SELECT bi.fruit_name, bi.source_city, f.country, bi.branch, bi.stock_level, f.id " +
                        "FROM branch_inventory bi JOIN fruits f ON bi.fruit_name = f.name AND bi.source_city = f.source_city WHERE 1=1");
                List<Object> params = new ArrayList<>();

                if (sourceCity != null && !sourceCity.isEmpty()) {
                    sql.append(" AND bi.source_city = ?");
                    params.add(sourceCity);
                }
                if (branch != null && !branch.isEmpty()) {
                    sql.append(" AND bi.branch = ?");
                    params.add(branch);
                }
                if (country != null && !country.isEmpty()) {
                    sql.append(" AND f.country = ?");
                    params.add(country);
                }
                if (minStock != null && !minStock.isEmpty()) {
                    sql.append(" AND bi.stock_level >= ?");
                    params.add(Integer.parseInt(minStock));
                }
                if (maxStock != null && !maxStock.isEmpty()) {
                    sql.append(" AND bi.stock_level <= ?");
                    params.add(Integer.parseInt(maxStock));
                }

                try (PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
                    for (int i = 0; i < params.size(); i++) {
                        stmt.setObject(i + 1, params.get(i));
                    }
                    ResultSet rs = stmt.executeQuery();
                    Map<String, Fruit> fruitMap = new HashMap<>();
                    while (rs.next()) {
                        String key = rs.getString("fruit_name") + ":" + rs.getString("source_city");
                        Fruit fruit = fruitMap.get(key);
                        if (fruit == null) {
                            fruit = new Fruit(
                                    rs.getInt("id"),
                                    rs.getString("fruit_name"),
                                    rs.getString("source_city"),
                                    rs.getString("country"),
                                    0
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
                                if (!branchOption.equals(branch)) {
                                    availableBranches.add(branchOption);
                                }
                            }
                            fruit.setAvailableBranches(availableBranches);
                        }
                    }
                }

                request.setAttribute("fruits", fruits);
                request.setAttribute("sourceCities", sourceCities);
                request.setAttribute("branches", branches);
                request.setAttribute("countries", countries);
                request.setAttribute("selectedSourceCity", sourceCity);
                request.setAttribute("selectedBranch", branch);
                request.setAttribute("selectedCountry", country);
                request.setAttribute("minStock", minStock);
                request.setAttribute("maxStock", maxStock);
                request.getRequestDispatcher("borrowFruit.jsp").forward(request, response);
            } else {
                List<Fruit> fruits = new ArrayList<>();
                try (PreparedStatement stmt = conn.prepareStatement(
                        "SELECT id, name, source_city, country, stock_level FROM fruits")) {
                    ResultSet rs = stmt.executeQuery();
                    while (rs.next()) {
                        fruits.add(new Fruit(
                                rs.getInt("id"),
                                rs.getString("name"),
                                rs.getString("source_city"),
                                rs.getString("country"),
                                rs.getInt("stock_level")
                        ));
                    }
                }
                request.setAttribute("fruits", fruits);
                request.getRequestDispatcher("fruitList.jsp").forward(request, response);
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

                String checkSql = "SELECT bi.stock_level, f.name, bi.source_city " +
                        "FROM branch_inventory bi JOIN fruits f ON bi.fruit_name = f.name AND bi.source_city = f.source_city " +
                        "WHERE f.id = ? AND bi.branch = ?";
                int availableStock = 0;
                String fruitName = null;
                String sourceCity = null;
                try (PreparedStatement stmt = conn.prepareStatement(checkSql)) {
                    stmt.setInt(1, fruitId);
                    stmt.setString(2, lenderBranch);
                    ResultSet rs = stmt.executeQuery();
                    if (rs.next()) {
                        availableStock = rs.getInt("stock_level");
                        fruitName = rs.getString("name");
                        sourceCity = rs.getString("source_city");
                    }
                }

                if (fruitName == null) {
                    throw new SQLException("Fruit not found in branch " + lenderBranch);
                }
                if (quantity <= 0 || quantity > availableStock) {
                    throw new SQLException("Invalid quantity or insufficient stock in branch " + lenderBranch);
                }

                String updateSql = "UPDATE branch_inventory SET stock_level = stock_level - ? " +
                        "WHERE fruit_name = ? AND source_city = ? AND branch = ?";
                try (PreparedStatement stmt = conn.prepareStatement(updateSql)) {
                    stmt.setInt(1, quantity);
                    stmt.setString(2, fruitName);
                    stmt.setString(3, sourceCity);
                    stmt.setString(4, lenderBranch);
                    int rows = stmt.executeUpdate();
                    if (rows == 0) {
                        throw new SQLException("Failed to update branch inventory");
                    }
                }

                String insertSql = "INSERT INTO borrow_records (borrow_branch, lender_branch, fruit_id, quantity, borrow_date) " +
                        "VALUES (?, ?, ?, ?, NOW())";
                try (PreparedStatement stmt = conn.prepareStatement(insertSql)) {
                    stmt.setString(1, borrowBranch);
                    stmt.setString(2, lenderBranch);
                    stmt.setInt(3, fruitId);
                    stmt.setInt(4, quantity);
                    stmt.executeUpdate();
                }

                conn.commit();
                request.setAttribute("success", "Borrowed " + quantity + " " + fruitName + " from branch " + lenderBranch + " to " + borrowBranch);
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