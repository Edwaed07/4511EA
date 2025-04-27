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

import com.aib.dao.warehouse.FruitDAO;
import com.aib.dao.warehouse.InventoryDAO;
import com.aib.dao.warehouse.impl.FruitDAOImpl;
import com.aib.dao.warehouse.impl.InventoryDAOImpl;
import com.aib.model.warehouse.Fruit;
import com.aib.model.warehouse.Inventory;
import com.aib.model.warehouse.WarehouseStaff;

@WebServlet("/warehouse/inventory")
public class InventoryServlet extends HttpServlet {
    private InventoryDAO inventoryDAO = new InventoryDAOImpl();
    private FruitDAO fruitDAO = new FruitDAOImpl();
    
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
            // 获取所有水果
            List<Fruit> fruits = fruitDAO.findAll();
            request.setAttribute("fruits", fruits);
            
            // 获取该仓库的库存
            List<Inventory> inventoryList = inventoryDAO.findByWarehouse(staff.getStaffId());
            request.setAttribute("inventoryList", inventoryList);
            
            // 获取低库存提醒
            List<Inventory> lowStockList = inventoryDAO.findLowStock();
            request.setAttribute("lowStockList", lowStockList);
            
            // 转发到库存管理页面
            request.getRequestDispatcher("/warehouse/jsp/inventory.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("数据库操作错误", e);
        }
    }
} 