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

    public static class ReserveRecord {
        private String fruitName;
        private String borrowBranch;
        private String lenderBranch;
        private int quantity;
        private String borrowDate;

        public ReserveRecord(String fruitName, String borrowBranch, String lenderBranch, int quantity, String borrowDate) {
            this.fruitName = fruitName;
            this.borrowBranch = borrowBranch;
            this.lenderBranch = lenderBranch;
            this.quantity = quantity;
            this.borrowDate = borrowDate;
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

            List<ReserveRecord> reserveRecords = new ArrayList<>();
            String sql = "SELECT f.name AS fruit_name, br.borrow_branch, br.lender_branch, br.quantity, br.borrow_date " +
                        "FROM borrow_records br " +
                        "JOIN fruits f ON br.fruit_id = f.id " +
                        "WHERE br.borrow_branch = ? OR br.lender_branch = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, employeeBranch);
                stmt.setString(2, employeeBranch);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    reserveRecords.add(new ReserveRecord(
                        rs.getString("fruit_name"),
                        rs.getString("borrow_branch"),
                        rs.getString("lender_branch"),
                        rs.getInt("quantity"),
                        rs.getString("borrow_date")
                    ));
                }
                System.out.println("Reserve Records found: " + reserveRecords.size());
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