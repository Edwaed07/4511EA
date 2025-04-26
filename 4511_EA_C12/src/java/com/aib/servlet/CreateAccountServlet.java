package com.aib.servlet;

import com.aib.util.DatabaseConnection;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "CreateAccountServlet", urlPatterns = {"/CreateAccountServlet"})
public class CreateAccountServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("createAccount.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String name = request.getParameter("name");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String branch = request.getParameter("branch");
        String role = request.getParameter("role");
        
        if (role == null || role.isEmpty()) {
            role = "shopStaff"; // 默认角色
        }

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            if (!password.equals(confirmPassword)) {
                request.setAttribute("error", "Passwords do not match.");
                request.getRequestDispatcher("createAccount.jsp?role=" + role).forward(request, response);
                return;
            }

            String checkSql = "SELECT email FROM employees WHERE email = ?";
            try (PreparedStatement stmt = conn.prepareStatement(checkSql)) {
                stmt.setString(1, email);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    request.setAttribute("error", "Email already exists.");
                    request.getRequestDispatcher("createAccount.jsp?role=" + role).forward(request, response);
                    return;
                }
            }

            String insertSql = "INSERT INTO employees (email, name, password, role, branch) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(insertSql)) {
                stmt.setString(1, email);
                stmt.setString(2, name);
                stmt.setString(3, password);
                stmt.setString(4, role);
                stmt.setString(5, branch);
                int result = stmt.executeUpdate();
                
                if (result > 0) {
                    System.out.println("Account created successfully for " + email + " with role " + role);
                } else {
                    System.out.println("Failed to create account");
                }
            }

            response.sendRedirect("login.jsp?role=" + role);
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Database error: " + e.getMessage());
            request.getRequestDispatcher("createAccount.jsp?role=" + role).forward(request, response);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}