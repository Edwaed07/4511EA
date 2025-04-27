package controller;

import db.DeliveryDAO;
import db.FruitDAO;
import db.InventoryDAO;
import db.ReserveDAO;
import model.Delivery;
import model.Fruit;
import model.Inventory;
import model.Reserve;
import model.User;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "DeliveryServlet", urlPatterns = {"/warehouse/deliveries"})
public class DeliveryServlet extends HttpServlet {

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
        
        try {
            if ("complete".equals(action)) {
                // 完成交付
                completeDelivery(request, response, user);
            } else if ("distribute".equals(action)) {
                // 分发到本地商店
                distributeToShops(request, response, user);
            } else {
                // 显示交付页面
                showDeliveriesPage(request, response, user);
            }
        } catch (ClassNotFoundException | SQLException e) {
            request.setAttribute("errorMsg", "数据库错误：" + e.getMessage());
            try {
                showDeliveriesPage(request, response, user);
            } catch (ClassNotFoundException | SQLException ex) {
                request.setAttribute("errorMsg", "严重错误：" + ex.getMessage());
                request.getRequestDispatcher("/warehouse/deliveries.jsp").forward(request, response);
            }
        }
    }
    
    private void completeDelivery(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException, ClassNotFoundException, SQLException {
        int deliveryId = Integer.parseInt(request.getParameter("deliveryId"));
        
        // 获取交付信息
        Delivery delivery = DeliveryDAO.getDeliveryById(deliveryId);
        
        if (delivery != null && "in-transit".equals(delivery.getStatus())) {
            // 更新交付状态
            DeliveryDAO.updateDeliveryStatus(deliveryId, "delivered");
            
            // 更新目的地仓库库存
            String locationType = delivery.getDestinationType();
            String locationName = delivery.getDestinationLocation();
            
            // 获取当前库存
            Inventory inventory = InventoryDAO.getInventory(delivery.getFruitId(), locationType, locationName);
            int currentQuantity = (inventory != null) ? inventory.getQuantity() : 0;
            
            // 增加库存
            InventoryDAO.updateInventoryQuantity(delivery.getFruitId(), locationType, locationName, currentQuantity + delivery.getQuantity());
            
            // 如果有关联的预订，更新预订状态
            if (delivery.getReserveId() > 0) {
                ReserveDAO.updateReserveStatus(delivery.getReserveId(), "delivered");
            }
            
            request.setAttribute("message", "交付已成功完成！");
        } else {
            request.setAttribute("errorMsg", "无法找到交付或交付已完成！");
        }
        
        // 返回交付页面
        showDeliveriesPage(request, response, user);
    }
    
    private void distributeToShops(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException, ClassNotFoundException, SQLException {
        int fruitId = Integer.parseInt(request.getParameter("fruitId"));
        String shop = request.getParameter("shop");
        String city = request.getParameter("city");
        int quantity = Integer.parseInt(request.getParameter("quantity"));
        
        // 获取当前仓库库存
        String warehouseLocation = user.getLocation();
        Inventory warehouse = InventoryDAO.getInventory(fruitId, "centralWarehouse", warehouseLocation);
        
        if (warehouse != null && warehouse.getQuantity() >= quantity) {
            // 创建交付记录
            Delivery delivery = new Delivery();
            delivery.setFruitId(fruitId);
            delivery.setSourceLocation(warehouseLocation);
            delivery.setDestinationLocation(shop);
            delivery.setDestinationType("shop");
            delivery.setQuantity(quantity);
            delivery.setDeliveryDate(new Date());
            delivery.setStatus("in-transit");
            delivery.setReserveId(0); // 不关联预订
            
            // 保存交付记录
            DeliveryDAO.addDelivery(delivery);
            
            // 更新中央仓库库存
            InventoryDAO.updateInventoryQuantity(fruitId, "centralWarehouse", warehouseLocation, warehouse.getQuantity() - quantity);
            
            request.setAttribute("message", "商店交付已成功安排！");
        } else {
            request.setAttribute("errorMsg", "中央仓库库存不足！");
        }
        
        // 返回交付页面
        showDeliveriesPage(request, response, user);
    }
    
    private void showDeliveriesPage(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException, ClassNotFoundException, SQLException {
        String warehouseLocation = user.getLocation();
        
        // 获取所有目的地为该仓库的交付
        List<Delivery> incomingDeliveries = DeliveryDAO.getDeliveriesByDestination("centralWarehouse", warehouseLocation);
        request.setAttribute("incomingDeliveries", incomingDeliveries);
        
        // 获取所有源头为该仓库的交付
        List<Delivery> outgoingDeliveries = DeliveryDAO.getDeliveriesBySource(warehouseLocation);
        request.setAttribute("outgoingDeliveries", outgoingDeliveries);
        
        // 获取水果信息
        List<Fruit> fruits = FruitDAO.getAllFruits();
        request.setAttribute("fruits", fruits);
        
        // 获取当前仓库的库存
        List<Inventory> inventory = InventoryDAO.getInventoryByLocation("centralWarehouse", warehouseLocation);
        request.setAttribute("inventory", inventory);
        
        // 转发到交付页面
        request.getRequestDispatcher("/warehouse/deliveries.jsp").forward(request, response);
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
        return "Delivery Management Servlet";
    }
} 