package db;

import model.Delivery;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DeliveryDAO {
    
    // 添加新交付记录
    public static int addDelivery(Delivery delivery) throws ClassNotFoundException, SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int generatedId = -1;
        
        try {
            conn = DBConnection.getConnection();
            String sql = "INSERT INTO deliveries (fruit_id, source_location, destination_location, destination_type, " +
                         "quantity, delivery_date, status, reserve_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, delivery.getFruitId());
            pstmt.setString(2, delivery.getSourceLocation());
            pstmt.setString(3, delivery.getDestinationLocation());
            pstmt.setString(4, delivery.getDestinationType());
            pstmt.setInt(5, delivery.getQuantity());
            pstmt.setTimestamp(6, new Timestamp(delivery.getDeliveryDate().getTime()));
            pstmt.setString(7, delivery.getStatus());
            pstmt.setInt(8, delivery.getReserveId());
            
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
    
    // 根据ID获取交付记录
    public static Delivery getDeliveryById(int id) throws ClassNotFoundException, SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Delivery delivery = null;
        
        try {
            conn = DBConnection.getConnection();
            String sql = "SELECT * FROM deliveries WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            
            rs = pstmt.executeQuery();
            if (rs.next()) {
                delivery = new Delivery();
                delivery.setId(rs.getInt("id"));
                delivery.setFruitId(rs.getInt("fruit_id"));
                delivery.setSourceLocation(rs.getString("source_location"));
                delivery.setDestinationLocation(rs.getString("destination_location"));
                delivery.setDestinationType(rs.getString("destination_type"));
                delivery.setQuantity(rs.getInt("quantity"));
                delivery.setDeliveryDate(rs.getTimestamp("delivery_date"));
                delivery.setStatus(rs.getString("status"));
                delivery.setReserveId(rs.getInt("reserve_id"));
            }
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            DBConnection.closeConnection(conn);
        }
        
        return delivery;
    }
    
    // 更新交付记录状态
    public static boolean updateDeliveryStatus(int deliveryId, String status) throws ClassNotFoundException, SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;
        
        try {
            conn = DBConnection.getConnection();
            String sql = "UPDATE deliveries SET status = ? WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, status);
            pstmt.setInt(2, deliveryId);
            
            int rowsAffected = pstmt.executeUpdate();
            success = (rowsAffected > 0);
        } finally {
            if (pstmt != null) pstmt.close();
            DBConnection.closeConnection(conn);
        }
        
        return success;
    }
    
    // 获取特定目的地的所有交付记录
    public static List<Delivery> getDeliveriesByDestination(String destinationType, String destinationLocation) throws ClassNotFoundException, SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Delivery> deliveries = new ArrayList<>();
        
        try {
            conn = DBConnection.getConnection();
            String sql = "SELECT * FROM deliveries WHERE destination_type = ? AND destination_location = ? ORDER BY delivery_date DESC";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, destinationType);
            pstmt.setString(2, destinationLocation);
            
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Delivery delivery = new Delivery();
                delivery.setId(rs.getInt("id"));
                delivery.setFruitId(rs.getInt("fruit_id"));
                delivery.setSourceLocation(rs.getString("source_location"));
                delivery.setDestinationLocation(rs.getString("destination_location"));
                delivery.setDestinationType(rs.getString("destination_type"));
                delivery.setQuantity(rs.getInt("quantity"));
                delivery.setDeliveryDate(rs.getTimestamp("delivery_date"));
                delivery.setStatus(rs.getString("status"));
                delivery.setReserveId(rs.getInt("reserve_id"));
                deliveries.add(delivery);
            }
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            DBConnection.closeConnection(conn);
        }
        
        return deliveries;
    }
    
    // 获取特定源头的所有交付记录
    public static List<Delivery> getDeliveriesBySource(String sourceLocation) throws ClassNotFoundException, SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Delivery> deliveries = new ArrayList<>();
        
        try {
            conn = DBConnection.getConnection();
            String sql = "SELECT * FROM deliveries WHERE source_location = ? ORDER BY delivery_date DESC";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, sourceLocation);
            
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Delivery delivery = new Delivery();
                delivery.setId(rs.getInt("id"));
                delivery.setFruitId(rs.getInt("fruit_id"));
                delivery.setSourceLocation(rs.getString("source_location"));
                delivery.setDestinationLocation(rs.getString("destination_location"));
                delivery.setDestinationType(rs.getString("destination_type"));
                delivery.setQuantity(rs.getInt("quantity"));
                delivery.setDeliveryDate(rs.getTimestamp("delivery_date"));
                delivery.setStatus(rs.getString("status"));
                delivery.setReserveId(rs.getInt("reserve_id"));
                deliveries.add(delivery);
            }
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            DBConnection.closeConnection(conn);
        }
        
        return deliveries;
    }
    
    // 根据预订ID获取交付记录
    public static List<Delivery> getDeliveriesByReserveId(int reserveId) throws ClassNotFoundException, SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Delivery> deliveries = new ArrayList<>();
        
        try {
            conn = DBConnection.getConnection();
            String sql = "SELECT * FROM deliveries WHERE reserve_id = ? ORDER BY delivery_date DESC";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, reserveId);
            
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Delivery delivery = new Delivery();
                delivery.setId(rs.getInt("id"));
                delivery.setFruitId(rs.getInt("fruit_id"));
                delivery.setSourceLocation(rs.getString("source_location"));
                delivery.setDestinationLocation(rs.getString("destination_location"));
                delivery.setDestinationType(rs.getString("destination_type"));
                delivery.setQuantity(rs.getInt("quantity"));
                delivery.setDeliveryDate(rs.getTimestamp("delivery_date"));
                delivery.setStatus(rs.getString("status"));
                delivery.setReserveId(rs.getInt("reserve_id"));
                deliveries.add(delivery);
            }
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            DBConnection.closeConnection(conn);
        }
        
        return deliveries;
    }
} 