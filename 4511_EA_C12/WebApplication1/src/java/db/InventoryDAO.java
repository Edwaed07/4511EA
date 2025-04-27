package db;

import model.Inventory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class InventoryDAO {
    
    // 添加新库存记录
    public static int addInventory(Inventory inventory) throws ClassNotFoundException, SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int generatedId = -1;
        
        try {
            conn = DBConnection.getConnection();
            String sql = "INSERT INTO inventory (fruit_id, location_type, location_name, quantity, last_updated) VALUES (?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, inventory.getFruitId());
            pstmt.setString(2, inventory.getLocationType());
            pstmt.setString(3, inventory.getLocationName());
            pstmt.setInt(4, inventory.getQuantity());
            pstmt.setTimestamp(5, new Timestamp(inventory.getLastUpdated().getTime()));
            
            pstmt.executeUpdate();
            
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                generatedId = rs.getInt(1);
            }
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            DBConnection.closeConnection(conn);
        }
        
        return generatedId;
    }
    
    // 根据水果ID和位置获取库存
    public static Inventory getInventory(int fruitId, String locationType, String locationName) throws ClassNotFoundException, SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Inventory inventory = null;
        
        try {
            conn = DBConnection.getConnection();
            String sql = "SELECT * FROM inventory WHERE fruit_id = ? AND location_type = ? AND location_name = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, fruitId);
            pstmt.setString(2, locationType);
            pstmt.setString(3, locationName);
            
            rs = pstmt.executeQuery();
            if (rs.next()) {
                inventory = new Inventory();
                inventory.setId(rs.getInt("id"));
                inventory.setFruitId(rs.getInt("fruit_id"));
                inventory.setLocationType(rs.getString("location_type"));
                inventory.setLocationName(rs.getString("location_name"));
                inventory.setQuantity(rs.getInt("quantity"));
                inventory.setLastUpdated(new Date(rs.getTimestamp("last_updated").getTime()));
            }
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            DBConnection.closeConnection(conn);
        }
        
        return inventory;
    }
    
    // 根据位置获取所有库存
    public static List<Inventory> getInventoryByLocation(String locationType, String locationName) throws ClassNotFoundException, SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Inventory> inventoryList = new ArrayList<>();
        
        try {
            conn = DBConnection.getConnection();
            String sql = "SELECT * FROM inventory WHERE location_type = ? AND location_name = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, locationType);
            pstmt.setString(2, locationName);
            
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Inventory inventory = new Inventory();
                inventory.setId(rs.getInt("id"));
                inventory.setFruitId(rs.getInt("fruit_id"));
                inventory.setLocationType(rs.getString("location_type"));
                inventory.setLocationName(rs.getString("location_name"));
                inventory.setQuantity(rs.getInt("quantity"));
                inventory.setLastUpdated(new Date(rs.getTimestamp("last_updated").getTime()));
                inventoryList.add(inventory);
            }
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            DBConnection.closeConnection(conn);
        }
        
        return inventoryList;
    }
    
    // 更新库存数量
    public static boolean updateInventoryQuantity(int fruitId, String locationType, String locationName, int quantity) throws ClassNotFoundException, SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;
        
        try {
            conn = DBConnection.getConnection();
            String sql = "UPDATE inventory SET quantity = ?, last_updated = ? WHERE fruit_id = ? AND location_type = ? AND location_name = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, quantity);
            pstmt.setTimestamp(2, new Timestamp(new Date().getTime()));
            pstmt.setInt(3, fruitId);
            pstmt.setString(4, locationType);
            pstmt.setString(5, locationName);
            
            int rowsAffected = pstmt.executeUpdate();
            success = (rowsAffected > 0);
            
            // 如果没有找到记录，创建新记录
            if (!success) {
                Inventory inventory = new Inventory();
                inventory.setFruitId(fruitId);
                inventory.setLocationType(locationType);
                inventory.setLocationName(locationName);
                inventory.setQuantity(quantity);
                inventory.setLastUpdated(new Date());
                addInventory(inventory);
                success = true;
            }
        } finally {
            if (pstmt != null) pstmt.close();
            DBConnection.closeConnection(conn);
        }
        
        return success;
    }
    
    // 根据水果ID获取所有库存地点
    public static List<Inventory> getAllInventoryByFruitId(int fruitId) throws ClassNotFoundException, SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Inventory> inventoryList = new ArrayList<>();
        
        try {
            conn = DBConnection.getConnection();
            String sql = "SELECT * FROM inventory WHERE fruit_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, fruitId);
            
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Inventory inventory = new Inventory();
                inventory.setId(rs.getInt("id"));
                inventory.setFruitId(rs.getInt("fruit_id"));
                inventory.setLocationType(rs.getString("location_type"));
                inventory.setLocationName(rs.getString("location_name"));
                inventory.setQuantity(rs.getInt("quantity"));
                inventory.setLastUpdated(new Date(rs.getTimestamp("last_updated").getTime()));
                inventoryList.add(inventory);
            }
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            DBConnection.closeConnection(conn);
        }
        
        return inventoryList;
    }
} 