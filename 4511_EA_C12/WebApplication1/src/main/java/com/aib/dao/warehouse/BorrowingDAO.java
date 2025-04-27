package com.aib.dao.warehouse;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import com.aib.model.warehouse.Borrowing;

public interface BorrowingDAO {
    Borrowing findById(int borrowingId) throws SQLException;
    List<Borrowing> findAll() throws SQLException;
    List<Borrowing> findByWarehouse(int warehouseId) throws SQLException;
    List<Borrowing> findByBorrower(int borrowerId) throws SQLException;
    List<Borrowing> findByStatus(String status) throws SQLException;
    List<Borrowing> findPendingBorrowings() throws SQLException;
    List<Borrowing> findOverdueBorrowings() throws SQLException;
    void save(Borrowing borrowing) throws SQLException;
    void update(Borrowing borrowing) throws SQLException;
    void updateStatus(int borrowingId, String status) throws SQLException;
    void updateActualReturnDate(int borrowingId, Timestamp actualReturnDate) throws SQLException;
    void delete(int borrowingId) throws SQLException;
} 