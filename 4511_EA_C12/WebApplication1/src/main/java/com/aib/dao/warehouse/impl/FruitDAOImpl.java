package com.aib.dao.warehouse.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.aib.dao.warehouse.FruitDAO;
import com.aib.model.warehouse.Fruit;
import com.aib.util.DBUtil;

public class FruitDAOImpl implements FruitDAO {

    @Override
    public Fruit findById(int fruitId) throws SQLException {
        String sql = "SELECT * FROM fruits WHERE fruit_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, fruitId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToFruit(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<Fruit> findAll() throws SQLException {
        List<Fruit> fruitList = new ArrayList<>();
        String sql = "SELECT * FROM fruits";
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                fruitList.add(mapResultSetToFruit(rs));
            }
        }
        return fruitList;
    }

    @Override
    public List<Fruit> findBySourceLocation(String sourceLocation) throws SQLException {
        List<Fruit> fruitList = new ArrayList<>();
        String sql = "SELECT * FROM fruits WHERE source_location = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, sourceLocation);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    fruitList.add(mapResultSetToFruit(rs));
                }
            }
        }
        return fruitList;
    }

    @Override
    public void save(Fruit fruit) throws SQLException {
        String sql = "INSERT INTO fruits (fruit_name, source_location, description) VALUES (?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, fruit.getFruitName());
            stmt.setString(2, fruit.getSourceLocation());
            stmt.setString(3, fruit.getDescription());
            
            stmt.executeUpdate();
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    fruit.setFruitId(generatedKeys.getInt(1));
                }
            }
        }
    }

    @Override
    public void update(Fruit fruit) throws SQLException {
        String sql = "UPDATE fruits SET fruit_name = ?, source_location = ?, description = ? WHERE fruit_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, fruit.getFruitName());
            stmt.setString(2, fruit.getSourceLocation());
            stmt.setString(3, fruit.getDescription());
            stmt.setInt(4, fruit.getFruitId());
            
            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(int fruitId) throws SQLException {
        String sql = "DELETE FROM fruits WHERE fruit_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, fruitId);
            stmt.executeUpdate();
        }
    }
    
    private Fruit mapResultSetToFruit(ResultSet rs) throws SQLException {
        Fruit fruit = new Fruit();
        fruit.setFruitId(rs.getInt("fruit_id"));
        fruit.setFruitName(rs.getString("fruit_name"));
        fruit.setSourceLocation(rs.getString("source_location"));
        fruit.setDescription(rs.getString("description"));
        fruit.setCreatedAt(rs.getTimestamp("created_at"));
        return fruit;
    }
} 