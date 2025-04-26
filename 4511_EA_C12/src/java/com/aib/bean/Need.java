package com.aib.bean;

import java.util.Date;

public class Need {
    private int id;
    private String country;
    private int fruitId;
    private String fruitName;
    private int totalQuantity;
    private int requestCount;
    private Date startDate;
    private Date endDate;
    private String status; // "Pending", "Approved", "Rejected"
    private String deliveryStatus; // "Not Started", "Shipped", "Delivered"
    
    public Need(int id, String country, int fruitId, String fruitName, int totalQuantity, int requestCount) {
        this.id = id;
        this.country = country;
        this.fruitId = fruitId;
        this.fruitName = fruitName;
        this.totalQuantity = totalQuantity;
        this.requestCount = requestCount;
        this.status = "Pending";
        this.deliveryStatus = "Not Started";
    }
    
    // Getters
    public int getId() {
        return id;
    }
    
    public String getCountry() {
        return country;
    }
    
    public int getFruitId() {
        return fruitId;
    }
    
    public String getFruitName() {
        return fruitName;
    }
    
    public int getTotalQuantity() {
        return totalQuantity;
    }
    
    public int getRequestCount() {
        return requestCount;
    }
    
    public Date getStartDate() {
        return startDate;
    }
    
    public Date getEndDate() {
        return endDate;
    }
    
    public String getStatus() {
        return status;
    }
    
    public String getDeliveryStatus() {
        return deliveryStatus;
    }
    
    // Setters
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }
    
    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }
    
    public void setRequestCount(int requestCount) {
        this.requestCount = requestCount;
    }
} 