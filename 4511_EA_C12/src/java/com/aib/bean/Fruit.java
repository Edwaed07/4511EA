package com.aib.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Fruit {
    private int id;
    private String name;
    private String sourceCity;
    private String country;
    private int stockLevel;
    private Map<String, Integer> branchStocks = new HashMap<>();
    private List<String> availableBranches = new ArrayList<>();

    public Fruit(int id, String name, String sourceCity, String country, int stockLevel) {
        this.id = id;
        this.name = name;
        this.sourceCity = sourceCity;
        this.country = country;
        this.stockLevel = stockLevel;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSourceCity() {
        return sourceCity;
    }

    public String getCountry() {
        return country;
    }

    public int getStockLevel() {
        return stockLevel;
    }

    public Map<String, Integer> getBranchStocks() {
        return branchStocks;
    }

    public List<String> getAvailableBranches() {
        return availableBranches;
    }

    // Setters
    public void addBranchStock(String branch, int stock) {
        branchStocks.put(branch, stock);
    }

    public void setAvailableBranches(List<String> branches) {
        this.availableBranches = branches;
    }
}