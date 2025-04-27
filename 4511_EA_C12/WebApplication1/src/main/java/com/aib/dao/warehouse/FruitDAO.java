package com.aib.dao.warehouse;

import java.sql.SQLException;
import java.util.List;

import com.aib.model.warehouse.Fruit;

public interface FruitDAO {
    Fruit findById(int fruitId) throws SQLException;
    List<Fruit> findAll() throws SQLException;
    List<Fruit> findBySourceLocation(String sourceLocation) throws SQLException;
    void save(Fruit fruit) throws SQLException;
    void update(Fruit fruit) throws SQLException;
    void delete(int fruitId) throws SQLException;
}