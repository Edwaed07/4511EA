package db;

import model.Fruit;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class FruitDAO {
    
    // 添加新水果
    public static int addFruit(Fruit fruit) throws ClassNotFoundException, SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int generatedId = -1;
        
        try {
            conn = DBConnection.getConnection();
            String sql = "INSERT INTO fruits (name, source_country, description, active) VALUES (?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, fruit.getName());
            pstmt.setString(2, fruit.getSourceCountry());
            pstmt.setString(3, fruit.getDescription());
            pstmt.setBoolean(4, fruit.isActive());
            
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
    
    // 获取所有水果
    public static List<Fruit> getAllFruits() throws ClassNotFoundException, SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Fruit> fruits = new ArrayList<>();
        
        try {
            conn = DBConnection.getConnection();
            String sql = "SELECT * FROM fruits WHERE active = true";
            pstmt = conn.prepareStatement(sql);
            
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Fruit fruit = new Fruit();
                fruit.setId(rs.getInt("id"));
                fruit.setName(rs.getString("name"));
                fruit.setSourceCountry(rs.getString("source_country"));
                fruit.setDescription(rs.getString("description"));
                fruit.setActive(rs.getBoolean("active"));
                fruits.add(fruit);
            }
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            DBConnection.closeConnection(conn);
        }
        
        return fruits;
    }
    
    // 根据ID获取水果
    public static Fruit getFruitById(int id) throws ClassNotFoundException, SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Fruit fruit = null;
        
        try {
            conn = DBConnection.getConnection();
            String sql = "SELECT * FROM fruits WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            
            rs = pstmt.executeQuery();
            if (rs.next()) {
                fruit = new Fruit();
                fruit.setId(rs.getInt("id"));
                fruit.setName(rs.getString("name"));
                fruit.setSourceCountry(rs.getString("source_country"));
                fruit.setDescription(rs.getString("description"));
                fruit.setActive(rs.getBoolean("active"));
            }
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            DBConnection.closeConnection(conn);
        }
        
        return fruit;
    }
    
    // 根据源国家获取水果
    public static List<Fruit> getFruitsBySourceCountry(String sourceCountry) throws ClassNotFoundException, SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Fruit> fruits = new ArrayList<>();
        
        try {
            conn = DBConnection.getConnection();
            String sql = "SELECT * FROM fruits WHERE source_country = ? AND active = true";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, sourceCountry);
            
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Fruit fruit = new Fruit();
                fruit.setId(rs.getInt("id"));
                fruit.setName(rs.getString("name"));
                fruit.setSourceCountry(rs.getString("source_country"));
                fruit.setDescription(rs.getString("description"));
                fruit.setActive(rs.getBoolean("active"));
                fruits.add(fruit);
            }
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            DBConnection.closeConnection(conn);
        }
        
        return fruits;
    }
    
    // 更新水果信息
    public static boolean updateFruit(Fruit fruit) throws ClassNotFoundException, SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;
        
        try {
            conn = DBConnection.getConnection();
            String sql = "UPDATE fruits SET name = ?, source_country = ?, description = ?, active = ? WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, fruit.getName());
            pstmt.setString(2, fruit.getSourceCountry());
            pstmt.setString(3, fruit.getDescription());
            pstmt.setBoolean(4, fruit.isActive());
            pstmt.setInt(5, fruit.getId());
            
            int rowsAffected = pstmt.executeUpdate();
            success = (rowsAffected > 0);
        } finally {
            if (pstmt != null) pstmt.close();
            DBConnection.closeConnection(conn);
        }
        
        return success;
    }
} 