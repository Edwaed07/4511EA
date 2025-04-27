package controller;

import db.FruitDAO;
import db.InventoryDAO;
import db.ReserveDAO;
import model.User;
import model.Fruit;
import model.Inventory;
import model.Reserve;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "WarehouseDashboardServlet", urlPatterns = {"/warehouse/dashboard"})
public class WarehouseDashboardServlet extends HttpServlet {

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
        
        try {
            // 获取当前仓库的库存
            String warehouseLocation = user.getLocation();
            List<Inventory> inventory = InventoryDAO.getInventoryByLocation("centralWarehouse", warehouseLocation);
            request.setAttribute("inventory", inventory);
            
            // 获取该国家所有待处理的预订
            List<Reserve> pendingReserves = ReserveDAO.getPendingReservesByCountry(warehouseLocation);
            request.setAttribute("pendingReserves", pendingReserves);
            
            // 获取水果信息
            List<Fruit> fruits = FruitDAO.getAllFruits();
            request.setAttribute("fruits", fruits);
            
            // 转发到仪表板页面
            request.getRequestDispatcher("/warehouse/dashboard.jsp").forward(request, response);
            
        } catch (ClassNotFoundException | SQLException e) {
            request.setAttribute("errorMsg", "数据库错误：" + e.getMessage());
            request.getRequestDispatcher("/warehouse/dashboard.jsp").forward(request, response);
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
        return "Warehouse Dashboard Servlet";
    }
} 