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
import javax.servlet.http.HttpSession;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String role = request.getParameter("role");

        // 打印日誌以便排查
        System.out.println("Attempting to login with email: " + email + ", role: " + role);

        // 清除舊的 Session
        HttpSession session = request.getSession();
        session.invalidate();
        session = request.getSession(true);

        try (Connection conn = DatabaseConnection.getConnection()) {
            System.out.println("Database connection established successfully.");

            try (PreparedStatement stmt = conn.prepareStatement(
                    "SELECT * FROM employees WHERE email = ? AND password = ? AND role = ?")) {
                stmt.setString(1, email);
                stmt.setString(2, password);
                stmt.setString(3, role);
                ResultSet rs = stmt.executeQuery();

                System.out.println("SQL query executed: SELECT * FROM employees WHERE email = '" + email + "' AND role = '" + role + "'");

                if (rs.next()) {
                    System.out.println("User found in database. Logging in...");
                    session.setAttribute("email", email);
                    session.setAttribute("role", role);
                    session.setAttribute("shopId", rs.getInt("shop_id"));

                    if ("shopStaff".equals(role)) {
                        response.sendRedirect("staffHome.jsp");
                    } else if ("warehouseStaff".equals(role)) {
                        response.sendRedirect("warehouseHome.jsp");
                    } else if ("seniorManagement".equals(role)) {
                        response.sendRedirect("managementHome.jsp");
                    } else {
                        System.out.println("Invalid role: " + role);
                        request.setAttribute("error", "Invalid role");
                        request.getRequestDispatcher("login.jsp?role=" + role).forward(request, response);
                    }
                } else {
                    System.out.println("No user found in database. Login failed.");
                    request.setAttribute("error", "Invalid email, password, or role");
                    request.getRequestDispatcher("login.jsp?role=" + role).forward(request, response);
                }
            }
        } catch (SQLException e) {
            System.out.println("Database error occurred: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Database error: " + e.getMessage());
            request.getRequestDispatcher("login.jsp?role=" + role).forward(request, response);
        }
    }
}