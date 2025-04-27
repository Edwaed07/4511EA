package com.aib.dao.warehouse.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.aib.dao.warehouse.InventoryDAO;
import com.aib.model.warehouse.Inventory;
import com.aib.util.DBUtil;

public class InventoryDAOImpl implements InventoryDAO {

    @Override
    public Inventory findById(int inventoryId) throws SQLException {
        String sql = "SELECT i.*, f.fruit_name, w.warehouse_location " +
                     "FROM warehouse_inventory i " +
                     "JOIN fruits f ON i.fruit_id = f.fruit_id " +
                     "JOIN warehouse_staff w ON i.warehouse_id = w.staff_id " +
                     "WHERE i.inventory_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, inventoryId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToInventory(rs);
                }
            }
        }
        return null;
    }

    @Override
    public Inventory findByFruitAndWarehouse(int fruitId, int warehouseId) throws SQLException {
        String sql = "SELECT i.*, f.fruit_name, w.warehouse_location " +
                     "FROM warehouse_inventory i " +
                     "JOIN fruits f ON i.fruit_id = f.fruit_id " +
                     "JOIN warehouse_staff w ON i.warehouse_id = w.staff_id " +
                     "WHERE i.fruit_id = ? AND i.warehouse_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, fruitId);
            stmt.setInt(2, warehouseId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToInventory(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<Inventory> findAll() throws SQLException {
        List<Inventory> inventoryList = new ArrayList<>();
        String sql = "SELECT i.*, f.fruit_name, w.warehouse_location " +
                     "FROM warehouse_inventory i " +
                     "JOIN fruits f ON i.fruit_id = f.fruit_id " +
                     "JOIN warehouse_staff w ON i.warehouse_id = w.staff_id";
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                inventoryList.add(mapResultSetToInventory(rs));
            }
        }
        return inventoryList;
    }

    @Override
    public List<Inventory> findByWarehouse(int warehouseId) throws SQLException {
        List<Inventory> inventoryList = new ArrayList<>();
        String sql = "SELECT i.*, f.fruit_name, w.warehouse_location " +
                     "FROM warehouse_inventory i " +
                     "JOIN fruits f ON i.fruit_id = f.fruit_id " +
                     "JOIN warehouse_staff w ON i.warehouse_id = w.staff_id " +
                     "WHERE i.warehouse_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, warehouseId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    inventoryList.add(mapResultSetToInventory(rs));
                }
            }
        }
        return inventoryList;
    }

    @Override
    public List<Inventory> findByFruit(int fruitId) throws SQLException {
        List<Inventory> inventoryList = new ArrayList<>();
        String sql = "SELECT i.*, f.fruit_name, w.warehouse_location " +
                     "FROM warehouse_inventory i " +
                     "JOIN fruits f ON i.fruit_id = f.fruit_id " +
                     "JOIN warehouse_staff w ON i.warehouse_id = w.staff_id " +
                     "WHERE i.fruit_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, fruitId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    inventoryList.add(mapResultSetToInventory(rs));
                }
            }
        }
        return inventoryList;
    }

    @Override
    public List<Inventory> findLowStock() throws SQLException {
        List<Inventory> inventoryList = new ArrayList<>();
        String sql = "SELECT i.*, f.fruit_name, w.warehouse_location " +
                     "FROM warehouse_inventory i " +
                     "JOIN fruits f ON i.fruit_id = f.fruit_id " +
                     "JOIN warehouse_staff w ON i.warehouse_id = w.staff_id " +
                     "WHERE i.quantity <= i.minimum_stock";
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                inventoryList.add(mapResultSetToInventory(rs));
            }
        }
        return inventoryList;
    }

    @Override
    public void save(Inventory inventory) throws SQLException {
        String sql = "INSERT INTO warehouse_inventory (fruit_id, warehouse_id, quantity, minimum_stock, maximum_stock) " +
                     "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, inventory.getFruitId());
            stmt.setInt(2, inventory.getWarehouseId());
            stmt.setInt(3, inventory.getQuantity());
            stmt.setInt(4, inventory.getMinimumStock());
            stmt.setInt(5, inventory.getMaximumStock());
            
            stmt.executeUpdate();
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    inventory.setInventoryId(generatedKeys.getInt(1));
                }
            }
        }
    }

    @Override
    public void update(Inventory inventory) throws SQLException {
        String sql = "UPDATE warehouse_inventory " +
                     "SET fruit_id = ?, warehouse_id = ?, quantity = ?, minimum_stock = ?, maximum_stock = ? " +
                     "WHERE inventory_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, inventory.getFruitId());
            stmt.setInt(2, inventory.getWarehouseId());
            stmt.setInt(3, inventory.getQuantity());
            stmt.setInt(4, inventory.getMinimumStock());
            stmt.setInt(5, inventory.getMaximumStock());
            stmt.setInt(6, inventory.getInventoryId());
            
            stmt.executeUpdate();
        }
    }

    @Override
    public void updateQuantity(int inventoryId, int quantity) throws SQLException {
        String sql = "UPDATE warehouse_inventory SET quantity = ? WHERE inventory_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, quantity);
            stmt.setInt(2, inventoryId);
            
            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(int inventoryId) throws SQLException {
        String sql = "DELETE FROM warehouse_inventory WHERE inventory_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, inventoryId);
            stmt.executeUpdate();
        }
    }
    
    private Inventory mapResultSetToInventory(ResultSet rs) throws SQLException {
        Inventory inventory = new Inventory();
        inventory.setInventoryId(rs.getInt("inventory_id"));
        inventory.setFruitId(rs.getInt("fruit_id"));
        inventory.setWarehouseId(rs.getInt("warehouse_id"));
        inventory.setQuantity(rs.getInt("quantity"));
        inventory.setMinimumStock(rs.getInt("minimum_stock"));
        inventory.setMaximumStock(rs.getInt("maximum_stock"));
        inventory.setLastUpdated(rs.getTimestamp("last_updated"));
        
        // 连接字段
        inventory.setFruitName(rs.getString("fruit_name"));
        inventory.setWarehouseLocation(rs.getString("warehouse_location"));
        
        return inventory;
    }
} 