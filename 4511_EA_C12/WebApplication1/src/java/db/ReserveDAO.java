package db;

import model.Reserve;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReserveDAO {
    
    // 添加新预订
    public static int addReserve(Reserve reserve) throws ClassNotFoundException, SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int generatedId = -1;
        
        try {
            conn = DBConnection.getConnection();
            String sql = "INSERT INTO reserves (fruit_id, user_id, shop_name, city, country, quantity, reserve_date, delivery_date, status) " +
                         "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, reserve.getFruitId());
            pstmt.setInt(2, reserve.getUserId());
            pstmt.setString(3, reserve.getShopName());
            pstmt.setString(4, reserve.getCity());
            pstmt.setString(5, reserve.getCountry());
            pstmt.setInt(6, reserve.getQuantity());
            pstmt.setTimestamp(7, new Timestamp(reserve.getReserveDate().getTime()));
            pstmt.setTimestamp(8, reserve.getDeliveryDate() != null ? new Timestamp(reserve.getDeliveryDate().getTime()) : null);
            pstmt.setString(9, reserve.getStatus());
            
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
    
    // 根据ID获取预订
    public static Reserve getReserveById(int id) throws ClassNotFoundException, SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Reserve reserve = null;
        
        try {
            conn = DBConnection.getConnection();
            String sql = "SELECT * FROM reserves WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            
            rs = pstmt.executeQuery();
            if (rs.next()) {
                reserve = new Reserve();
                reserve.setId(rs.getInt("id"));
                reserve.setFruitId(rs.getInt("fruit_id"));
                reserve.setUserId(rs.getInt("user_id"));
                reserve.setShopName(rs.getString("shop_name"));
                reserve.setCity(rs.getString("city"));
                reserve.setCountry(rs.getString("country"));
                reserve.setQuantity(rs.getInt("quantity"));
                reserve.setReserveDate(rs.getTimestamp("reserve_date"));
                reserve.setDeliveryDate(rs.getTimestamp("delivery_date"));
                reserve.setStatus(rs.getString("status"));
            }
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            DBConnection.closeConnection(conn);
        }
        
        return reserve;
    }
    
    // 获取按国家分组的所有待处理预订
    public static List<Reserve> getPendingReservesByCountry(String country) throws ClassNotFoundException, SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Reserve> reserves = new ArrayList<>();
        
        try {
            conn = DBConnection.getConnection();
            String sql = "SELECT * FROM reserves WHERE country = ? AND status = 'pending' ORDER BY reserve_date";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, country);
            
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Reserve reserve = new Reserve();
                reserve.setId(rs.getInt("id"));
                reserve.setFruitId(rs.getInt("fruit_id"));
                reserve.setUserId(rs.getInt("user_id"));
                reserve.setShopName(rs.getString("shop_name"));
                reserve.setCity(rs.getString("city"));
                reserve.setCountry(rs.getString("country"));
                reserve.setQuantity(rs.getInt("quantity"));
                reserve.setReserveDate(rs.getTimestamp("reserve_date"));
                reserve.setDeliveryDate(rs.getTimestamp("delivery_date"));
                reserve.setStatus(rs.getString("status"));
                reserves.add(reserve);
            }
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            DBConnection.closeConnection(conn);
        }
        
        return reserves;
    }
    
    // 更新预订状态
    public static boolean updateReserveStatus(int reserveId, String status) throws ClassNotFoundException, SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;
        
        try {
            conn = DBConnection.getConnection();
            String sql = "UPDATE reserves SET status = ? WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, status);
            pstmt.setInt(2, reserveId);
            
            int rowsAffected = pstmt.executeUpdate();
            success = (rowsAffected > 0);
        } finally {
            if (pstmt != null) pstmt.close();
            DBConnection.closeConnection(conn);
        }
        
        return success;
    }
    
    // 根据水果ID和国家获取总需求量
    public static int getTotalReserveQuantityByFruitAndCountry(int fruitId, String country) throws ClassNotFoundException, SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int totalQuantity = 0;
        
        try {
            conn = DBConnection.getConnection();
            String sql = "SELECT SUM(quantity) as total FROM reserves WHERE fruit_id = ? AND country = ? AND status = 'approved'";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, fruitId);
            pstmt.setString(2, country);
            
            rs = pstmt.executeQuery();
            if (rs.next()) {
                totalQuantity = rs.getInt("total");
            }
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            DBConnection.closeConnection(conn);
        }
        
        return totalQuantity;
    }
    
    // 获取所有由特定用户创建的预订
    public static List<Reserve> getReservesByUserId(int userId) throws ClassNotFoundException, SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Reserve> reserves = new ArrayList<>();
        
        try {
            conn = DBConnection.getConnection();
            String sql = "SELECT * FROM reserves WHERE user_id = ? ORDER BY reserve_date DESC";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Reserve reserve = new Reserve();
                reserve.setId(rs.getInt("id"));
                reserve.setFruitId(rs.getInt("fruit_id"));
                reserve.setUserId(rs.getInt("user_id"));
                reserve.setShopName(rs.getString("shop_name"));
                reserve.setCity(rs.getString("city"));
                reserve.setCountry(rs.getString("country"));
                reserve.setQuantity(rs.getInt("quantity"));
                reserve.setReserveDate(rs.getTimestamp("reserve_date"));
                reserve.setDeliveryDate(rs.getTimestamp("delivery_date"));
                reserve.setStatus(rs.getString("status"));
                reserves.add(reserve);
            }
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            DBConnection.closeConnection(conn);
        }
        
        return reserves;
    }
} 