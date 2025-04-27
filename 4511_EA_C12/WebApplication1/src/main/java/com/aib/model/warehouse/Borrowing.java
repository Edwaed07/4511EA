package com.aib.model.warehouse;

import java.sql.Timestamp;

public class Borrowing {
    private int borrowingId;
    private int fruitId;
    private int inventoryId;
    private int warehouseId;
    private int borrowerId;
    private int quantity;
    private Timestamp borrowDate;
    private Timestamp expectedReturnDate;
    private Timestamp actualReturnDate;
    private String status; // pending, approved, borrowed, returned, overdue
    private String purpose;
    private String remarks;
    
    // 用于连接显示的附加属性
    private String fruitName;
    private String warehouseLocation;
    private String borrowerName;
    
    public Borrowing() {
    }

    public Borrowing(int borrowingId, int fruitId, int inventoryId, int warehouseId, int borrowerId,
                    int quantity, Timestamp borrowDate, Timestamp expectedReturnDate, 
                    Timestamp actualReturnDate, String status, String purpose, String remarks) {
        this.borrowingId = borrowingId;
        this.fruitId = fruitId;
        this.inventoryId = inventoryId;
        this.warehouseId = warehouseId;
        this.borrowerId = borrowerId;
        this.quantity = quantity;
        this.borrowDate = borrowDate;
        this.expectedReturnDate = expectedReturnDate;
        this.actualReturnDate = actualReturnDate;
        this.status = status;
        this.purpose = purpose;
        this.remarks = remarks;
    }

    public int getBorrowingId() {
        return borrowingId;
    }

    public void setBorrowingId(int borrowingId) {
        this.borrowingId = borrowingId;
    }

    public int getFruitId() {
        return fruitId;
    }

    public void setFruitId(int fruitId) {
        this.fruitId = fruitId;
    }

    public int getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(int inventoryId) {
        this.inventoryId = inventoryId;
    }

    public int getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(int warehouseId) {
        this.warehouseId = warehouseId;
    }

    public int getBorrowerId() {
        return borrowerId;
    }

    public void setBorrowerId(int borrowerId) {
        this.borrowerId = borrowerId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Timestamp getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(Timestamp borrowDate) {
        this.borrowDate = borrowDate;
    }

    public Timestamp getExpectedReturnDate() {
        return expectedReturnDate;
    }

    public void setExpectedReturnDate(Timestamp expectedReturnDate) {
        this.expectedReturnDate = expectedReturnDate;
    }

    public Timestamp getActualReturnDate() {
        return actualReturnDate;
    }

    public void setActualReturnDate(Timestamp actualReturnDate) {
        this.actualReturnDate = actualReturnDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
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

    public String getBorrowerName() {
        return borrowerName;
    }

    public void setBorrowerName(String borrowerName) {
        this.borrowerName = borrowerName;
    }
} 