package com.aib.controller.warehouse;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.aib.dao.warehouse.InventoryDAO;
import com.aib.dao.warehouse.ReservationDAO;
import com.aib.dao.warehouse.impl.InventoryDAOImpl;
import com.aib.dao.warehouse.impl.ReservationDAOImpl;
import com.aib.model.warehouse.Inventory;
import com.aib.model.warehouse.Reservation;
import com.aib.model.warehouse.WarehouseStaff;

@WebServlet("/warehouse/reservation-approval")
public class ReservationApprovalServlet extends HttpServlet {
    private ReservationDAO reservationDAO = new ReservationDAOImpl();
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
        
        // 获取预订ID
        String reservationIdParam = request.getParameter("id");
        if (reservationIdParam == null || reservationIdParam.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/warehouse/reservations");
            return;
        }
        
        try {
            int reservationId = Integer.parseInt(reservationIdParam);
            
            // 获取预订信息
            Reservation reservation = reservationDAO.findById(reservationId);
            if (reservation == null) {
                request.setAttribute("error", "预订不存在");
                request.getRequestDispatcher("/warehouse/jsp/reservations.jsp").forward(request, response);
                return;
            }
            
            request.setAttribute("reservation", reservation);
            
            // 检查库存是否足够
            Inventory inventory = inventoryDAO.findByFruitAndWarehouse(reservation.getFruitId(), staff.getStaffId());
            boolean sufficientStock = (inventory != null && inventory.getQuantity() >= reservation.getQuantity());
            request.setAttribute("sufficientStock", sufficientStock);
            
            if (inventory != null) {
                request.setAttribute("currentStock", inventory.getQuantity());
            } else {
                request.setAttribute("currentStock", 0);
            }
            
            // 转发到审批页面
            request.getRequestDispatcher("/warehouse/jsp/reservation-approval.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/warehouse/reservations");
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
        String reservationIdParam = request.getParameter("reservationId");
        String action = request.getParameter("action");
        String remarks = request.getParameter("remarks");
        
        if (reservationIdParam == null || reservationIdParam.isEmpty() || action == null || action.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/warehouse/reservations");
            return;
        }
        
        try {
            int reservationId = Integer.parseInt(reservationIdParam);
            
            // 获取预订信息
            Reservation reservation = reservationDAO.findById(reservationId);
            if (reservation == null) {
                request.setAttribute("error", "预订不存在");
                request.getRequestDispatcher("/warehouse/jsp/reservations.jsp").forward(request, response);
                return;
            }
            
            // 处理审批操作
            if ("approve".equals(action)) {
                // 检查库存是否足够
                Inventory inventory = inventoryDAO.findByFruitAndWarehouse(reservation.getFruitId(), staff.getStaffId());
                if (inventory == null || inventory.getQuantity() < reservation.getQuantity()) {
                    request.setAttribute("error", "库存不足，无法批准预订");
                    request.setAttribute("reservation", reservation);
                    request.getRequestDispatcher("/warehouse/jsp/reservation-approval.jsp").forward(request, response);
                    return;
                }
                
                // 更新预订状态为已批准
                reservationDAO.updateStatus(reservationId, "approved");
                
                // 添加批准备注
                if (remarks != null && !remarks.isEmpty()) {
                    reservation.setRemarks(remarks);
                    reservationDAO.update(reservation);
                }
                
                session.setAttribute("successMessage", "预订已成功批准");
            } else if ("reject".equals(action)) {
                // 更新预订状态为已拒绝
                reservationDAO.updateStatus(reservationId, "rejected");
                
                // 添加拒绝原因
                if (remarks != null && !remarks.isEmpty()) {
                    reservation.setRemarks(remarks);
                    reservationDAO.update(reservation);
                }
                
                session.setAttribute("successMessage", "预订已被拒绝");
            }
            
            // 重定向回预订列表页面
            response.sendRedirect(request.getContextPath() + "/warehouse/reservations");
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/warehouse/reservations");
        } catch (SQLException e) {
            throw new ServletException("数据库操作错误", e);
        }
    }
} 