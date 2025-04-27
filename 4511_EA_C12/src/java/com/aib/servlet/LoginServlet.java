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

        // 驗證輸入參數
        if (email == null || email.isEmpty() || password == null || password.isEmpty() || role == null || role.isEmpty()) {
            request.setAttribute("error", "Email, password, and role are required.");
            request.getRequestDispatcher("login.jsp?role=" + role).forward(request, response);
            return;
        }

        // 修正角色映射邏輯，與 index.jsp 傳入的值匹配
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
                dbRole = role; // 如果角色名稱未匹配，保持原樣
                break;
        }

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            // 使用 LEFT JOIN 確保即使 branch 為 NULL 也能查詢到記錄
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

                    // 設置 "Login successful" 訊息
                    session.setAttribute("message", "Login successful");
                    session.setAttribute("messageType", "success");

                    // 根據角色導向不同頁面
                    if ("warehouseStaff".equals(dbRole)) {
                        response.sendRedirect("warehouseHome.jsp");
                    } else if ("seniorManager".equals(dbRole)) {
                        response.sendRedirect("managementDashboard.jsp");
                    } else if ("shopStaff".equals(dbRole)) {
                        response.sendRedirect("staffHome.jsp");
                    } else {
                        response.sendRedirect("staffHome.jsp"); // 默認導向
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