package com.aib.controller.warehouse;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.aib.dao.warehouse.WarehouseStaffDAO;
import com.aib.dao.warehouse.impl.WarehouseStaffDAOImpl;
import com.aib.model.warehouse.WarehouseStaff;

@WebServlet("/warehouse/login")
public class LoginServlet extends HttpServlet {
    private WarehouseStaffDAO staffDAO = new WarehouseStaffDAOImpl();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 检查用户是否已登录
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("warehouseStaff") != null) {
            // 如果已登录，重定向到仪表板
            response.sendRedirect(request.getContextPath() + "/warehouse/dashboard");
            return;
        }
        
        // 未登录，显示登录页面
        request.getRequestDispatcher("/warehouse/jsp/login.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        // 简单的表单验证
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            request.setAttribute("error", "用户名和密码不能为空");
            request.getRequestDispatcher("/warehouse/jsp/login.jsp").forward(request, response);
            return;
        }
        
        try {
            // 尝试认证
            if (staffDAO.authenticate(username, password)) {
                // 认证成功，获取员工信息并存储在会话中
                WarehouseStaff staff = staffDAO.findByUsername(username);
                HttpSession session = request.getSession();
                session.setAttribute("warehouseStaff", staff);
                
                // 重定向到仪表板
                response.sendRedirect(request.getContextPath() + "/warehouse/dashboard");
            } else {
                // 认证失败
                request.setAttribute("error", "用户名或密码不正确");
                request.getRequestDispatcher("/warehouse/jsp/login.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            // 数据库错误
            request.setAttribute("error", "系统错误：" + e.getMessage());
            request.getRequestDispatcher("/warehouse/jsp/login.jsp").forward(request, response);
        }
    }
} 