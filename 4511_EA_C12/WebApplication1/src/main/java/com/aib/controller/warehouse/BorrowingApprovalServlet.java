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

@WebServlet("/warehouse/borrowing-approval")
public class BorrowingApprovalServlet extends HttpServlet {
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
            
            request.setAttribute("borrowing", borrowing);
            
            // 检查库存是否足够
            Inventory inventory = inventoryDAO.findByFruitAndWarehouse(borrowing.getFruitId(), staff.getStaffId());
            boolean sufficientStock = (inventory != null && inventory.getQuantity() >= borrowing.getQuantity());
            request.setAttribute("sufficientStock", sufficientStock);
            
            if (inventory != null) {
                request.setAttribute("currentStock", inventory.getQuantity());
            } else {
                request.setAttribute("currentStock", 0);
            }
            
            // 转发到审批页面
            request.getRequestDispatcher("/warehouse/jsp/borrowing-approval.jsp").forward(request, response);
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
        String action = request.getParameter("action");
        String remarks = request.getParameter("remarks");
        
        if (borrowingIdParam == null || borrowingIdParam.isEmpty() || action == null || action.isEmpty()) {
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
            
            // 处理审批操作
            if ("approve".equals(action)) {
                // 检查库存是否足够
                Inventory inventory = inventoryDAO.findByFruitAndWarehouse(borrowing.getFruitId(), staff.getStaffId());
                if (inventory == null || inventory.getQuantity() < borrowing.getQuantity()) {
                    request.setAttribute("error", "库存不足，无法批准借用");
                    request.setAttribute("borrowing", borrowing);
                    request.getRequestDispatcher("/warehouse/jsp/borrowing-approval.jsp").forward(request, response);
                    return;
                }
                
                // 更新借用状态为已批准，并设置借用日期为当前时间
                borrowing.setStatus("approved");
                borrowing.setBorrowDate(new Timestamp(new Date().getTime()));
                
                // 添加批准备注
                if (remarks != null && !remarks.isEmpty()) {
                    borrowing.setRemarks(remarks);
                }
                
                borrowingDAO.update(borrowing);
                
                // 减少库存
                int newQuantity = inventory.getQuantity() - borrowing.getQuantity();
                inventoryDAO.updateQuantity(inventory.getInventoryId(), newQuantity);
                
                session.setAttribute("successMessage", "借用申请已成功批准");
            } else if ("reject".equals(action)) {
                // 更新借用状态为已拒绝
                borrowingDAO.updateStatus(borrowingId, "rejected");
                
                // 添加拒绝原因
                if (remarks != null && !remarks.isEmpty()) {
                    borrowing.setRemarks(remarks);
                    borrowingDAO.update(borrowing);
                }
                
                session.setAttribute("successMessage", "借用申请已被拒绝");
            }
            
            // 重定向回借用列表页面
            response.sendRedirect(request.getContextPath() + "/warehouse/borrowings");
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/warehouse/borrowings");
        } catch (SQLException e) {
            throw new ServletException("数据库操作错误", e);
        }
    }
} 