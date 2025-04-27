package controller;

import db.DeliveryDAO;
import db.FruitDAO;
import db.InventoryDAO;
import db.ReserveDAO;
import model.Delivery;
import model.Fruit;
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

@WebServlet(name = "ReserveManagementServlet", urlPatterns = {"/warehouse/reserves"})
public class ReserveManagementServlet extends HttpServlet {

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
            if ("approve".equals(action)) {
                // 审批预订
                approveReserve(request, response, user);
            } else if ("deliver".equals(action)) {
                // 安排交付
                deliverReserve(request, response, user);
            } else {
                // 显示预订管理页面
                showReservesPage(request, response, user);
            }
        } catch (ClassNotFoundException | SQLException e) {
            request.setAttribute("errorMsg", "数据库错误：" + e.getMessage());
            try {
                showReservesPage(request, response, user);
            } catch (ClassNotFoundException | SQLException ex) {
                request.setAttribute("errorMsg", "严重错误：" + ex.getMessage());
                request.getRequestDispatcher("/warehouse/reserves.jsp").forward(request, response);
            }
        }
    }
    
    private void approveReserve(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException, ClassNotFoundException, SQLException {
        int reserveId = Integer.parseInt(request.getParameter("reserveId"));
        
        // 获取预订信息
        Reserve reserve = ReserveDAO.getReserveById(reserveId);
        
        if (reserve != null && "pending".equals(reserve.getStatus())) {
            // 更新预订状态为已批准
            ReserveDAO.updateReserveStatus(reserveId, "approved");
            request.setAttribute("message", "预订已成功批准！");
        } else {
            request.setAttribute("errorMsg", "无法找到预订或预订已处理！");
        }
        
        // 返回预订管理页面
        showReservesPage(request, response, user);
    }
    
    private void deliverReserve(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException, ClassNotFoundException, SQLException {
        int reserveId = Integer.parseInt(request.getParameter("reserveId"));
        
        // 获取预订信息
        Reserve reserve = ReserveDAO.getReserveById(reserveId);
        
        if (reserve != null && "approved".equals(reserve.getStatus())) {
            // 检查源仓库是否有足够的库存
            Fruit fruit = FruitDAO.getFruitById(reserve.getFruitId());
            String sourceLocation = fruit.getSourceCountry();
            int availableQuantity = InventoryDAO.getInventory(reserve.getFruitId(), "sourceWarehouse", sourceLocation).getQuantity();
            
            if (availableQuantity >= reserve.getQuantity()) {
                // 创建交付记录
                Delivery delivery = new Delivery();
                delivery.setFruitId(reserve.getFruitId());
                delivery.setSourceLocation(sourceLocation);
                delivery.setDestinationLocation(reserve.getCountry());
                delivery.setDestinationType("centralWarehouse");
                delivery.setQuantity(reserve.getQuantity());
                delivery.setDeliveryDate(new Date());
                delivery.setStatus("in-transit");
                delivery.setReserveId(reserveId);
                
                // 保存交付记录
                DeliveryDAO.addDelivery(delivery);
                
                // 更新源仓库库存
                InventoryDAO.updateInventoryQuantity(reserve.getFruitId(), "sourceWarehouse", sourceLocation, availableQuantity - reserve.getQuantity());
                
                // 更新预订状态
                ReserveDAO.updateReserveStatus(reserveId, "in-transit");
                
                request.setAttribute("message", "交付已成功安排！");
            } else {
                request.setAttribute("errorMsg", "源仓库库存不足！");
            }
        } else {
            request.setAttribute("errorMsg", "无法找到预订或预订未批准！");
        }
        
        // 返回预订管理页面
        showReservesPage(request, response, user);
    }
    
    private void showReservesPage(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException, ClassNotFoundException, SQLException {
        // 获取该国家所有待处理的预订
        String country = user.getLocation();
        List<Reserve> pendingReserves = ReserveDAO.getPendingReservesByCountry(country);
        request.setAttribute("pendingReserves", pendingReserves);
        
        // 获取水果信息
        List<Fruit> fruits = FruitDAO.getAllFruits();
        request.setAttribute("fruits", fruits);
        
        // 转发到预订管理页面
        request.getRequestDispatcher("/warehouse/reserves.jsp").forward(request, response);
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
        return "Reserve Management Servlet";
    }
} 