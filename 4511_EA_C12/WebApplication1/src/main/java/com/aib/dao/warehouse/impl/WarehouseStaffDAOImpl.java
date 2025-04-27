package com.aib.dao.warehouse.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.aib.dao.warehouse.WarehouseStaffDAO;
import com.aib.model.warehouse.WarehouseStaff;
import com.aib.util.DBUtil;

public class WarehouseStaffDAOImpl implements WarehouseStaffDAO {
    
    @Override
    public WarehouseStaff findById(int staffId) throws SQLException {
        String sql = "SELECT * FROM warehouse_staff WHERE staff_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, staffId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToStaff(rs);
                }
            }
        }
        return null;
    }

    @Override
    public WarehouseStaff findByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM warehouse_staff WHERE username = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToStaff(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<WarehouseStaff> findAll() throws SQLException {
        List<WarehouseStaff> staffList = new ArrayList<>();
        String sql = "SELECT * FROM warehouse_staff";
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                staffList.add(mapResultSetToStaff(rs));
            }
        }
        return staffList;
    }

    @Override
    public void save(WarehouseStaff staff) throws SQLException {
        String sql = "INSERT INTO warehouse_staff (username, password, full_name, warehouse_location, email, phone, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, staff.getUsername());
            stmt.setString(2, staff.getPassword());
            stmt.setString(3, staff.getFullName());
            stmt.setString(4, staff.getWarehouseLocation());
            stmt.setString(5, staff.getEmail());
            stmt.setString(6, staff.getPhone());
            stmt.setString(7, staff.getStatus() != null ? staff.getStatus() : "active");
            
            stmt.executeUpdate();
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    staff.setStaffId(generatedKeys.getInt(1));
                }
            }
        }
    }

    @Override
    public void update(WarehouseStaff staff) throws SQLException {
        String sql = "UPDATE warehouse_staff SET username = ?, password = ?, full_name = ?, warehouse_location = ?, email = ?, phone = ?, status = ? WHERE staff_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, staff.getUsername());
            stmt.setString(2, staff.getPassword());
            stmt.setString(3, staff.getFullName());
            stmt.setString(4, staff.getWarehouseLocation());
            stmt.setString(5, staff.getEmail());
            stmt.setString(6, staff.getPhone());
            stmt.setString(7, staff.getStatus());
            stmt.setInt(8, staff.getStaffId());
            
            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(int staffId) throws SQLException {
        String sql = "DELETE FROM warehouse_staff WHERE staff_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, staffId);
            stmt.executeUpdate();
        }
    }

    @Override
    public boolean authenticate(String username, String password) throws SQLException {
        String sql = "SELECT COUNT(*) FROM warehouse_staff WHERE username = ? AND password = ? AND status = 'active'";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    private WarehouseStaff mapResultSetToStaff(ResultSet rs) throws SQLException {
        WarehouseStaff staff = new WarehouseStaff();
        staff.setStaffId(rs.getInt("staff_id"));
        staff.setUsername(rs.getString("username"));
        staff.setPassword(rs.getString("password"));
        staff.setFullName(rs.getString("full_name"));
        staff.setWarehouseLocation(rs.getString("warehouse_location"));
        staff.setEmail(rs.getString("email"));
        staff.setPhone(rs.getString("phone"));
        staff.setCreatedAt(rs.getTimestamp("created_at"));
        staff.setStatus(rs.getString("status"));
        return staff;
    }
} 