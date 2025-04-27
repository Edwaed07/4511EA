package com.aib.controller.warehouse;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.aib.dao.warehouse.BorrowingDAO;
import com.aib.dao.warehouse.InventoryDAO;
import com.aib.dao.warehouse.impl.BorrowingDAOImpl;
import com.aib.dao.warehouse.impl.InventoryDAOImpl;
import com.aib.model.warehouse.Borrowing;
import com.aib.model.warehouse.Inventory;
import com.aib.model.warehouse.WarehouseStaff;

@WebServlet("/warehouse/borrowing-return")
public class BorrowingReturnServlet extends HttpServlet {
    private BorrowingDAO borrowingDAO = new BorrowingDAOImpl();
    private InventoryDAO inventoryDAO = new InventoryDAOImpl();
    
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
        
        // 获取借用ID
        String borrowingIdParam = request.getParameter("id");
        if (borrowingIdParam == null || borrowingIdParam.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/warehouse/borrowings");
            return;
        }
        
        try {
            int borrowingId = Integer.parseInt(borrowingIdParam);
            
            // 获取借用信息
            Borrowing borrowing = borrowingDAO.findById(borrowingId);
            if (borrowing == null) {
                request.setAttribute("error", "借用记录不存在");
                request.getRequestDispatcher("/warehouse/jsp/borrowings.jsp").forward(request, response);
                return;
            }
            
            // 只能处理已借出的记录
            if (!"borrowed".equals(borrowing.getStatus())) {
                request.setAttribute("error", "只能归还已借出的水果");
                request.getRequestDispatcher("/warehouse/jsp/borrowings.jsp").forward(request, response);
                return;
            }
            
            request.setAttribute("borrowing", borrowing);
            
            // 转发到归还页面
            request.getRequestDispatcher("/warehouse/jsp/borrowing-return.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/warehouse/borrowings");
        } catch (SQLException e) {
            throw new ServletException("数据库操作错误", e);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 检查用户是否已登录
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("warehouseStaff") == null) {
            response.sendRedirect(request.getContextPath() + "/warehouse/login");
            return;
        }
        
        WarehouseStaff staff = (WarehouseStaff) session.getAttribute("warehouseStaff");
        
        // 获取参数
        String borrowingIdParam = request.getParameter("borrowingId");
        String returnCondition = request.getParameter("returnCondition");
        String remarks = request.getParameter("remarks");
        
        if (borrowingIdParam == null || borrowingIdParam.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/warehouse/borrowings");
            return;
        }
        
        try {
            int borrowingId = Integer.parseInt(borrowingIdParam);
            
            // 获取借用信息
            Borrowing borrowing = borrowingDAO.findById(borrowingId);
            if (borrowing == null) {
                request.setAttribute("error", "借用记录不存在");
                request.getRequestDispatcher("/warehouse/jsp/borrowings.jsp").forward(request, response);
                return;
            }
            
            // 只能处理已借出的记录
            if (!"borrowed".equals(borrowing.getStatus())) {
                request.setAttribute("error", "只能归还已借出的水果");
                request.getRequestDispatcher("/warehouse/jsp/borrowings.jsp").forward(request, response);
                return;
            }
            
            // 更新借用记录
            borrowing.setStatus("returned");
            borrowing.setActualReturnDate(new Timestamp(new Date().getTime()));
            
            // 添加归还备注
            if (remarks != null && !remarks.isEmpty()) {
                if (returnCondition != null && !returnCondition.isEmpty()) {
                    borrowing.setRemarks("归还状况: " + returnCondition + " - " + remarks);
                } else {
                    borrowing.setRemarks(remarks);
                }
            } else if (returnCondition != null && !returnCondition.isEmpty()) {
                borrowing.setRemarks("归还状况: " + returnCondition);
            }
            
            borrowingDAO.update(borrowing);
            
            // 恢复库存
            Inventory inventory = inventoryDAO.findByFruitAndWarehouse(borrowing.getFruitId(), staff.getStaffId());
            if (inventory != null) {
                int newQuantity = inventory.getQuantity() + borrowing.getQuantity();
                inventoryDAO.updateQuantity(inventory.getInventoryId(), newQuantity);
            }
            
            session.setAttribute("successMessage", "借用水果已成功归还");
            
            // 重定向回借用列表页面
            response.sendRedirect(request.getContextPath() + "/warehouse/borrowings");
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/warehouse/borrowings");
        } catch (SQLException e) {
            throw new ServletException("数据库操作错误", e);
        }
    }
} 