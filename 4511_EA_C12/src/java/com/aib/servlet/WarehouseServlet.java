package com.aib.servlet;

import com.aib.bean.Delivery;
import com.aib.bean.Fruit;
import com.aib.bean.Need;
import com.aib.bean.Warehouse;
import com.aib.util.DatabaseConnection;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "WarehouseServlet", urlPatterns = {"/WarehouseServlet"})
public class WarehouseServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        if (action == null) {
            action = "warehouseHome";
        }
        
        HttpSession session = request.getSession();
        if (session.getAttribute("employeeId") == null) {
            response.sendRedirect("login.jsp?role=warehouseStaff");
            return;
        }
        
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            
            switch (action) {
                case "warehouseHome":
                    showWarehouseHome(request, response, conn);
                    break;
                    
                case "viewTotalNeeds":
                    viewTotalNeeds(request, response, conn);
                    break;
                    
                case "viewDeliveries":
                    viewDeliveries(request, response, conn);
                    break;
                    
                case "arrangeDelivery":
                    arrangeDelivery(request, response, conn);
                    break;
                
                case "loadCheckIn":
                    loadCheckInPage(request, response, conn);
                    break;
                    
                default:
                    showWarehouseHome(request, response, conn);
                    break;
            }
            
        } catch (SQLException e) {
            request.setAttribute("error", "Database error: " + e.getMessage());
            e.printStackTrace();
            request.getRequestDispatcher("error.jsp").forward(request, response);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    private void showWarehouseHome(HttpServletRequest request, HttpServletResponse response, Connection conn)
            throws ServletException, IOException, SQLException {
        
        // 获取最近待处理的需求和配送
        List<Need> recentNeeds = getRecentNeeds(conn, 5);
        List<Delivery> recentDeliveries = getRecentDeliveries(conn, 5);
        
        // 获取基本统计数据
        int pendingNeedsCount = getPendingNeedsCount(conn);
        int inTransitDeliveriesCount = getDeliveriesCountByStatus(conn, "In Transit");
        int pendingDeliveriesCount = getDeliveriesCountByStatus(conn, "Pending");
        
        // 设置请求属性
        request.setAttribute("recentNeeds", recentNeeds);
        request.setAttribute("recentDeliveries", recentDeliveries);
        request.setAttribute("pendingNeedsCount", pendingNeedsCount);
        request.setAttribute("inTransitDeliveriesCount", inTransitDeliveriesCount);
        request.setAttribute("pendingDeliveriesCount", pendingDeliveriesCount);
        
        // 转发到仓库主页
        request.getRequestDispatcher("warehouseHome.jsp").forward(request, response);
    }
    
    private void viewTotalNeeds(HttpServletRequest request, HttpServletResponse response, Connection conn)
            throws ServletException, IOException, SQLException {
        
        String country = request.getParameter("country");
        String status = request.getParameter("status");
        
        // 构建SQL查询
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT n.id, n.country, n.fruit_id, f.fruit_name as fruit_name, ");
        sqlBuilder.append("n.total_quantity, n.request_count, n.start_date, n.end_date, ");
        sqlBuilder.append("n.status, n.delivery_status ");
        sqlBuilder.append("FROM country_needs n ");
        sqlBuilder.append("JOIN fruits f ON n.fruit_id = f.fruit_id ");
        sqlBuilder.append("WHERE 1=1 ");
        
        List<Object> params = new ArrayList<>();
        
        if (country != null && !country.isEmpty()) {
            sqlBuilder.append("AND n.country = ? ");
            params.add(country);
        }
        
        if (status != null && !status.isEmpty()) {
            sqlBuilder.append("AND n.status = ? ");
            params.add(status);
        }
        
        sqlBuilder.append("ORDER BY n.country, n.status, n.id");
        
        // 执行查询
        List<Need> totalNeeds = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sqlBuilder.toString())) {
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Need need = new Need(
                    rs.getInt("id"),
                    rs.getString("country"),
                    rs.getInt("fruit_id"),
                    rs.getString("fruit_name"),
                    rs.getInt("total_quantity"),
                    rs.getInt("request_count")
                );
                
                need.setStartDate(rs.getDate("start_date"));
                need.setEndDate(rs.getDate("end_date"));
                need.setStatus(rs.getString("status"));
                need.setDeliveryStatus(rs.getString("delivery_status"));
                
                totalNeeds.add(need);
            }
        }
        
        // 获取可用国家列表
        List<String> countries = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(
                "SELECT DISTINCT country FROM country_needs ORDER BY country")) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                countries.add(rs.getString("country"));
            }
        }
        
        // 获取国家摘要数据
        List<Map<String, Object>> countrySummary = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(
                "SELECT country, " +
                "SUM(CASE WHEN status = 'Pending' THEN 1 ELSE 0 END) as pending_count, " +
                "SUM(CASE WHEN status = 'Approved' THEN 1 ELSE 0 END) as approved_count, " +
                "SUM(CASE WHEN status = 'Rejected' THEN 1 ELSE 0 END) as rejected_count " +
                "FROM country_needs GROUP BY country ORDER BY country")) {
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Map<String, Object> summary = new HashMap<>();
                summary.put("country", rs.getString("country"));
                summary.put("pendingCount", rs.getInt("pending_count"));
                summary.put("approvedCount", rs.getInt("approved_count"));
                summary.put("rejectedCount", rs.getInt("rejected_count"));
                countrySummary.add(summary);
            }
        }
        
        // 设置请求属性
        request.setAttribute("totalNeeds", totalNeeds);
        request.setAttribute("countries", countries);
        request.setAttribute("countrySummary", countrySummary);
        request.setAttribute("selectedCountry", country);
        request.setAttribute("selectedStatus", status);
        
        // 转发到总需求页面
        request.getRequestDispatcher("totalNeeds.jsp").forward(request, response);
    }
    
    private void viewDeliveries(HttpServletRequest request, HttpServletResponse response, Connection conn)
            throws ServletException, IOException, SQLException {
        
        // 获取不同状态的配送
        List<Delivery> pendingDeliveries = getDeliveriesByStatus(conn, "Pending");
        List<Delivery> inTransitDeliveries = getDeliveriesByStatus(conn, "In Transit");
        List<Delivery> completedDeliveries = getDeliveriesByStatus(conn, "Delivered");
        
        // 获取源位置和目的地位置
        List<Map<String, Object>> sourceLocations = getWarehouses(conn, "Source");
        List<Map<String, Object>> destinationLocations = getWarehouses(conn, "Central");
        
        // 获取可用的水果
        List<Fruit> availableFruits = getAvailableFruits(conn);
        
        // 设置请求属性
        request.setAttribute("pendingDeliveries", pendingDeliveries);
        request.setAttribute("inTransitDeliveries", inTransitDeliveries);
        request.setAttribute("completedDeliveries", completedDeliveries);
        request.setAttribute("sourceLocations", sourceLocations);
        request.setAttribute("destinationLocations", destinationLocations);
        request.setAttribute("availableFruits", availableFruits);
        
        // 转发到配送管理页面
        request.getRequestDispatcher("manageDeliveries.jsp").forward(request, response);
    }
    
    private void arrangeDelivery(HttpServletRequest request, HttpServletResponse response, Connection conn)
            throws ServletException, IOException, SQLException {
        
        String needIdStr = request.getParameter("needId");
        if (needIdStr == null || needIdStr.isEmpty()) {
            request.setAttribute("error", "Need ID is required");
            request.getRequestDispatcher("error.jsp").forward(request, response);
            return;
        }
        
        int needId = Integer.parseInt(needIdStr);
        
        // 获取需求详情
        Need need = getNeedById(conn, needId);
        if (need == null) {
            request.setAttribute("error", "Need not found");
            request.getRequestDispatcher("error.jsp").forward(request, response);
            return;
        }
        
        // 获取源仓库和目的地仓库
        List<Map<String, Object>> sourceWarehouses = getWarehousesByFruit(conn, need.getFruitId(), "Source");
        List<Map<String, Object>> destinationWarehouses = getWarehousesByCountry(conn, need.getCountry(), "Central");
        
        // 获取可用库存
        int availableStock = getTotalAvailableStock(conn, need.getFruitId());
        
        // 获取商店请求
        List<Map<String, Object>> shopRequests = getShopRequestsByNeedId(conn, needId);
        
        // 设置请求属性
        request.setAttribute("need", need);
        request.setAttribute("sourceWarehouses", sourceWarehouses);
        request.setAttribute("destinationWarehouses", destinationWarehouses);
        request.setAttribute("availableStock", availableStock);
        request.setAttribute("shopRequests", shopRequests);
        
        // 转发到配送安排页面
        request.getRequestDispatcher("arrangeDelivery.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        if (action == null) {
            request.setAttribute("error", "No action specified");
            request.getRequestDispatcher("error.jsp").forward(request, response);
            return;
        }
        
        HttpSession session = request.getSession();
        if (session.getAttribute("employeeId") == null) {
            response.sendRedirect("login.jsp?role=warehouseStaff");
            return;
        }
        
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            
            switch (action) {
                case "checkIn":
                    checkInStock(request, response, conn);
                    break;
                    
                case "approveNeed":
                    approveNeed(request, response, conn);
                    break;
                    
                case "rejectNeed":
                    rejectNeed(request, response, conn);
                    break;
                    
                case "createDelivery":
                    createDelivery(request, response, conn);
                    break;
                    
                case "startDelivery":
                    startDelivery(request, response, conn);
                    break;
                    
                case "completeDelivery":
                    completeDelivery(request, response, conn);
                    break;
                    
                case "arrangeDelivery":
                    processArrangeDelivery(request, response, conn);
                    break;
                    
                default:
                    request.setAttribute("error", "Unknown action: " + action);
                    request.getRequestDispatcher("error.jsp").forward(request, response);
                    break;
            }
            
        } catch (SQLException e) {
            request.setAttribute("error", "Database error: " + e.getMessage());
            e.printStackTrace();
            request.getRequestDispatcher("error.jsp").forward(request, response);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    private void checkInStock(HttpServletRequest request, HttpServletResponse response, Connection conn)
            throws ServletException, IOException, SQLException {
        
        String fruitIdStr = request.getParameter("fruitId");
        String quantityStr = request.getParameter("quantity");
        String warehouseLocation = request.getParameter("warehouseLocation");
        
        // 驗證輸入
        if (fruitIdStr == null || fruitIdStr.isEmpty() || 
            quantityStr == null || quantityStr.isEmpty() || 
            warehouseLocation == null || warehouseLocation.isEmpty()) {
            
            request.setAttribute("message", "All fields are required");
            request.setAttribute("messageType", "error");
            loadCheckInPage(request, response, conn);
            return;
        }
        
        int fruitId = Integer.parseInt(fruitIdStr);
        int quantity = Integer.parseInt(quantityStr);
        
        // 更新倉庫庫存
        String updateSql = "UPDATE warehouse_inventory SET stock_level = stock_level + ? " +
                          "WHERE fruit_id = ? AND warehouse_type = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(updateSql)) {
            stmt.setInt(1, quantity);
            stmt.setInt(2, fruitId);
            stmt.setString(3, warehouseLocation);
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected == 0) {
                // 如果沒有記錄被更新，則插入新記錄
                String insertSql = "INSERT INTO warehouse_inventory (fruit_id, warehouse_type, stock_level) " +
                                 "VALUES (?, ?, ?)";
                
                try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                    insertStmt.setInt(1, fruitId);
                    insertStmt.setString(2, warehouseLocation);
                    insertStmt.setInt(3, quantity);
                    insertStmt.executeUpdate();
                }
            }
            
            // 記錄庫存變動日誌（添加錯誤處理）
            try {
                String logSql = "INSERT INTO stock_movement_log (fruit_id, warehouse_type, quantity, movement_type, user_id, movement_date) " +
                              "VALUES (?, ?, ?, 'Check-In', ?, CURRENT_TIMESTAMP)";
                
                HttpSession session = request.getSession();
                
                try (PreparedStatement logStmt = conn.prepareStatement(logSql)) {
                    logStmt.setInt(1, fruitId);
                    logStmt.setString(2, warehouseLocation);
                    logStmt.setInt(3, quantity);
                    logStmt.setInt(4, (Integer) session.getAttribute("employeeId"));
                    logStmt.executeUpdate();
                }
            } catch (SQLException e) {
                // 只記錄錯誤，但不中斷處理流程
                // 表格可能不存在，但主要功能（庫存更新）已經完成
                System.err.println("無法記錄庫存變動日誌: " + e.getMessage());
                // 庫存已更新成功，所以仍然顯示成功信息，但添加提示
                request.setAttribute("message", "Stock checked in successfully (movement log not created)");
                request.setAttribute("messageType", "warning");
                loadCheckInPage(request, response, conn);
                return;
            }
            
            // 設置成功消息
            request.setAttribute("message", "Stock checked in successfully");
            request.setAttribute("messageType", "success");
        }
        
        // 重新加載入庫頁面
        loadCheckInPage(request, response, conn);
    }
    
    private void loadCheckInPage(HttpServletRequest request, HttpServletResponse response, Connection conn)
            throws ServletException, IOException, SQLException {
        
        // 获取可用的水果
        List<Fruit> fruits = getAvailableFruits(conn);
        
        // 获取当前仓库库存
        List<Map<String, Object>> warehouseStock = getWarehouseStock(conn);
        
        // 设置请求属性
        request.setAttribute("fruits", fruits);
        request.setAttribute("warehouseStock", warehouseStock);
        
        // 转发到入库页面
        request.getRequestDispatcher("warehouseCheckIn.jsp").forward(request, response);
    }
    
    private void approveNeed(HttpServletRequest request, HttpServletResponse response, Connection conn)
            throws ServletException, IOException, SQLException {
        
        String needIdStr = request.getParameter("needId");
        if (needIdStr == null || needIdStr.isEmpty()) {
            request.setAttribute("message", "Need ID is required");
            request.setAttribute("messageType", "error");
            viewTotalNeeds(request, response, conn);
            return;
        }
        
        int needId = Integer.parseInt(needIdStr);
        
        // 更新需求状态为已批准
        String updateSql = "UPDATE country_needs SET status = 'Approved' WHERE id = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(updateSql)) {
            stmt.setInt(1, needId);
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                request.setAttribute("message", "Need approved successfully");
                request.setAttribute("messageType", "success");
            } else {
                request.setAttribute("message", "Failed to approve need");
                request.setAttribute("messageType", "error");
            }
        }
        
        // 重新加载总需求页面
        viewTotalNeeds(request, response, conn);
    }
    
    private void rejectNeed(HttpServletRequest request, HttpServletResponse response, Connection conn)
            throws ServletException, IOException, SQLException {
        
        String needIdStr = request.getParameter("needId");
        if (needIdStr == null || needIdStr.isEmpty()) {
            request.setAttribute("message", "Need ID is required");
            request.setAttribute("messageType", "error");
            viewTotalNeeds(request, response, conn);
            return;
        }
        
        int needId = Integer.parseInt(needIdStr);
        
        // 更新需求状态为已拒绝
        String updateSql = "UPDATE country_needs SET status = 'Rejected' WHERE id = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(updateSql)) {
            stmt.setInt(1, needId);
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                request.setAttribute("message", "Need rejected successfully");
                request.setAttribute("messageType", "success");
            } else {
                request.setAttribute("message", "Failed to reject need");
                request.setAttribute("messageType", "error");
            }
        }
        
        // 重新加载总需求页面
        viewTotalNeeds(request, response, conn);
    }
    
    // 辅助方法实现
    private List<Need> getRecentNeeds(Connection conn, int limit) throws SQLException {
        List<Need> needs = new ArrayList<>();
        String sql = "SELECT n.id, n.country, n.fruit_id, f.fruit_name as fruit_name, " +
                     "n.total_quantity, n.request_count, n.start_date, n.end_date, " +
                     "n.status, n.delivery_status " +
                     "FROM country_needs n " +
                     "JOIN fruits f ON n.fruit_id = f.fruit_id " +
                     "ORDER BY n.created_at DESC LIMIT ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, limit);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Need need = new Need(
                    rs.getInt("id"),
                    rs.getString("country"),
                    rs.getInt("fruit_id"),
                    rs.getString("fruit_name"),
                    rs.getInt("total_quantity"),
                    rs.getInt("request_count")
                );
                
                need.setStartDate(rs.getDate("start_date"));
                need.setEndDate(rs.getDate("end_date"));
                need.setStatus(rs.getString("status"));
                need.setDeliveryStatus(rs.getString("delivery_status"));
                
                needs.add(need);
            }
        }
        
        return needs;
    }
    
    private List<Delivery> getRecentDeliveries(Connection conn, int limit) throws SQLException {
        List<Delivery> deliveries = new ArrayList<>();
        String sql = "SELECT d.delivery_id, d.source_location, d.destination_location, " +
                     "d.fruit_id, f.fruit_name as fruit_name, d.quantity, d.created_date, " +
                     "d.ship_date, d.delivery_date, d.status " +
                     "FROM deliveries d " +
                     "JOIN fruits f ON d.fruit_id = f.fruit_id " +
                     "ORDER BY d.created_date DESC LIMIT ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, limit);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Delivery delivery = new Delivery(
                    rs.getInt("delivery_id"),
                    rs.getString("source_location"),
                    rs.getString("destination_location"),
                    rs.getInt("fruit_id"),
                    rs.getString("fruit_name"),
                    rs.getInt("quantity")
                );
                
                delivery.setShipDate(rs.getTimestamp("ship_date"));
                delivery.setDeliveryDate(rs.getTimestamp("delivery_date"));
                delivery.setStatus(rs.getString("status"));
                
                deliveries.add(delivery);
            }
        }
        
        return deliveries;
    }
    
    private int getPendingNeedsCount(Connection conn) throws SQLException {
        String sql = "SELECT COUNT(*) as count FROM country_needs WHERE status = 'Pending'";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("count");
            }
        }
        return 0;
    }
    
    private int getDeliveriesCountByStatus(Connection conn, String status) throws SQLException {
        String sql = "SELECT COUNT(*) as count FROM deliveries WHERE status = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("count");
            }
        }
        return 0;
    }
    
    private List<Map<String, Object>> getWarehouses(Connection conn, String type) throws SQLException {
        List<Map<String, Object>> warehouses = new ArrayList<>();
        String sql = "SELECT id, name, type, city, country FROM warehouses WHERE type = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, type);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Map<String, Object> warehouse = new HashMap<>();
                warehouse.put("id", rs.getInt("id"));
                warehouse.put("name", rs.getString("name"));
                warehouse.put("type", rs.getString("type"));
                warehouse.put("city", rs.getString("city"));
                warehouse.put("country", rs.getString("country"));
                
                warehouses.add(warehouse);
            }
        }
        
        return warehouses;
    }
    
    private List<Delivery> getDeliveriesByStatus(Connection conn, String status) throws SQLException {
        List<Delivery> deliveries = new ArrayList<>();
        String sql = "SELECT d.delivery_id, d.source_location, d.destination_location, " +
                     "d.fruit_id, f.fruit_name as fruit_name, d.quantity, d.created_date, " +
                     "d.ship_date, d.delivery_date, d.status " +
                     "FROM deliveries d " +
                     "JOIN fruits f ON d.fruit_id = f.fruit_id " +
                     "WHERE d.status = ? " +
                     "ORDER BY d.created_date DESC";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Delivery delivery = new Delivery(
                    rs.getInt("delivery_id"),
                    rs.getString("source_location"),
                    rs.getString("destination_location"),
                    rs.getInt("fruit_id"),
                    rs.getString("fruit_name"),
                    rs.getInt("quantity")
                );
                
                delivery.setShipDate(rs.getTimestamp("ship_date"));
                delivery.setDeliveryDate(rs.getTimestamp("delivery_date"));
                delivery.setStatus(rs.getString("status"));
                
                deliveries.add(delivery);
            }
        }
        
        return deliveries;
    }
    
    private List<Fruit> getAvailableFruits(Connection conn) throws SQLException {
        List<Fruit> fruits = new ArrayList<>();
        String sql = "SELECT f.fruit_id as id, f.fruit_name as name, f.source_location, f.country, " +
                    "COALESCE(wi.stock_level, 0) as stock_level " +
                    "FROM fruits f " +
                    "LEFT JOIN warehouse_inventory wi ON f.fruit_id = wi.fruit_id " +
                    "WHERE wi.warehouse_type = 'Source' OR wi.warehouse_type IS NULL " +
                    "ORDER BY f.fruit_name";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Fruit fruit = new Fruit(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("source_location"),
                    rs.getString("country"),
                    rs.getInt("stock_level")
                );
                
                fruits.add(fruit);
            }
        }
        
        return fruits;
    }
    
    private Need getNeedById(Connection conn, int needId) throws SQLException {
        String sql = "SELECT n.id, n.country, n.fruit_id, f.fruit_name as fruit_name, " +
                    "n.total_quantity, n.request_count, n.start_date, n.end_date, " +
                    "n.status, n.delivery_status " +
                    "FROM country_needs n " +
                    "JOIN fruits f ON n.fruit_id = f.fruit_id " +
                    "WHERE n.id = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, needId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Need need = new Need(
                    rs.getInt("id"),
                    rs.getString("country"),
                    rs.getInt("fruit_id"),
                    rs.getString("fruit_name"),
                    rs.getInt("total_quantity"),
                    rs.getInt("request_count")
                );
                
                need.setStartDate(rs.getDate("start_date"));
                need.setEndDate(rs.getDate("end_date"));
                need.setStatus(rs.getString("status"));
                need.setDeliveryStatus(rs.getString("delivery_status"));
                
                return need;
            }
        }
        
        return null;
    }
    
    private List<Map<String, Object>> getWarehousesByFruit(Connection conn, int fruitId, String warehouseType) throws SQLException {
        List<Map<String, Object>> warehouses = new ArrayList<>();
        String sql = "SELECT w.id, w.name, w.type, w.city, w.country, COALESCE(wi.stock_level, 0) as stock " +
                    "FROM warehouses w " +
                    "LEFT JOIN warehouse_inventory wi ON w.type = wi.warehouse_type AND wi.fruit_id = ? " +
                    "WHERE w.type = ? " +
                    "ORDER BY w.name";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, fruitId);
            stmt.setString(2, warehouseType);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Map<String, Object> warehouse = new HashMap<>();
                warehouse.put("id", rs.getInt("id"));
                warehouse.put("name", rs.getString("name"));
                warehouse.put("type", rs.getString("type"));
                warehouse.put("city", rs.getString("city"));
                warehouse.put("country", rs.getString("country"));
                warehouse.put("stock", rs.getInt("stock"));
                
                warehouses.add(warehouse);
            }
        }
        
        return warehouses;
    }
    
    private List<Map<String, Object>> getWarehousesByCountry(Connection conn, String country, String warehouseType) throws SQLException {
        List<Map<String, Object>> warehouses = new ArrayList<>();
        String sql = "SELECT id, name, type, city, country " +
                    "FROM warehouses " +
                    "WHERE country = ? AND type = ? " +
                    "ORDER BY name";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, country);
            stmt.setString(2, warehouseType);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Map<String, Object> warehouse = new HashMap<>();
                warehouse.put("id", rs.getInt("id"));
                warehouse.put("name", rs.getString("name"));
                warehouse.put("type", rs.getString("type"));
                warehouse.put("city", rs.getString("city"));
                warehouse.put("country", rs.getString("country"));
                
                warehouses.add(warehouse);
            }
        }
        
        return warehouses;
    }
    
    private int getTotalAvailableStock(Connection conn, int fruitId) throws SQLException {
        String sql = "SELECT SUM(stock_level) as total FROM warehouse_inventory " +
                    "WHERE fruit_id = ? AND warehouse_type = 'Source'";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, fruitId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("total");
            }
        }
        
        return 0;
    }
    
    private List<Map<String, Object>> getShopRequestsByNeedId(Connection conn, int needId) throws SQLException {
        List<Map<String, Object>> shopRequests = new ArrayList<>();
        String sql = "SELECT sr.shop_id, s.name, s.city, sr.quantity, sr.request_date " +
                    "FROM shop_requests sr " +
                    "JOIN shops s ON sr.shop_id = s.id " +
                    "WHERE sr.need_id = ? " +
                    "ORDER BY s.name";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, needId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Map<String, Object> request = new HashMap<>();
                request.put("shopId", rs.getInt("shop_id"));
                request.put("name", rs.getString("name"));
                request.put("city", rs.getString("city"));
                request.put("quantity", rs.getInt("quantity"));
                request.put("requestDate", rs.getTimestamp("request_date"));
                
                shopRequests.add(request);
            }
        }
        
        return shopRequests;
    }
    
    private List<Map<String, Object>> getWarehouseStock(Connection conn) throws SQLException {
        List<Map<String, Object>> stock = new ArrayList<>();
        String sql = "SELECT f.fruit_name as fruit_name, f.source_location as source_city, f.country, " +
                    "SUM(CASE WHEN wi.warehouse_type = 'Central' THEN wi.stock_level ELSE 0 END) as central_stock, " +
                    "SUM(CASE WHEN wi.warehouse_type = 'Source' THEN wi.stock_level ELSE 0 END) as source_stock, " +
                    "MAX(wi.last_updated) as last_updated " +
                    "FROM fruits f " +
                    "LEFT JOIN warehouse_inventory wi ON f.fruit_id = wi.fruit_id " +
                    "GROUP BY f.fruit_name, f.source_location, f.country " +
                    "ORDER BY f.fruit_name";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Map<String, Object> item = new HashMap<>();
                item.put("fruitName", rs.getString("fruit_name"));
                item.put("sourceCity", rs.getString("source_city"));
                item.put("country", rs.getString("country"));
                item.put("centralStock", rs.getInt("central_stock"));
                item.put("sourceStock", rs.getInt("source_stock"));
                item.put("lastUpdated", rs.getTimestamp("last_updated"));
                
                stock.add(item);
            }
        }
        
        return stock;
    }
    
    private void createDelivery(HttpServletRequest request, HttpServletResponse response, Connection conn) 
            throws ServletException, IOException, SQLException {
        // 创建配送实现
        String deliveryType = request.getParameter("deliveryType");
        String sourceLocationStr = request.getParameter("sourceLocation");
        String destinationLocationStr = request.getParameter("destinationLocation");
        String fruitIdStr = request.getParameter("fruitId");
        String quantityStr = request.getParameter("quantity");
        
        if (sourceLocationStr == null || destinationLocationStr == null || 
            fruitIdStr == null || quantityStr == null) {
            request.setAttribute("message", "All fields are required");
            request.setAttribute("messageType", "error");
            viewDeliveries(request, response, conn);
            return;
        }
        
        int sourceId = Integer.parseInt(sourceLocationStr);
        int destinationId = Integer.parseInt(destinationLocationStr);
        int fruitId = Integer.parseInt(fruitIdStr);
        int quantity = Integer.parseInt(quantityStr);
        
        // 获取仓库信息
        String sourceLocation = "";
        String destinationLocation = "";
        
        String warehouseSql = "SELECT name FROM warehouses WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(warehouseSql)) {
            stmt.setInt(1, sourceId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                sourceLocation = rs.getString("name");
            }
        }
        
        try (PreparedStatement stmt = conn.prepareStatement(warehouseSql)) {
            stmt.setInt(1, destinationId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                destinationLocation = rs.getString("name");
            }
        }
        
        // 创建配送记录
        String insertSql = "INSERT INTO deliveries (source_location, destination_location, fruit_id, quantity, status, created_by) " +
                         "VALUES (?, ?, ?, ?, 'Pending', ?)";
        
        try (PreparedStatement stmt = conn.prepareStatement(insertSql)) {
            stmt.setString(1, sourceLocation);
            stmt.setString(2, destinationLocation);
            stmt.setInt(3, fruitId);
            stmt.setInt(4, quantity);
            stmt.setInt(5, (Integer) request.getSession().getAttribute("employeeId"));
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                request.setAttribute("message", "Delivery created successfully");
                request.setAttribute("messageType", "success");
            } else {
                request.setAttribute("message", "Failed to create delivery");
                request.setAttribute("messageType", "error");
            }
        }
        
        viewDeliveries(request, response, conn);
    }
    
    private void startDelivery(HttpServletRequest request, HttpServletResponse response, Connection conn) 
            throws ServletException, IOException, SQLException {
        // 开始配送实现
        String deliveryIdStr = request.getParameter("deliveryId");
        if (deliveryIdStr == null || deliveryIdStr.isEmpty()) {
            request.setAttribute("message", "Delivery ID is required");
            request.setAttribute("messageType", "error");
            viewDeliveries(request, response, conn);
            return;
        }
        
        int deliveryId = Integer.parseInt(deliveryIdStr);
        
        // 更新配送状态
        String updateSql = "UPDATE deliveries SET status = 'In Transit', ship_date = CURRENT_TIMESTAMP WHERE delivery_id = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(updateSql)) {
            stmt.setInt(1, deliveryId);
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                request.setAttribute("message", "Delivery started successfully");
                request.setAttribute("messageType", "success");
            } else {
                request.setAttribute("message", "Failed to start delivery");
                request.setAttribute("messageType", "error");
            }
        }
        
        viewDeliveries(request, response, conn);
    }
    
    private void completeDelivery(HttpServletRequest request, HttpServletResponse response, Connection conn) 
            throws ServletException, IOException, SQLException {
        // 完成配送实现
        String deliveryIdStr = request.getParameter("deliveryId");
        if (deliveryIdStr == null || deliveryIdStr.isEmpty()) {
            request.setAttribute("message", "Delivery ID is required");
            request.setAttribute("messageType", "error");
            viewDeliveries(request, response, conn);
            return;
        }
        
        int deliveryId = Integer.parseInt(deliveryIdStr);
        
        // 更新配送状态
        String updateSql = "UPDATE deliveries SET status = 'Delivered', delivery_date = CURRENT_TIMESTAMP WHERE delivery_id = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(updateSql)) {
            stmt.setInt(1, deliveryId);
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                request.setAttribute("message", "Delivery completed successfully");
                request.setAttribute("messageType", "success");
            } else {
                request.setAttribute("message", "Failed to complete delivery");
                request.setAttribute("messageType", "error");
            }
        }
        
        viewDeliveries(request, response, conn);
    }
    
    private void processArrangeDelivery(HttpServletRequest request, HttpServletResponse response, Connection conn) 
            throws ServletException, IOException, SQLException {
        // 处理安排配送实现
        String needIdStr = request.getParameter("needId");
        String sourceWarehouseStr = request.getParameter("sourceWarehouse");
        String destinationWarehouseStr = request.getParameter("destinationWarehouse");
        String quantityStr = request.getParameter("quantity");
        String estimatedDeliveryDaysStr = request.getParameter("estimatedDeliveryDays");
        String notes = request.getParameter("notes");
        
        if (needIdStr == null || sourceWarehouseStr == null || destinationWarehouseStr == null || 
            quantityStr == null || estimatedDeliveryDaysStr == null) {
            request.setAttribute("message", "All fields are required");
            request.setAttribute("messageType", "error");
            arrangeDelivery(request, response, conn);
            return;
        }
        
        int needId = Integer.parseInt(needIdStr);
        int sourceWarehouseId = Integer.parseInt(sourceWarehouseStr);
        int destinationWarehouseId = Integer.parseInt(destinationWarehouseStr);
        int quantity = Integer.parseInt(quantityStr);
        
        // 获取需求信息
        Need need = getNeedById(conn, needId);
        if (need == null) {
            request.setAttribute("message", "Need not found");
            request.setAttribute("messageType", "error");
            arrangeDelivery(request, response, conn);
            return;
        }
        
        // 获取仓库信息
        String sourceLocation = "";
        String destinationLocation = "";
        
        String warehouseSql = "SELECT name FROM warehouses WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(warehouseSql)) {
            stmt.setInt(1, sourceWarehouseId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                sourceLocation = rs.getString("name");
            }
        }
        
        try (PreparedStatement stmt = conn.prepareStatement(warehouseSql)) {
            stmt.setInt(1, destinationWarehouseId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                destinationLocation = rs.getString("name");
            }
        }
        
        // 创建配送记录
        String insertSql = "INSERT INTO deliveries (source_location, destination_location, fruit_id, quantity, need_id, status, notes, created_by) " +
                         "VALUES (?, ?, ?, ?, ?, 'Pending', ?, ?)";
        
        try (PreparedStatement stmt = conn.prepareStatement(insertSql)) {
            stmt.setString(1, sourceLocation);
            stmt.setString(2, destinationLocation);
            stmt.setInt(3, need.getFruitId());
            stmt.setInt(4, quantity);
            stmt.setInt(5, needId);
            stmt.setString(6, notes);
            stmt.setInt(7, (Integer) request.getSession().getAttribute("employeeId"));
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                // 更新需求状态
                String updateSql = "UPDATE country_needs SET delivery_status = 'Shipped' WHERE id = ?";
                try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                    updateStmt.setInt(1, needId);
                    updateStmt.executeUpdate();
                }
                
                request.setAttribute("message", "Delivery arranged successfully");
                request.setAttribute("messageType", "success");
            } else {
                request.setAttribute("message", "Failed to arrange delivery");
                request.setAttribute("messageType", "error");
            }
        }
        
        viewTotalNeeds(request, response, conn);
    }
} 