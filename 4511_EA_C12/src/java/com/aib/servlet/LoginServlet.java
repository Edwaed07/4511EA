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

@WebServlet(name = "LoginServlet", urlPatterns = {"/LoginServlet"})
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String role = request.getParameter("role");


        if (email == null || email.isEmpty() || password == null || password.isEmpty() || role == null || role.isEmpty()) {
            request.setAttribute("error", "Email, password, and role are required.");
            request.getRequestDispatcher("login.jsp?role=" + role).forward(request, response);
            return;
        }


        String dbRole;
        switch (role) {
            case "warehouseStaff":
                dbRole = "warehouseStaff";
                break;
            case "seniorManager":
                dbRole = "seniorManager";
                break;
            case "shopStaff":
                dbRole = "shopStaff";
                break;
            default:
                dbRole = role; 
                break;
        }

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();

            String sql = "SELECT e.id, e.name, e.email, e.role, e.branch, s.source_city " +
                        "FROM employees e LEFT JOIN shops s ON e.branch = s.branch " +
                        "WHERE e.email = ? AND e.password = ? AND e.role = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, email);
                stmt.setString(2, password);
                stmt.setString(3, dbRole);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    HttpSession session = request.getSession();
                    session.setAttribute("employeeId", rs.getInt("id"));
                    session.setAttribute("name", rs.getString("name"));
                    session.setAttribute("email", rs.getString("email"));
                    session.setAttribute("role", rs.getString("role"));
                    session.setAttribute("employeeBranch", rs.getString("branch"));
                    session.setAttribute("employeeCity", rs.getString("source_city"));


                    session.setAttribute("message", "Login successful");
                    session.setAttribute("messageType", "success");

          
                    if ("warehouseStaff".equals(dbRole)) {
                        response.sendRedirect("warehouseHome.jsp");
                    } else if ("seniorManager".equals(dbRole)) {
                        response.sendRedirect("SeniorManagementHome.jsp");
                    } else if ("shopStaff".equals(dbRole)) {
                        response.sendRedirect("staffHome.jsp");
                    } else {
                        response.sendRedirect("staffHome.jsp"); 
                    }
                } else {
                    request.setAttribute("error", "Invalid email, password, or role.");
                    request.getRequestDispatcher("login.jsp?role=" + role).forward(request, response);
                }
            }
        } catch (SQLException e) {
            request.setAttribute("error", "Database error: " + e.getMessage());
            request.getRequestDispatcher("login.jsp?role=" + role).forward(request, response);
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