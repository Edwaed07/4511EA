package com.aib.dao.warehouse;

import java.sql.SQLException;
import java.util.List;

import com.aib.model.warehouse.Reservation;

public interface ReservationDAO {
    Reservation findById(int reservationId) throws SQLException;
    List<Reservation> findAll() throws SQLException;
    List<Reservation> findByWarehouse(int warehouseId) throws SQLException;
    List<Reservation> findByBakery(int bakeryId) throws SQLException;
    List<Reservation> findByStatus(String status) throws SQLException;
    List<Reservation> findPendingReservations() throws SQLException;
    void save(Reservation reservation) throws SQLException;
    void update(Reservation reservation) throws SQLException;
    void updateStatus(int reservationId, String status) throws SQLException;
    void delete(int reservationId) throws SQLException;
} 