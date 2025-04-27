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

import com.aib.dao.warehouse.BorrowingDAO;
import com.aib.dao.warehouse.impl.BorrowingDAOImpl;
import com.aib.model.warehouse.Borrowing;
import com.aib.model.warehouse.WarehouseStaff;

@WebServlet("/warehouse/borrowings")
public class BorrowingListServlet extends HttpServlet {
    private BorrowingDAO borrowingDAO = new BorrowingDAOImpl();
    
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
            List<Borrowing> borrowings;
            
            if (statusFilter != null && !statusFilter.isEmpty()) {
                borrowings = borrowingDAO.findByStatus(statusFilter);
                request.setAttribute("statusFilter", statusFilter);
            } else {
                // 获取该仓库的所有借用记录
                borrowings = borrowingDAO.findByWarehouse(staff.getStaffId());
            }
            
            // 获取逾期的借用记录
            List<Borrowing> overdueBorrowings = borrowingDAO.findOverdueBorrowings();
            request.setAttribute("overdueBorrowings", overdueBorrowings);
            
            request.setAttribute("borrowings", borrowings);
            
            // 转发到借用列表页面
            request.getRequestDispatcher("/warehouse/jsp/borrowings.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("数据库操作错误", e);
        }
    }
} 