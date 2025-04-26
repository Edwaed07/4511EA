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

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            
            // 倉庫員工的登錄處理
            if ("warehouseStaff".equals(role)) {
                String sql = "SELECT e.id, e.name, e.email, e.role " +
                           "FROM employees e " +
                           "WHERE e.email = ? AND e.password = ? AND e.role = 'Warehouse Staff'";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, email);
                    stmt.setString(2, password);
                    ResultSet rs = stmt.executeQuery();
                    if (rs.next()) {
                        HttpSession session = request.getSession();
                        session.setAttribute("employeeId", rs.getInt("id"));
                        session.setAttribute("name", rs.getString("name"));
                        session.setAttribute("email", rs.getString("email"));
                        session.setAttribute("role", rs.getString("role"));
                        session.setAttribute("userId", rs.getInt("id")); // 添加userId屬性以兼容存在代碼
                        response.sendRedirect("WarehouseServlet?action=warehouseHome");
                        return;
                    } else {
                        request.setAttribute("error", "無效的電子郵件、密碼或角色。");
                        request.getRequestDispatcher("login.jsp?role=" + role).forward(request, response);
                        return;
                    }
                }
            }
            
            // 商店員工的登錄處理
            if ("shopStaff".equals(role)) {
                String sql = "SELECT e.id, e.name, e.email, e.role, e.branch, s.source_city " +
                        "FROM employees e JOIN shops s ON e.branch = s.branch " +
                        "WHERE e.email = ? AND e.password = ? AND e.role = 'Shop Staff'";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, email);
                    stmt.setString(2, password);
                    ResultSet rs = stmt.executeQuery();
                    if (rs.next()) {
                        HttpSession session = request.getSession();
                        session.setAttribute("employeeId", rs.getInt("id"));
                        session.setAttribute("name", rs.getString("name"));
                        session.setAttribute("email", rs.getString("email"));
                        session.setAttribute("role", rs.getString("role"));
                        session.setAttribute("employeeBranch", rs.getString("branch"));
                        session.setAttribute("employeeCity", rs.getString("source_city"));
                        session.setAttribute("userId", rs.getInt("id")); // 添加userId屬性以兼容存在代碼
                        response.sendRedirect("staffHome.jsp");
                        return;
                    }
                }
            }
            
            // 管理員登錄處理
            if ("admin".equals(role)) {
                String sql = "SELECT id, name, email, role FROM employees " +
                           "WHERE email = ? AND password = ? AND role = 'Senior Management'";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, email);
                    stmt.setString(2, password);
                    ResultSet rs = stmt.executeQuery();
                    if (rs.next()) {
                        HttpSession session = request.getSession();
                        session.setAttribute("adminId", rs.getInt("id"));
                        session.setAttribute("name", rs.getString("name"));
                        session.setAttribute("email", rs.getString("email"));
                        session.setAttribute("role", rs.getString("role"));
                        session.setAttribute("userId", rs.getInt("id")); // 添加userId屬性以兼容存在代碼
                        response.sendRedirect("adminHome.jsp");
                        return;
                    }
                }
            }
            
            // 如果所有登錄嘗試都失敗
            request.setAttribute("error", "無效的電子郵件、密碼或角色。");
            request.getRequestDispatcher("login.jsp?role=" + role).forward(request, response);
            
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "數據庫錯誤: " + e.getMessage());
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