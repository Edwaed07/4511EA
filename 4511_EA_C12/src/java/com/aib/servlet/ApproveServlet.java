package com.aib.servlet;

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

@WebServlet(name = "ApproveServlet", urlPatterns = {"/ApproveServlet"})
public class ApproveServlet extends HttpServlet {

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
                request.getRequestDispatcher("approve.jsp").forward(request, response);
                return;
            }

            
            List<Map<String, Object>> pendingBorrowRecords = new ArrayList<>();
            String borrowSql = "SELECT br.id, br.borrow_branch, br.lender_branch, br.fruit_id, br.quantity, br.borrow_date, br.status, f.name AS fruit_name, f.source_city " +
                              "FROM borrow_records br JOIN fruits f ON br.fruit_id = f.id " +
                              "WHERE br.lender_branch = ? AND br.status = 'PENDING'";
            try (PreparedStatement stmt = conn.prepareStatement(borrowSql)) {
                stmt.setString(1, employeeBranch);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    Map<String, Object> record = new HashMap<>();
                    record.put("id", rs.getInt("id"));
                    record.put("borrow_branch", rs.getString("borrow_branch"));
                    record.put("lender_branch", rs.getString("lender_branch"));
                    record.put("fruit_id", rs.getInt("fruit_id"));
                    record.put("fruit_name", rs.getString("fruit_name"));
                    record.put("source_city", rs.getString("source_city"));
                    record.put("quantity", rs.getInt("quantity"));
                    record.put("borrow_date", rs.getDate("borrow_date"));
                    record.put("status", rs.getString("status"));
                    pendingBorrowRecords.add(record);
                }
            }


            List<Map<String, Object>> myBorrowRecords = new ArrayList<>();
            String myBorrowSql = "SELECT br.id, br.borrow_branch, br.lender_branch, br.fruit_id, br.quantity, br.borrow_date, br.status, f.name AS fruit_name, f.source_city " +
                                "FROM borrow_records br JOIN fruits f ON br.fruit_id = f.id " +
                                "WHERE br.borrow_branch = ?";
            try (PreparedStatement stmt = conn.prepareStatement(myBorrowSql)) {
                stmt.setString(1, employeeBranch);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    Map<String, Object> record = new HashMap<>();
                    record.put("id", rs.getInt("id"));
                    record.put("borrow_branch", rs.getString("borrow_branch"));
                    record.put("lender_branch", rs.getString("lender_branch"));
                    record.put("fruit_id", rs.getInt("fruit_id"));
                    record.put("fruit_name", rs.getString("fruit_name"));
                    record.put("source_city", rs.getString("source_city"));
                    record.put("quantity", rs.getInt("quantity"));
                    record.put("borrow_date", rs.getDate("borrow_date"));
                    record.put("status", rs.getString("status"));
                    myBorrowRecords.add(record);
                }
            }


            List<Map<String, Object>> myReserveRecords = new ArrayList<>();
            String reserveSql = "SELECT rr.id, rr.branch, rr.fruit_id, rr.quantity, rr.source_city, rr.reserve_date, rr.status, f.name AS fruit_name " +
                               "FROM reserve_records rr JOIN fruits f ON rr.fruit_id = f.id " +
                               "WHERE rr.branch = ?";
            try (PreparedStatement stmt = conn.prepareStatement(reserveSql)) {
                stmt.setString(1, employeeBranch);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    Map<String, Object> record = new HashMap<>();
                    record.put("id", rs.getInt("id"));
                    record.put("branch", rs.getString("branch"));
                    record.put("fruit_id", rs.getInt("fruit_id"));
                    record.put("fruit_name", rs.getString("fruit_name"));
                    record.put("source_city", rs.getString("source_city"));
                    record.put("quantity", rs.getInt("quantity"));
                    record.put("reserve_date", rs.getDate("reserve_date"));
                    record.put("status", rs.getString("status"));
                    myReserveRecords.add(record);
                }
            }

            request.setAttribute("pendingBorrowRecords", pendingBorrowRecords);
            request.setAttribute("myBorrowRecords", myBorrowRecords);
            request.setAttribute("myReserveRecords", myReserveRecords);
            request.getRequestDispatcher("approve.jsp").forward(request, response);
        } catch (SQLException e) {
            session.setAttribute("connectionStatus", "Database error: " + e.getMessage());
            request.setAttribute("error", "Failed to retrieve data: " + e.getMessage());
            System.out.println("Database error: " + e.getMessage());
            e.printStackTrace();
            request.getRequestDispatcher("approve.jsp").forward(request, response);
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

            String employeeBranch = (String) session.getAttribute("employeeBranch");
            if (employeeBranch == null) {
                request.setAttribute("error", "Employee branch not found. Please login as shopStaff.");
                doGet(request, response);
                return;
            }

            String action = request.getParameter("action");
            int recordId = Integer.parseInt(request.getParameter("recordId"));
            String lenderBranch = request.getParameter("lenderBranch");
            String borrowBranch = request.getParameter("borrowBranch");
            int fruitId = Integer.parseInt(request.getParameter("fruitId"));
            String fruitName = request.getParameter("fruitName");
            String sourceCity = request.getParameter("sourceCity");
            int quantity = Integer.parseInt(request.getParameter("quantity"));

            if ("approve".equals(action)) {

                String checkSql = "SELECT bi.stock_level " +
                                 "FROM branch_inventory bi " +
                                 "WHERE bi.branch = ? AND bi.fruit_name = ? AND bi.source_city = ?";
                int availableStock = 0;
                try (PreparedStatement stmt = conn.prepareStatement(checkSql)) {
                    stmt.setString(1, lenderBranch);
                    stmt.setString(2, fruitName);
                    stmt.setString(3, sourceCity);
                    ResultSet rs = stmt.executeQuery();
                    if (rs.next()) {
                        availableStock = rs.getInt("stock_level");
                    } else {
                        throw new SQLException("Fruit not found in branch " + lenderBranch);
                    }
                }

                if (quantity <= 0 || quantity > availableStock) {
                    throw new SQLException("Invalid quantity or insufficient stock in branch " + lenderBranch);
                }


                String updateLenderSql = "UPDATE branch_inventory SET stock_level = stock_level - ? " +
                                        "WHERE branch = ? AND fruit_name = ? AND source_city = ?";
                try (PreparedStatement stmt = conn.prepareStatement(updateLenderSql)) {
                    stmt.setInt(1, quantity);
                    stmt.setString(2, lenderBranch);
                    stmt.setString(3, fruitName);
                    stmt.setString(4, sourceCity);
                    int rows = stmt.executeUpdate();
                    if (rows == 0) {
                        throw new SQLException("Failed to update lender branch inventory");
                    }
                }

                
                String fruitInfoSql = "SELECT name, source_city, country " +
                                     "FROM fruits " +
                                     "WHERE id = ?";
                String fruitCountry = null;
                String fruitSourceCity = null;
                String fruitNameFromDb = null;
                try (PreparedStatement stmt = conn.prepareStatement(fruitInfoSql)) {
                    stmt.setInt(1, fruitId);
                    ResultSet rs = stmt.executeQuery();
                    if (rs.next()) {
                        fruitNameFromDb = rs.getString("name");
                        fruitSourceCity = rs.getString("source_city");
                        fruitCountry = rs.getString("country");
                    } else {
                        throw new SQLException("Fruit not found with ID " + fruitId);
                    }
                }


                String upsertBorrowSql = "INSERT INTO branch_inventory (branch, fruit_name, source_city, stock_level, country) " +
                                        "VALUES (?, ?, ?, ?, ?) " +
                                        "ON DUPLICATE KEY UPDATE stock_level = stock_level + ?";
                try (PreparedStatement stmt = conn.prepareStatement(upsertBorrowSql)) {
                    stmt.setString(1, borrowBranch);
                    stmt.setString(2, fruitNameFromDb);
                    stmt.setString(3, fruitSourceCity);
                    stmt.setInt(4, quantity);
                    stmt.setString(5, fruitCountry);
                    stmt.setInt(6, quantity);
                    stmt.executeUpdate();
                }


                String updateStatusSql = "UPDATE borrow_records SET status = 'DELIVERY' WHERE id = ? AND lender_branch = ? AND status = 'PENDING'";
                try (PreparedStatement stmt = conn.prepareStatement(updateStatusSql)) {
                    stmt.setInt(1, recordId);
                    stmt.setString(2, employeeBranch);
                    int rows = stmt.executeUpdate();
                    if (rows > 0) {
                        request.setAttribute("success", "Borrow request approved successfully. Status updated to DELIVERY.");
                    } else {
                        throw new SQLException("Failed to approve borrow request. It may have already been processed.");
                    }
                }
            } else if ("reject".equals(action)) {

                String updateStatusSql = "UPDATE borrow_records SET status = 'REJECTED' WHERE id = ? AND lender_branch = ? AND status = 'PENDING'";
                try (PreparedStatement stmt = conn.prepareStatement(updateStatusSql)) {
                    stmt.setInt(1, recordId);
                    stmt.setString(2, employeeBranch);
                    int rows = stmt.executeUpdate();
                    if (rows > 0) {
                        request.setAttribute("success", "Borrow request rejected successfully.");
                    } else {
                        throw new SQLException("Failed to reject borrow request. It may have already been processed.");
                    }
                }
            }

            conn.commit();
            doGet(request, response);
        } catch (SQLException e) {
            session.setAttribute("connectionStatus", "Database error: " + e.getMessage());
            request.setAttribute("error", "Failed to process request: " + e.getMessage());
            System.out.println("Database error: " + e.getMessage());
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
            System.out.println("Unexpected error: " + e.getMessage());
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