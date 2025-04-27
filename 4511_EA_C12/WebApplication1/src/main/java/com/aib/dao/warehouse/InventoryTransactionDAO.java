package com.aib.dao.warehouse;

import java.sql.SQLException;
import java.util.List;

import com.aib.model.warehouse.InventoryTransaction;

public interface InventoryTransactionDAO {
    InventoryTransaction findById(int transactionId) throws SQLException;
    List<InventoryTransaction> findAll() throws SQLException;
    List<InventoryTransaction> findByWarehouse(int warehouseId) throws SQLException;
    List<InventoryTransaction> findByFruit(int fruitId) throws SQLException;
    List<InventoryTransaction> findByStaff(int staffId) throws SQLException;
    List<InventoryTransaction> findByType(String transactionType) throws SQLException;
    List<InventoryTransaction> findByStatus(String status) throws SQLException;
    List<InventoryTransaction> findRecentTransactions(int limit) throws SQLException;
    void save(InventoryTransaction transaction) throws SQLException;
    void update(InventoryTransaction transaction) throws SQLException;
    void updateStatus(int transactionId, String status) throws SQLException;
} 