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

import com.aib.dao.warehouse.InventoryTransactionDAO;
import com.aib.dao.warehouse.impl.InventoryTransactionDAOImpl;
import com.aib.model.warehouse.InventoryTransaction;
import com.aib.model.warehouse.WarehouseStaff;

@WebServlet("/warehouse/transactions")
public class TransactionServlet extends HttpServlet {
    private InventoryTransactionDAO transactionDAO = new InventoryTransactionDAOImpl();
    
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
            String typeFilter = request.getParameter("type");
            List<InventoryTransaction> transactions;
            
            if (typeFilter != null && !typeFilter.isEmpty()) {
                transactions = transactionDAO.findByType(typeFilter);
                request.setAttribute("typeFilter", typeFilter);
            } else {
                // 获取该仓库的最近交易记录
                transactions = transactionDAO.findByWarehouse(staff.getStaffId());
            }
            
            request.setAttribute("transactions", transactions);
            
            // 转发到交易记录页面
            request.getRequestDispatcher("/warehouse/jsp/transactions.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("数据库操作错误", e);
        }
    }
} 