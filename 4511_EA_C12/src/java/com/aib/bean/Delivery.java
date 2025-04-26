package com.aib.bean;

import java.util.Date;

public class Delivery {
    private int id;
    private String sourceLocation;
    private String destinationLocation;
    private int fruitId;
    private String fruitName;
    private int quantity;
    private Date createdDate;
    private Date shipDate;
    private Date deliveryDate;
    private String status; // "Pending", "In Transit", "Delivered"
    private int needId; // 相关的需求ID
    private String notes;
    
    public Delivery(int id, String sourceLocation, String destinationLocation, int fruitId, String fruitName, int quantity) {
        this.id = id;
        this.sourceLocation = sourceLocation;
        this.destinationLocation = destinationLocation;
        this.fruitId = fruitId;
        this.fruitName = fruitName;
        this.quantity = quantity;
        this.createdDate = new Date(); // 当前时间
        this.status = "Pending";
    }
    
    // Getters
    public int getId() {
        return id;
    }
    
    public String getSourceLocation() {
        return sourceLocation;
    }
    
    public String getDestinationLocation() {
        return destinationLocation;
    }
    
    public int getFruitId() {
        return fruitId;
    }
    
    public String getFruitName() {
        return fruitName;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public Date getCreatedDate() {
        return createdDate;
    }
    
    public Date getShipDate() {
        return shipDate;
    }
    
    public Date getDeliveryDate() {
        return deliveryDate;
    }
    
    public String getStatus() {
        return status;
    }
    
    public int getNeedId() {
        return needId;
    }
    
    public String getNotes() {
        return notes;
    }
    
    // Setters
    public void setShipDate(Date shipDate) {
        this.shipDate = shipDate;
    }
    
    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public void setNeedId(int needId) {
        this.needId = needId;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
} 