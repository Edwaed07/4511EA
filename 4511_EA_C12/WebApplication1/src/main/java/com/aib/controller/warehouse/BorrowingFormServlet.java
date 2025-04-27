package com.aib.controller.warehouse;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.aib.dao.warehouse.BorrowingDAO;
import com.aib.dao.warehouse.FruitDAO;
import com.aib.dao.warehouse.InventoryDAO;
import com.aib.dao.warehouse.impl.BorrowingDAOImpl;
import com.aib.dao.warehouse.impl.FruitDAOImpl;
import com.aib.dao.warehouse.impl.InventoryDAOImpl;
import com.aib.model.warehouse.Borrowing;
import com.aib.model.warehouse.Fruit;
import com.aib.model.warehouse.Inventory;
import com.aib.model.warehouse.WarehouseStaff;

@WebServlet("/warehouse/borrowing-form")
public class BorrowingFormServlet extends HttpServlet {
    private BorrowingDAO borrowingDAO = new BorrowingDAOImpl();
    private FruitDAO fruitDAO = new FruitDAOImpl();
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
        
        try {
            // 获取所有水果
            List<Fruit> fruits = fruitDAO.findAll();
            request.setAttribute("fruits", fruits);
            
            // 转发到借用表单页面
            request.getRequestDispatcher("/warehouse/jsp/borrowing-form.jsp").forward(request, response);
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
        String expectedReturnDateStr = request.getParameter("expectedReturnDate");
        String purpose = request.getParameter("purpose");
        String remarks = request.getParameter("remarks");
        
        // 表单验证
        if (fruitIdParam == null || fruitIdParam.isEmpty() || 
            quantityParam == null || quantityParam.isEmpty() || 
            expectedReturnDateStr == null || expectedReturnDateStr.isEmpty() || 
            purpose == null || purpose.isEmpty()) {
            
            request.setAttribute("error", "必填字段不能为空");
            doGet(request, response);
            return;
        }
        
        try {
            int fruitId = Integer.parseInt(fruitIdParam);
            int quantity = Integer.parseInt(quantityParam);
            int warehouseId = staff.getStaffId(); // 使用员工的仓库ID
            
            // 日期格式转换
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date expectedReturnDate = dateFormat.parse(expectedReturnDateStr);
            Timestamp expectedReturnTimestamp = new Timestamp(expectedReturnDate.getTime());
            
            // 数量验证
            if (quantity <= 0) {
                request.setAttribute("error", "借用数量必须大于0");
                doGet(request, response);
                return;
            }
            
            // 检查库存是否足够
            Inventory inventory = inventoryDAO.findByFruitAndWarehouse(fruitId, warehouseId);
            if (inventory == null || inventory.getQuantity() < quantity) {
                request.setAttribute("error", "库存不足，无法借用");
                doGet(request, response);
                return;
            }
            
            // 创建借用记录
            Borrowing borrowing = new Borrowing();
            borrowing.setFruitId(fruitId);
            borrowing.setInventoryId(inventory.getInventoryId());
            borrowing.setWarehouseId(warehouseId);
            borrowing.setBorrowerId(staff.getStaffId());
            borrowing.setQuantity(quantity);
            borrowing.setExpectedReturnDate(expectedReturnTimestamp);
            borrowing.setPurpose(purpose);
            
            if (remarks != null && !remarks.isEmpty()) {
                borrowing.setRemarks(remarks);
            }
            
            // 设置初始状态为待审批
            borrowing.setStatus("pending");
            
            // 保存借用记录
            borrowingDAO.save(borrowing);
            
            // 设置成功消息并重定向到借用列表页面
            session.setAttribute("successMessage", "借用申请已提交，等待审批");
            response.sendRedirect(request.getContextPath() + "/warehouse/borrowings");
        } catch (NumberFormatException e) {
            request.setAttribute("error", "数字格式无效");
            doGet(request, response);
        } catch (ParseException e) {
            request.setAttribute("error", "日期格式无效");
            doGet(request, response);
        } catch (SQLException e) {
            throw new ServletException("数据库操作错误", e);
        }
    }
} 