package com.aib.servlet;

import java.io.IOException;
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

        // 模擬驗證（未來與資料庫連接）
        boolean isValid = true; // 假設驗證成功

        if (isValid) {
            HttpSession session = request.getSession();
            session.setAttribute("email", email);
            session.setAttribute("role", role);

            // 根據角色跳轉到對應主頁
            if ("shopStaff".equals(role)) {
                response.sendRedirect("staffHome.jsp");
            } else if ("warehouseStaff".equals(role)) {
                response.sendRedirect("warehouseHome.jsp");
            } else if ("seniorManagement".equals(role)) {
                response.sendRedirect("managementHome.jsp");
            }
        } else {
            request.setAttribute("error", "Invalid email or password");
            request.getRequestDispatcher("login.jsp?role=" + role).forward(request, response);
        }
    }
}