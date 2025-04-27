package com.aib.controller.warehouse;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.aib.model.warehouse.WarehouseStaff;

@WebServlet("/warehouse/dashboard")
public class DashboardServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 检查用户是否已登录
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("warehouseStaff") == null) {
            // 如果未登录，重定向到登录页面
            response.sendRedirect(request.getContextPath() + "/warehouse/login");
            return;
        }
        
        // 获取登录的仓库员工信息
        WarehouseStaff staff = (WarehouseStaff) session.getAttribute("warehouseStaff");
        request.setAttribute("staff", staff);
        
        // 转发到仪表板页面
        request.getRequestDispatcher("/warehouse/jsp/dashboard.jsp").forward(request, response);
    }
} 