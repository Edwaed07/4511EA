package com.aib.model.warehouse;

import java.sql.Timestamp;

public class InventoryTransaction {
    private int transactionId;
    private int warehouseId;
    private int fruitId;
    private String transactionType; // check_in, check_out, transfer
    private int quantity;
    private String sourceLocation;
    private String destinationLocation;
    private int staffId;
    private Timestamp transactionDate;
    private String status; // pending, approved, completed, cancelled
    
    // 用于连接显示的附加属性
    private String fruitName;
    private String warehouseLocation;
    private String staffName;

    public InventoryTransaction() {
    }

    public InventoryTransaction(int transactionId, int warehouseId, int fruitId, String transactionType, 
                              int quantity, String sourceLocation, String destinationLocation, 
                              int staffId, Timestamp transactionDate, String status) {
        this.transactionId = transactionId;
        this.warehouseId = warehouseId;
        this.fruitId = fruitId;
        this.transactionType = transactionType;
        this.quantity = quantity;
        this.sourceLocation = sourceLocation;
        this.destinationLocation = destinationLocation;
        this.staffId = staffId;
        this.transactionDate = transactionDate;
        this.status = status;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public int getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(int warehouseId) {
        this.warehouseId = warehouseId;
    }

    public int getFruitId() {
        return fruitId;
    }

    public void setFruitId(int fruitId) {
        this.fruitId = fruitId;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getSourceLocation() {
        return sourceLocation;
    }

    public void setSourceLocation(String sourceLocation) {
        this.sourceLocation = sourceLocation;
    }

    public String getDestinationLocation() {
        return destinationLocation;
    }

    public void setDestinationLocation(String destinationLocation) {
        this.destinationLocation = destinationLocation;
    }

    public int getStaffId() {
        return staffId;
    }

    public void setStaffId(int staffId) {
        this.staffId = staffId;
    }

    public Timestamp getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Timestamp transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFruitName() {
        return fruitName;
    }

    public void setFruitName(String fruitName) {
        this.fruitName = fruitName;
    }

    public String getWarehouseLocation() {
        return warehouseLocation;
    }

    public void setWarehouseLocation(String warehouseLocation) {
        this.warehouseLocation = warehouseLocation;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }
} 