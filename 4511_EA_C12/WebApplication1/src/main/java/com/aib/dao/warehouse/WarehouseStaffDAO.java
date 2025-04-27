package com.aib.dao.warehouse;

import java.sql.SQLException;
import java.util.List;

import com.aib.model.warehouse.WarehouseStaff;

public interface WarehouseStaffDAO {
    WarehouseStaff findById(int staffId) throws SQLException;
    WarehouseStaff findByUsername(String username) throws SQLException;
    List<WarehouseStaff> findAll() throws SQLException;
    void save(WarehouseStaff staff) throws SQLException;
    void update(WarehouseStaff staff) throws SQLException;
    void delete(int staffId) throws SQLException;
    boolean authenticate(String username, String password) throws SQLException;
} 