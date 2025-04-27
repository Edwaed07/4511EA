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
import com.aib.dao.warehouse.InventoryTransactionDAO;
import com.aib.dao.warehouse.impl.FruitDAOImpl;
import com.aib.dao.warehouse.impl.InventoryDAOImpl;
import com.aib.dao.warehouse.impl.InventoryTransactionDAOImpl;
import com.aib.model.warehouse.Fruit;
import com.aib.model.warehouse.Inventory;
import com.aib.model.warehouse.InventoryTransaction;
import com.aib.model.warehouse.WarehouseStaff;

@WebServlet("/warehouse/check-in")
public class CheckInServlet extends HttpServlet {
    private InventoryDAO inventoryDAO = new InventoryDAOImpl();
    private FruitDAO fruitDAO = new FruitDAOImpl();
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
            // 获取所有水果
            List<Fruit> fruits = fruitDAO.findAll();
            request.setAttribute("fruits", fruits);
            
            // 转发到入库表单页面
            request.getRequestDispatcher("/warehouse/jsp/check-in.jsp").forward(request, response);
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
        
        // 获取表单参数
        String fruitIdParam = request.getParameter("fruitId");
        String quantityParam = request.getParameter("quantity");
        String sourceLocation = request.getParameter("sourceLocation");
        
        // 表单验证
        if (fruitIdParam == null || fruitIdParam.isEmpty() || 
            quantityParam == null || quantityParam.isEmpty() ||
            sourceLocation == null || sourceLocation.isEmpty()) {
            request.setAttribute("error", "所有字段都必须填写");
            doGet(request, response);
            return;
        }
        
        try {
            int fruitId = Integer.parseInt(fruitIdParam);
            int quantity = Integer.parseInt(quantityParam);
            
            if (quantity <= 0) {
                request.setAttribute("error", "数量必须大于0");
                doGet(request, response);
                return;
            }
            
            // 检查水果是否存在
            Fruit fruit = fruitDAO.findById(fruitId);
            if (fruit == null) {
                request.setAttribute("error", "所选水果不存在");
                doGet(request, response);
                return;
            }
            
            // 查找现有库存
            Inventory inventory = inventoryDAO.findByFruitAndWarehouse(fruitId, staff.getStaffId());
            
            // 如果库存不存在，创建新库存
            if (inventory == null) {
                inventory = new Inventory();
                inventory.setFruitId(fruitId);
                inventory.setWarehouseId(staff.getStaffId());
                inventory.setQuantity(quantity);
                inventory.setMinimumStock(10); // 默认最小库存为10
                inventory.setMaximumStock(1000); // 默认最大库存为1000
                inventoryDAO.save(inventory);
            } else {
                // 更新现有库存
                int newQuantity = inventory.getQuantity() + quantity;
                inventoryDAO.updateQuantity(inventory.getInventoryId(), newQuantity);
            }
            
            // 创建入库交易记录
            InventoryTransaction transaction = new InventoryTransaction();
            transaction.setFruitId(fruitId);
            transaction.setWarehouseId(staff.getStaffId());
            transaction.setTransactionType("check_in");
            transaction.setQuantity(quantity);
            transaction.setSourceLocation(sourceLocation);
            transaction.setDestinationLocation(staff.getWarehouseLocation());
            transaction.setStaffId(staff.getStaffId());
            transaction.setStatus("completed");
            
            transactionDAO.save(transaction);
            
            // 设置成功消息并重定向回库存页面
            session.setAttribute("successMessage", "成功入库 " + quantity + " 个 " + fruit.getFruitName());
            response.sendRedirect(request.getContextPath() + "/warehouse/inventory");
        } catch (NumberFormatException e) {
            request.setAttribute("error", "无效的数字格式");
            doGet(request, response);
        } catch (SQLException e) {
            throw new ServletException("数据库操作错误", e);
        }
    }
} 