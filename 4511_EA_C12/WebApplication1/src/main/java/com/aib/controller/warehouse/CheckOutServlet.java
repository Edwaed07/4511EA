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

@WebServlet("/warehouse/check-out")
public class CheckOutServlet extends HttpServlet {
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
            // 获取该仓库的库存
            List<Inventory> inventoryList = inventoryDAO.findByWarehouse(staff.getStaffId());
            request.setAttribute("inventoryList", inventoryList);
            
            // 转发到出库表单页面
            request.getRequestDispatcher("/warehouse/jsp/check-out.jsp").forward(request, response);
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
        String inventoryIdParam = request.getParameter("inventoryId");
        String quantityParam = request.getParameter("quantity");
        String destinationLocation = request.getParameter("destinationLocation");
        
        // 表单验证
        if (inventoryIdParam == null || inventoryIdParam.isEmpty() || 
            quantityParam == null || quantityParam.isEmpty() ||
            destinationLocation == null || destinationLocation.isEmpty()) {
            request.setAttribute("error", "所有字段都必须填写");
            doGet(request, response);
            return;
        }
        
        try {
            int inventoryId = Integer.parseInt(inventoryIdParam);
            int quantity = Integer.parseInt(quantityParam);
            
            if (quantity <= 0) {
                request.setAttribute("error", "数量必须大于0");
                doGet(request, response);
                return;
            }
            
            // 获取库存信息
            Inventory inventory = inventoryDAO.findById(inventoryId);
            if (inventory == null) {
                request.setAttribute("error", "库存不存在");
                doGet(request, response);
                return;
            }
            
            // 检查库存是否充足
            if (inventory.getQuantity() < quantity) {
                request.setAttribute("error", "库存不足，当前库存: " + inventory.getQuantity());
                doGet(request, response);
                return;
            }
            
            // 获取水果信息
            Fruit fruit = fruitDAO.findById(inventory.getFruitId());
            if (fruit == null) {
                request.setAttribute("error", "水果信息不存在");
                doGet(request, response);
                return;
            }
            
            // 更新库存
            int newQuantity = inventory.getQuantity() - quantity;
            inventoryDAO.updateQuantity(inventoryId, newQuantity);
            
            // 创建出库交易记录
            InventoryTransaction transaction = new InventoryTransaction();
            transaction.setFruitId(inventory.getFruitId());
            transaction.setWarehouseId(staff.getStaffId());
            transaction.setTransactionType("check_out");
            transaction.setQuantity(quantity);
            transaction.setSourceLocation(staff.getWarehouseLocation());
            transaction.setDestinationLocation(destinationLocation);
            transaction.setStaffId(staff.getStaffId());
            transaction.setStatus("completed");
            
            transactionDAO.save(transaction);
            
            // 设置成功消息并重定向回库存页面
            session.setAttribute("successMessage", "成功出库 " + quantity + " 个 " + fruit.getFruitName() + " 到 " + destinationLocation);
            response.sendRedirect(request.getContextPath() + "/warehouse/inventory");
        } catch (NumberFormatException e) {
            request.setAttribute("error", "无效的数字格式");
            doGet(request, response);
        } catch (SQLException e) {
            throw new ServletException("数据库操作错误", e);
        }
    }
} 