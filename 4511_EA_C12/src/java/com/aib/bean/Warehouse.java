package com.aib.bean;

import java.util.HashMap;
import java.util.Map;

public class Warehouse {
    private int id;
    private String name;
    private String type; // "Source", "Central", "Local"
    private String city;
    private String country;
    private Map<Integer, Integer> fruitStock = new HashMap<>(); // 水果ID -> 数量

    public Warehouse(int id, String name, String type, String city, String country) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.city = city;
        this.country = country;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public Map<Integer, Integer> getFruitStock() {
        return fruitStock;
    }

    // Fruit stock management
    public void addFruitStock(int fruitId, int quantity) {
        int currentStock = fruitStock.getOrDefault(fruitId, 0);
        fruitStock.put(fruitId, currentStock + quantity);
    }

    public void removeFruitStock(int fruitId, int quantity) {
        int currentStock = fruitStock.getOrDefault(fruitId, 0);
        if (currentStock >= quantity) {
            fruitStock.put(fruitId, currentStock - quantity);
        } else {
            throw new IllegalArgumentException("Not enough stock for fruit ID " + fruitId);
        }
    }

    public int getFruitStockLevel(int fruitId) {
        return fruitStock.getOrDefault(fruitId, 0);
    }

    public boolean hasEnoughStock(int fruitId, int quantity) {
        return getFruitStockLevel(fruitId) >= quantity;
    }
} 