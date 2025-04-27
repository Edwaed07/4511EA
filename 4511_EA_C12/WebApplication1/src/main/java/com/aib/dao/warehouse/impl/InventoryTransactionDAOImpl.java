package com.aib.dao.warehouse.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.aib.dao.warehouse.InventoryTransactionDAO;
import com.aib.model.warehouse.InventoryTransaction;
import com.aib.util.DBUtil;

public class InventoryTransactionDAOImpl implements InventoryTransactionDAO {

    @Override
    public InventoryTransaction findById(int transactionId) throws SQLException {
        String sql = "SELECT t.*, f.fruit_name, w.warehouse_location, s.full_name as staff_name " +
                     "FROM inventory_transactions t " +
                     "JOIN fruits f ON t.fruit_id = f.fruit_id " +
                     "JOIN warehouse_staff w ON t.warehouse_id = w.staff_id " +
                     "JOIN warehouse_staff s ON t.staff_id = s.staff_id " +
                     "WHERE t.transaction_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, transactionId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToTransaction(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<InventoryTransaction> findAll() throws SQLException {
        List<InventoryTransaction> transactionList = new ArrayList<>();
        String sql = "SELECT t.*, f.fruit_name, w.warehouse_location, s.full_name as staff_name " +
                     "FROM inventory_transactions t " +
                     "JOIN fruits f ON t.fruit_id = f.fruit_id " +
                     "JOIN warehouse_staff w ON t.warehouse_id = w.staff_id " +
                     "JOIN warehouse_staff s ON t.staff_id = s.staff_id " +
                     "ORDER BY t.transaction_date DESC";
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                transactionList.add(mapResultSetToTransaction(rs));
            }
        }
        return transactionList;
    }

    @Override
    public List<InventoryTransaction> findByWarehouse(int warehouseId) throws SQLException {
        List<InventoryTransaction> transactionList = new ArrayList<>();
        String sql = "SELECT t.*, f.fruit_name, w.warehouse_location, s.full_name as staff_name " +
                     "FROM inventory_transactions t " +
                     "JOIN fruits f ON t.fruit_id = f.fruit_id " +
                     "JOIN warehouse_staff w ON t.warehouse_id = w.staff_id " +
                     "JOIN warehouse_staff s ON t.staff_id = s.staff_id " +
                     "WHERE t.warehouse_id = ? " +
                     "ORDER BY t.transaction_date DESC";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, warehouseId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    transactionList.add(mapResultSetToTransaction(rs));
                }
            }
        }
        return transactionList;
    }

    @Override
    public List<InventoryTransaction> findByFruit(int fruitId) throws SQLException {
        List<InventoryTransaction> transactionList = new ArrayList<>();
        String sql = "SELECT t.*, f.fruit_name, w.warehouse_location, s.full_name as staff_name " +
                     "FROM inventory_transactions t " +
                     "JOIN fruits f ON t.fruit_id = f.fruit_id " +
                     "JOIN warehouse_staff w ON t.warehouse_id = w.staff_id " +
                     "JOIN warehouse_staff s ON t.staff_id = s.staff_id " +
                     "WHERE t.fruit_id = ? " +
                     "ORDER BY t.transaction_date DESC";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, fruitId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    transactionList.add(mapResultSetToTransaction(rs));
                }
            }
        }
        return transactionList;
    }

    @Override
    public List<InventoryTransaction> findByStaff(int staffId) throws SQLException {
        List<InventoryTransaction> transactionList = new ArrayList<>();
        String sql = "SELECT t.*, f.fruit_name, w.warehouse_location, s.full_name as staff_name " +
                     "FROM inventory_transactions t " +
                     "JOIN fruits f ON t.fruit_id = f.fruit_id " +
                     "JOIN warehouse_staff w ON t.warehouse_id = w.staff_id " +
                     "JOIN warehouse_staff s ON t.staff_id = s.staff_id " +
                     "WHERE t.staff_id = ? " +
                     "ORDER BY t.transaction_date DESC";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, staffId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    transactionList.add(mapResultSetToTransaction(rs));
                }
            }
        }
        return transactionList;
    }

    @Override
    public List<InventoryTransaction> findByType(String transactionType) throws SQLException {
        List<InventoryTransaction> transactionList = new ArrayList<>();
        String sql = "SELECT t.*, f.fruit_name, w.warehouse_location, s.full_name as staff_name " +
                     "FROM inventory_transactions t " +
                     "JOIN fruits f ON t.fruit_id = f.fruit_id " +
                     "JOIN warehouse_staff w ON t.warehouse_id = w.staff_id " +
                     "JOIN warehouse_staff s ON t.staff_id = s.staff_id " +
                     "WHERE t.transaction_type = ? " +
                     "ORDER BY t.transaction_date DESC";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, transactionType);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    transactionList.add(mapResultSetToTransaction(rs));
                }
            }
        }
        return transactionList;
    }

    @Override
    public List<InventoryTransaction> findByStatus(String status) throws SQLException {
        List<InventoryTransaction> transactionList = new ArrayList<>();
        String sql = "SELECT t.*, f.fruit_name, w.warehouse_location, s.full_name as staff_name " +
                     "FROM inventory_transactions t " +
                     "JOIN fruits f ON t.fruit_id = f.fruit_id " +
                     "JOIN warehouse_staff w ON t.warehouse_id = w.staff_id " +
                     "JOIN warehouse_staff s ON t.staff_id = s.staff_id " +
                     "WHERE t.status = ? " +
                     "ORDER BY t.transaction_date DESC";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    transactionList.add(mapResultSetToTransaction(rs));
                }
            }
        }
        return transactionList;
    }

    @Override
    public List<InventoryTransaction> findRecentTransactions(int limit) throws SQLException {
        List<InventoryTransaction> transactionList = new ArrayList<>();
        String sql = "SELECT t.*, f.fruit_name, w.warehouse_location, s.full_name as staff_name " +
                     "FROM inventory_transactions t " +
                     "JOIN fruits f ON t.fruit_id = f.fruit_id " +
                     "JOIN warehouse_staff w ON t.warehouse_id = w.staff_id " +
                     "JOIN warehouse_staff s ON t.staff_id = s.staff_id " +
                     "ORDER BY t.transaction_date DESC " +
                     "LIMIT ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, limit);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    transactionList.add(mapResultSetToTransaction(rs));
                }
            }
        }
        return transactionList;
    }

    @Override
    public void save(InventoryTransaction transaction) throws SQLException {
        String sql = "INSERT INTO inventory_transactions (warehouse_id, fruit_id, transaction_type, quantity, " +
                     "source_location, destination_location, staff_id, status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, transaction.getWarehouseId());
            stmt.setInt(2, transaction.getFruitId());
            stmt.setString(3, transaction.getTransactionType());
            stmt.setInt(4, transaction.getQuantity());
            stmt.setString(5, transaction.getSourceLocation());
            stmt.setString(6, transaction.getDestinationLocation());
            stmt.setInt(7, transaction.getStaffId());
            stmt.setString(8, transaction.getStatus() != null ? transaction.getStatus() : "pending");
            
            stmt.executeUpdate();
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    transaction.setTransactionId(generatedKeys.getInt(1));
                }
            }
        }
    }

    @Override
    public void update(InventoryTransaction transaction) throws SQLException {
        String sql = "UPDATE inventory_transactions " +
                     "SET warehouse_id = ?, fruit_id = ?, transaction_type = ?, quantity = ?, " +
                     "source_location = ?, destination_location = ?, staff_id = ?, status = ? " +
                     "WHERE transaction_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, transaction.getWarehouseId());
            stmt.setInt(2, transaction.getFruitId());
            stmt.setString(3, transaction.getTransactionType());
            stmt.setInt(4, transaction.getQuantity());
            stmt.setString(5, transaction.getSourceLocation());
            stmt.setString(6, transaction.getDestinationLocation());
            stmt.setInt(7, transaction.getStaffId());
            stmt.setString(8, transaction.getStatus());
            stmt.setInt(9, transaction.getTransactionId());
            
            stmt.executeUpdate();
        }
    }

    @Override
    public void updateStatus(int transactionId, String status) throws SQLException {
        String sql = "UPDATE inventory_transactions SET status = ? WHERE transaction_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, transactionId);
            
            stmt.executeUpdate();
        }
    }

    private InventoryTransaction mapResultSetToTransaction(ResultSet rs) throws SQLException {
        InventoryTransaction transaction = new InventoryTransaction();
        transaction.setTransactionId(rs.getInt("transaction_id"));
        transaction.setWarehouseId(rs.getInt("warehouse_id"));
        transaction.setFruitId(rs.getInt("fruit_id"));
        transaction.setTransactionType(rs.getString("transaction_type"));
        transaction.setQuantity(rs.getInt("quantity"));
        transaction.setSourceLocation(rs.getString("source_location"));
        transaction.setDestinationLocation(rs.getString("destination_location"));
        transaction.setStaffId(rs.getInt("staff_id"));
        transaction.setTransactionDate(rs.getTimestamp("transaction_date"));
        transaction.setStatus(rs.getString("status"));
        
        // 连接字段
        transaction.setFruitName(rs.getString("fruit_name"));
        transaction.setWarehouseLocation(rs.getString("warehouse_location"));
        transaction.setStaffName(rs.getString("staff_name"));
        
        return transaction;
    }
} 