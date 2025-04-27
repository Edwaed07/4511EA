package controller;

import db.FruitDAO;
import db.InventoryDAO;
import model.Fruit;
import model.Inventory;
import model.User;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "InventoryUpdateServlet", urlPatterns = {"/warehouse/inventory"})
public class InventoryUpdateServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        // 检查用户是否登录且是仓库员工
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        if (user == null || !"warehouse".equals(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/warehouse/login");
            return;
        }
        
        String action = request.getParameter("action");
        
        if ("update".equals(action)) {
            // 更新库存
            updateInventory(request, response, user);
        } else {
            // 显示库存管理页面
            showInventoryPage(request, response, user);
        }
    }
    
    private void updateInventory(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        try {
            int fruitId = Integer.parseInt(request.getParameter("fruitId"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));
            String locationType = request.getParameter("locationType");
            String locationName = user.getLocation();
            
            // 更新库存
            boolean success = InventoryDAO.updateInventoryQuantity(fruitId, locationType, locationName, quantity);
            
            if (success) {
                request.setAttribute("message", "库存更新成功！");
            } else {
                request.setAttribute("errorMsg", "库存更新失败！");
            }
            
            // 重定向回库存页面
            showInventoryPage(request, response, user);
            
        } catch (NumberFormatException e) {
            request.setAttribute("errorMsg", "无效的数字输入！");
            showInventoryPage(request, response, user);
        } catch (ClassNotFoundException | SQLException e) {
            request.setAttribute("errorMsg", "数据库错误：" + e.getMessage());
            showInventoryPage(request, response, user);
        }
    }
    
    private void showInventoryPage(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        try {
            // 获取水果列表
            request.setAttribute("fruits", FruitDAO.getAllFruits());
            
            // 获取当前仓库的库存
            String warehouseLocation = user.getLocation();
            request.setAttribute("inventory", InventoryDAO.getInventoryByLocation("centralWarehouse", warehouseLocation));
            
            // 转发到库存管理页面
            request.getRequestDispatcher("/warehouse/inventory.jsp").forward(request, response);
            
        } catch (ClassNotFoundException | SQLException e) {
            request.setAttribute("errorMsg", "数据库错误：" + e.getMessage());
            request.getRequestDispatcher("/warehouse/inventory.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Warehouse Inventory Update Servlet";
    }
} 