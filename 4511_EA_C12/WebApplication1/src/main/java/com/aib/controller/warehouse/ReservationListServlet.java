package com.aib.controller.warehouse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.aib.dao.warehouse.ReservationDAO;
import com.aib.dao.warehouse.impl.ReservationDAOImpl;
import com.aib.model.warehouse.Reservation;
import com.aib.model.warehouse.WarehouseStaff;

@WebServlet("/warehouse/reservations")
public class ReservationListServlet extends HttpServlet {
    private ReservationDAO reservationDAO = new ReservationDAOImpl();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 检查用户是否已登录
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("warehouseStaff") == null) {
            response.sendRedirect(request.getContextPath() + "/warehouse/login");
            return;
        }
        
        WarehouseStaff staff = (WarehouseStaff) session.getAttribute("warehouseStaff");
        request.setAttribute("staff", staff);
        
        try {
            String statusFilter = request.getParameter("status");
            List<Reservation> reservations;
            
            if (statusFilter != null && !statusFilter.isEmpty()) {
                reservations = reservationDAO.findByStatus(statusFilter);
                request.setAttribute("statusFilter", statusFilter);
            } else {
                // 获取该仓库的所有预订
                reservations = reservationDAO.findByWarehouse(staff.getStaffId());
            }
            
            request.setAttribute("reservations", reservations);
            
            // 转发到预订列表页面
            request.getRequestDispatcher("/warehouse/jsp/reservations.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("数据库操作错误", e);
        }
    }
} 