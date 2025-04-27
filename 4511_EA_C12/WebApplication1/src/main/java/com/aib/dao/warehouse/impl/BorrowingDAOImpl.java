package com.aib.dao.warehouse.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.aib.dao.warehouse.BorrowingDAO;
import com.aib.model.warehouse.Borrowing;
import com.aib.util.DBUtil;

public class BorrowingDAOImpl implements BorrowingDAO {

    @Override
    public Borrowing findById(int borrowingId) throws SQLException {
        String sql = "SELECT b.*, f.fruit_name, w.warehouse_location, s.full_name as borrower_name " +
                     "FROM borrowings b " +
                     "JOIN fruits f ON b.fruit_id = f.fruit_id " +
                     "JOIN warehouse_staff w ON b.warehouse_id = w.staff_id " +
                     "JOIN warehouse_staff s ON b.borrower_id = s.staff_id " +
                     "WHERE b.borrowing_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, borrowingId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToBorrowing(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<Borrowing> findAll() throws SQLException {
        List<Borrowing> borrowingList = new ArrayList<>();
        String sql = "SELECT b.*, f.fruit_name, w.warehouse_location, s.full_name as borrower_name " +
                     "FROM borrowings b " +
                     "JOIN fruits f ON b.fruit_id = f.fruit_id " +
                     "JOIN warehouse_staff w ON b.warehouse_id = w.staff_id " +
                     "JOIN warehouse_staff s ON b.borrower_id = s.staff_id " +
                     "ORDER BY b.borrow_date DESC";
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                borrowingList.add(mapResultSetToBorrowing(rs));
            }
        }
        return borrowingList;
    }

    @Override
    public List<Borrowing> findByWarehouse(int warehouseId) throws SQLException {
        List<Borrowing> borrowingList = new ArrayList<>();
        String sql = "SELECT b.*, f.fruit_name, w.warehouse_location, s.full_name as borrower_name " +
                     "FROM borrowings b " +
                     "JOIN fruits f ON b.fruit_id = f.fruit_id " +
                     "JOIN warehouse_staff w ON b.warehouse_id = w.staff_id " +
                     "JOIN warehouse_staff s ON b.borrower_id = s.staff_id " +
                     "WHERE b.warehouse_id = ? " +
                     "ORDER BY b.borrow_date DESC";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, warehouseId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    borrowingList.add(mapResultSetToBorrowing(rs));
                }
            }
        }
        return borrowingList;
    }

    @Override
    public List<Borrowing> findByBorrower(int borrowerId) throws SQLException {
        List<Borrowing> borrowingList = new ArrayList<>();
        String sql = "SELECT b.*, f.fruit_name, w.warehouse_location, s.full_name as borrower_name " +
                     "FROM borrowings b " +
                     "JOIN fruits f ON b.fruit_id = f.fruit_id " +
                     "JOIN warehouse_staff w ON b.warehouse_id = w.staff_id " +
                     "JOIN warehouse_staff s ON b.borrower_id = s.staff_id " +
                     "WHERE b.borrower_id = ? " +
                     "ORDER BY b.borrow_date DESC";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, borrowerId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    borrowingList.add(mapResultSetToBorrowing(rs));
                }
            }
        }
        return borrowingList;
    }

    @Override
    public List<Borrowing> findByStatus(String status) throws SQLException {
        List<Borrowing> borrowingList = new ArrayList<>();
        String sql = "SELECT b.*, f.fruit_name, w.warehouse_location, s.full_name as borrower_name " +
                     "FROM borrowings b " +
                     "JOIN fruits f ON b.fruit_id = f.fruit_id " +
                     "JOIN warehouse_staff w ON b.warehouse_id = w.staff_id " +
                     "JOIN warehouse_staff s ON b.borrower_id = s.staff_id " +
                     "WHERE b.status = ? " +
                     "ORDER BY b.borrow_date DESC";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    borrowingList.add(mapResultSetToBorrowing(rs));
                }
            }
        }
        return borrowingList;
    }

    @Override
    public List<Borrowing> findPendingBorrowings() throws SQLException {
        return findByStatus("pending");
    }

    @Override
    public List<Borrowing> findOverdueBorrowings() throws SQLException {
        List<Borrowing> borrowingList = new ArrayList<>();
        String sql = "SELECT b.*, f.fruit_name, w.warehouse_location, s.full_name as borrower_name " +
                     "FROM borrowings b " +
                     "JOIN fruits f ON b.fruit_id = f.fruit_id " +
                     "JOIN warehouse_staff w ON b.warehouse_id = w.staff_id " +
                     "JOIN warehouse_staff s ON b.borrower_id = s.staff_id " +
                     "WHERE b.expected_return_date < NOW() AND b.status = 'borrowed' " +
                     "ORDER BY b.expected_return_date ASC";
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                borrowingList.add(mapResultSetToBorrowing(rs));
            }
        }
        return borrowingList;
    }

    @Override
    public void save(Borrowing borrowing) throws SQLException {
        String sql = "INSERT INTO borrowings (fruit_id, inventory_id, warehouse_id, borrower_id, " +
                     "quantity, expected_return_date, status, purpose, remarks) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, borrowing.getFruitId());
            stmt.setInt(2, borrowing.getInventoryId());
            stmt.setInt(3, borrowing.getWarehouseId());
            stmt.setInt(4, borrowing.getBorrowerId());
            stmt.setInt(5, borrowing.getQuantity());
            stmt.setTimestamp(6, borrowing.getExpectedReturnDate());
            stmt.setString(7, borrowing.getStatus() != null ? borrowing.getStatus() : "pending");
            stmt.setString(8, borrowing.getPurpose());
            stmt.setString(9, borrowing.getRemarks());
            
            stmt.executeUpdate();
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    borrowing.setBorrowingId(generatedKeys.getInt(1));
                }
            }
        }
    }

    @Override
    public void update(Borrowing borrowing) throws SQLException {
        String sql = "UPDATE borrowings " +
                     "SET fruit_id = ?, inventory_id = ?, warehouse_id = ?, borrower_id = ?, " +
                     "quantity = ?, borrow_date = ?, expected_return_date = ?, actual_return_date = ?, " +
                     "status = ?, purpose = ?, remarks = ? " +
                     "WHERE borrowing_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, borrowing.getFruitId());
            stmt.setInt(2, borrowing.getInventoryId());
            stmt.setInt(3, borrowing.getWarehouseId());
            stmt.setInt(4, borrowing.getBorrowerId());
            stmt.setInt(5, borrowing.getQuantity());
            stmt.setTimestamp(6, borrowing.getBorrowDate());
            stmt.setTimestamp(7, borrowing.getExpectedReturnDate());
            stmt.setTimestamp(8, borrowing.getActualReturnDate());
            stmt.setString(9, borrowing.getStatus());
            stmt.setString(10, borrowing.getPurpose());
            stmt.setString(11, borrowing.getRemarks());
            stmt.setInt(12, borrowing.getBorrowingId());
            
            stmt.executeUpdate();
        }
    }

    @Override
    public void updateStatus(int borrowingId, String status) throws SQLException {
        String sql = "UPDATE borrowings SET status = ? WHERE borrowing_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, borrowingId);
            
            stmt.executeUpdate();
        }
    }

    @Override
    public void updateActualReturnDate(int borrowingId, Timestamp actualReturnDate) throws SQLException {
        String sql = "UPDATE borrowings SET actual_return_date = ?, status = 'returned' WHERE borrowing_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setTimestamp(1, actualReturnDate);
            stmt.setInt(2, borrowingId);
            
            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(int borrowingId) throws SQLException {
        String sql = "DELETE FROM borrowings WHERE borrowing_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, borrowingId);
            stmt.executeUpdate();
        }
    }
    
    private Borrowing mapResultSetToBorrowing(ResultSet rs) throws SQLException {
        Borrowing borrowing = new Borrowing();
        borrowing.setBorrowingId(rs.getInt("borrowing_id"));
        borrowing.setFruitId(rs.getInt("fruit_id"));
        borrowing.setInventoryId(rs.getInt("inventory_id"));
        borrowing.setWarehouseId(rs.getInt("warehouse_id"));
        borrowing.setBorrowerId(rs.getInt("borrower_id"));
        borrowing.setQuantity(rs.getInt("quantity"));
        borrowing.setBorrowDate(rs.getTimestamp("borrow_date"));
        borrowing.setExpectedReturnDate(rs.getTimestamp("expected_return_date"));
        borrowing.setActualReturnDate(rs.getTimestamp("actual_return_date"));
        borrowing.setStatus(rs.getString("status"));
        borrowing.setPurpose(rs.getString("purpose"));
        borrowing.setRemarks(rs.getString("remarks"));
        
        // 连接字段
        borrowing.setFruitName(rs.getString("fruit_name"));
        borrowing.setWarehouseLocation(rs.getString("warehouse_location"));
        borrowing.setBorrowerName(rs.getString("borrower_name"));
        
        return borrowing;
    }
} 