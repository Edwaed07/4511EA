package com.aib.dao.warehouse.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.aib.dao.warehouse.ReservationDAO;
import com.aib.model.warehouse.Reservation;
import com.aib.util.DBUtil;

public class ReservationDAOImpl implements ReservationDAO {

    @Override
    public Reservation findById(int reservationId) throws SQLException {
        String sql = "SELECT r.*, f.fruit_name, w.warehouse_location, b.bakery_name " +
                     "FROM reservations r " +
                     "JOIN fruits f ON r.fruit_id = f.fruit_id " +
                     "JOIN warehouse_staff w ON r.warehouse_id = w.staff_id " +
                     "JOIN bakeries b ON r.bakery_id = b.bakery_id " +
                     "WHERE r.reservation_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, reservationId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToReservation(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<Reservation> findAll() throws SQLException {
        List<Reservation> reservationList = new ArrayList<>();
        String sql = "SELECT r.*, f.fruit_name, w.warehouse_location, b.bakery_name " +
                     "FROM reservations r " +
                     "JOIN fruits f ON r.fruit_id = f.fruit_id " +
                     "JOIN warehouse_staff w ON r.warehouse_id = w.staff_id " +
                     "JOIN bakeries b ON r.bakery_id = b.bakery_id " +
                     "ORDER BY r.reservation_date DESC";
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                reservationList.add(mapResultSetToReservation(rs));
            }
        }
        return reservationList;
    }

    @Override
    public List<Reservation> findByWarehouse(int warehouseId) throws SQLException {
        List<Reservation> reservationList = new ArrayList<>();
        String sql = "SELECT r.*, f.fruit_name, w.warehouse_location, b.bakery_name " +
                     "FROM reservations r " +
                     "JOIN fruits f ON r.fruit_id = f.fruit_id " +
                     "JOIN warehouse_staff w ON r.warehouse_id = w.staff_id " +
                     "JOIN bakeries b ON r.bakery_id = b.bakery_id " +
                     "WHERE r.warehouse_id = ? " +
                     "ORDER BY r.reservation_date DESC";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, warehouseId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    reservationList.add(mapResultSetToReservation(rs));
                }
            }
        }
        return reservationList;
    }

    @Override
    public List<Reservation> findByBakery(int bakeryId) throws SQLException {
        List<Reservation> reservationList = new ArrayList<>();
        String sql = "SELECT r.*, f.fruit_name, w.warehouse_location, b.bakery_name " +
                     "FROM reservations r " +
                     "JOIN fruits f ON r.fruit_id = f.fruit_id " +
                     "JOIN warehouse_staff w ON r.warehouse_id = w.staff_id " +
                     "JOIN bakeries b ON r.bakery_id = b.bakery_id " +
                     "WHERE r.bakery_id = ? " +
                     "ORDER BY r.reservation_date DESC";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, bakeryId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    reservationList.add(mapResultSetToReservation(rs));
                }
            }
        }
        return reservationList;
    }

    @Override
    public List<Reservation> findByStatus(String status) throws SQLException {
        List<Reservation> reservationList = new ArrayList<>();
        String sql = "SELECT r.*, f.fruit_name, w.warehouse_location, b.bakery_name " +
                     "FROM reservations r " +
                     "JOIN fruits f ON r.fruit_id = f.fruit_id " +
                     "JOIN warehouse_staff w ON r.warehouse_id = w.staff_id " +
                     "JOIN bakeries b ON r.bakery_id = b.bakery_id " +
                     "WHERE r.status = ? " +
                     "ORDER BY r.reservation_date DESC";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    reservationList.add(mapResultSetToReservation(rs));
                }
            }
        }
        return reservationList;
    }

    @Override
    public List<Reservation> findPendingReservations() throws SQLException {
        return findByStatus("pending");
    }

    @Override
    public void save(Reservation reservation) throws SQLException {
        String sql = "INSERT INTO reservations (fruit_id, warehouse_id, bakery_id, quantity, " +
                     "expected_delivery_date, status, remarks) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, reservation.getFruitId());
            stmt.setInt(2, reservation.getWarehouseId());
            stmt.setInt(3, reservation.getBakeryId());
            stmt.setInt(4, reservation.getQuantity());
            stmt.setTimestamp(5, reservation.getExpectedDeliveryDate());
            stmt.setString(6, reservation.getStatus() != null ? reservation.getStatus() : "pending");
            stmt.setString(7, reservation.getRemarks());
            
            stmt.executeUpdate();
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    reservation.setReservationId(generatedKeys.getInt(1));
                }
            }
        }
    }

    @Override
    public void update(Reservation reservation) throws SQLException {
        String sql = "UPDATE reservations " +
                     "SET fruit_id = ?, warehouse_id = ?, bakery_id = ?, quantity = ?, " +
                     "expected_delivery_date = ?, status = ?, remarks = ? " +
                     "WHERE reservation_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, reservation.getFruitId());
            stmt.setInt(2, reservation.getWarehouseId());
            stmt.setInt(3, reservation.getBakeryId());
            stmt.setInt(4, reservation.getQuantity());
            stmt.setTimestamp(5, reservation.getExpectedDeliveryDate());
            stmt.setString(6, reservation.getStatus());
            stmt.setString(7, reservation.getRemarks());
            stmt.setInt(8, reservation.getReservationId());
            
            stmt.executeUpdate();
        }
    }

    @Override
    public void updateStatus(int reservationId, String status) throws SQLException {
        String sql = "UPDATE reservations SET status = ? WHERE reservation_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, reservationId);
            
            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(int reservationId) throws SQLException {
        String sql = "DELETE FROM reservations WHERE reservation_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, reservationId);
            stmt.executeUpdate();
        }
    }
    
    private Reservation mapResultSetToReservation(ResultSet rs) throws SQLException {
        Reservation reservation = new Reservation();
        reservation.setReservationId(rs.getInt("reservation_id"));
        reservation.setFruitId(rs.getInt("fruit_id"));
        reservation.setWarehouseId(rs.getInt("warehouse_id"));
        reservation.setBakeryId(rs.getInt("bakery_id"));
        reservation.setQuantity(rs.getInt("quantity"));
        reservation.setReservationDate(rs.getTimestamp("reservation_date"));
        reservation.setExpectedDeliveryDate(rs.getTimestamp("expected_delivery_date"));
        reservation.setStatus(rs.getString("status"));
        reservation.setRemarks(rs.getString("remarks"));
        
        // 连接字段
        reservation.setFruitName(rs.getString("fruit_name"));
        reservation.setWarehouseLocation(rs.getString("warehouse_location"));
        reservation.setBakeryName(rs.getString("bakery_name"));
        
        return reservation;
    }
} 