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

@WebServlet(name = "CheckReserveServlet", urlPatterns = {"/CheckReserveServlet"})
public class CheckReserveServlet extends HttpServlet {

    // 借貨記錄類
    public static class BorrowRecord {
        private String fruitName;
        private String borrowBranch;
        private String lenderBranch;
        private int quantity;
        private String borrowDate;
        private String status; // 新增 status 欄位

        public BorrowRecord(String fruitName, String borrowBranch, String lenderBranch, int quantity, String borrowDate, String status) {
            this.fruitName = fruitName;
            this.borrowBranch = borrowBranch;
            this.lenderBranch = lenderBranch;
            this.quantity = quantity;
            this.borrowDate = borrowDate;
            this.status = status;
        }

        public String getFruitName() { return fruitName; }
        public void setFruitName(String fruitName) { this.fruitName = fruitName; }

        public String getBorrowBranch() { return borrowBranch; }
        public void setBorrowBranch(String borrowBranch) { this.borrowBranch = borrowBranch; }

        public String getLenderBranch() { return lenderBranch; }
        public void setLenderBranch(String lenderBranch) { this.lenderBranch = lenderBranch; }

        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }

        public String getBorrowDate() { return borrowDate; }
        public void setBorrowDate(String borrowDate) { this.borrowDate = borrowDate; }

        public String getStatus() { return status; } // 新增 getter
        public void setStatus(String status) { this.status = status; } // 新增 setter
    }

    // 訂貨記錄類
    public static class ReserveRecord {
        private String fruitName;
        private String branch;
        private String sourceCity;
        private int quantity;
        private String reserveDate;
        private String status; // 新增 status 欄位

        public ReserveRecord(String fruitName, String branch, String sourceCity, int quantity, String reserveDate, String status) {
            this.fruitName = fruitName;
            this.branch = branch;
            this.sourceCity = sourceCity;
            this.quantity = quantity;
            this.reserveDate = reserveDate;
            this.status = status;
        }

        public String getFruitName() { return fruitName; }
        public void setFruitName(String fruitName) { this.fruitName = fruitName; }

        public String getBranch() { return branch; }
        public void setBranch(String branch) { this.branch = branch; }

        public String getSourceCity() { return sourceCity; }
        public void setSourceCity(String sourceCity) { this.sourceCity = sourceCity; }

        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }

        public String getReserveDate() { return reserveDate; }
        public void setReserveDate(String reserveDate) { this.reserveDate = reserveDate; }

        public String getStatus() { return status; } // 新增 getter
        public void setStatus(String status) { this.status = status; } // 新增 setter
    }

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
            System.out.println("Employee Branch: " + employeeBranch);
            if (employeeBranch == null) {
                request.setAttribute("error", "Employee branch not found. Please login as shopStaff.");
                request.getRequestDispatcher("checkReserve.jsp").forward(request, response);
                return;
            }

            // 獲取篩選參數
            String filter = request.getParameter("filter");
            if (filter == null || filter.isEmpty()) {
                filter = "both";
            }

            // 查詢借貨記錄
            List<BorrowRecord> borrowRecords = new ArrayList<>();
            if (!filter.equals("reserve")) {
                String borrowSql = "SELECT f.fruit_name AS fruit_name, br.borrow_branch, br.lender_branch, br.quantity, br.borrow_date, br.status " +
                                  "FROM borrow_records br " +
                                  "JOIN fruits f ON br.fruit_id = f.fruit_id " +
                                  "WHERE br.borrow_branch = ? OR br.lender_branch = ?";
                try (PreparedStatement stmt = conn.prepareStatement(borrowSql)) {
                    stmt.setString(1, employeeBranch);
                    stmt.setString(2, employeeBranch);
                    ResultSet rs = stmt.executeQuery();
                    while (rs.next()) {
                        borrowRecords.add(new BorrowRecord(
                            rs.getString("fruit_name"),
                            rs.getString("borrow_branch"),
                            rs.getString("lender_branch"),
                            rs.getInt("quantity"),
                            rs.getString("borrow_date"),
                            rs.getString("status") // 新增 status
                        ));
                    }
                    System.out.println("Borrow Records found: " + borrowRecords.size());
                }
            }
            request.setAttribute("borrowRecords", borrowRecords);

            // 查詢訂貨記錄
            List<ReserveRecord> reserveRecords = new ArrayList<>();
            if (!filter.equals("borrow")) {
                String reserveSql = "SELECT f.fruit_name AS fruit_name, rr.branch, rr.source_city, rr.quantity, rr.reserve_date, rr.status " +
                                   "FROM reserve_records rr " +
                                   "JOIN fruits f ON rr.fruit_id = f.fruit_id " +
                                   "WHERE rr.branch = ?";
                try (PreparedStatement stmt = conn.prepareStatement(reserveSql)) {
                    stmt.setString(1, employeeBranch);
                    ResultSet rs = stmt.executeQuery();
                    while (rs.next()) {
                        reserveRecords.add(new ReserveRecord(
                            rs.getString("fruit_name"),
                            rs.getString("branch"),
                            rs.getString("source_city"),
                            rs.getInt("quantity"),
                            rs.getString("reserve_date"),
                            rs.getString("status") // 新增 status
                        ));
                    }
                    System.out.println("Reserve Records found: " + reserveRecords.size());
                }
            }
            request.setAttribute("reserveRecords", reserveRecords);

            request.getRequestDispatcher("checkReserve.jsp").forward(request, response);
        } catch (SQLException e) {
            session.setAttribute("connectionStatus", "Database error: " + e.getMessage());
            request.setAttribute("error", "Failed to retrieve data: " + e.getMessage());
            System.out.println("Database error: " + e.getMessage());
            e.printStackTrace();
            request.getRequestDispatcher("checkReserve.jsp").forward(request, response);
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
        doGet(request, response);
    }
}