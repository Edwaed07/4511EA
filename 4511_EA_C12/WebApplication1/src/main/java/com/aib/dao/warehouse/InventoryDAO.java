package com.aib.dao.warehouse;

import java.sql.SQLException;
import java.util.List;

import com.aib.model.warehouse.Inventory;

public interface InventoryDAO {
    Inventory findById(int inventoryId) throws SQLException;
    Inventory findByFruitAndWarehouse(int fruitId, int warehouseId) throws SQLException;
    List<Inventory> findAll() throws SQLException;
    List<Inventory> findByWarehouse(int warehouseId) throws SQLException;
    List<Inventory> findByFruit(int fruitId) throws SQLException;
    List<Inventory> findLowStock() throws SQLException;
    void save(Inventory inventory) throws SQLException;
    void update(Inventory inventory) throws SQLException;
    void updateQuantity(int inventoryId, int quantity) throws SQLException;
    void delete(int inventoryId) throws SQLException;
} 