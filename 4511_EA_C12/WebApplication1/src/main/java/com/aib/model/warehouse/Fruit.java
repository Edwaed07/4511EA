package com.aib.model.warehouse;

import java.sql.Timestamp;

public class Fruit {
    private int fruitId;
    private String fruitName;
    private String sourceLocation;
    private String description;
    private Timestamp createdAt;
    
    public Fruit() {
    }
    
    public Fruit(int fruitId, String fruitName, String sourceLocation, String description, Timestamp createdAt) {
        this.fruitId = fruitId;
        this.fruitName = fruitName;
        this.sourceLocation = sourceLocation;
        this.description = description;
        this.createdAt = createdAt;
    }

    public int getFruitId() {
        return fruitId;
    }

    public void setFruitId(int fruitId) {
        this.fruitId = fruitId;
    }

    public String getFruitName() {
        return fruitName;
    }

    public void setFruitName(String fruitName) {
        this.fruitName = fruitName;
    }

    public String getSourceLocation() {
        return sourceLocation;
    }

    public void setSourceLocation(String sourceLocation) {
        this.sourceLocation = sourceLocation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
} 