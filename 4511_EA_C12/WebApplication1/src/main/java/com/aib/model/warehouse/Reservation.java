package com.aib.model.warehouse;

import java.sql.Timestamp;

public class Reservation {
    private int reservationId;
    private int fruitId;
    private int warehouseId;
    private int bakeryId;
    private int quantity;
    private Timestamp reservationDate;
    private Timestamp expectedDeliveryDate;
    private String status; // pending, approved, rejected, completed
    private String remarks;
    
    // 用于连接显示的附加属性
    private String fruitName;
    private String warehouseLocation;
    private String bakeryName;
    
    public Reservation() {
    }

    public Reservation(int reservationId, int fruitId, int warehouseId, int bakeryId, int quantity,
                      Timestamp reservationDate, Timestamp expectedDeliveryDate, String status, String remarks) {
        this.reservationId = reservationId;
        this.fruitId = fruitId;
        this.warehouseId = warehouseId;
        this.bakeryId = bakeryId;
        this.quantity = quantity;
        this.reservationDate = reservationDate;
        this.expectedDeliveryDate = expectedDeliveryDate;
        this.status = status;
        this.remarks = remarks;
    }

    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public int getFruitId() {
        return fruitId;
    }

    public void setFruitId(int fruitId) {
        this.fruitId = fruitId;
    }

    public int getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(int warehouseId) {
        this.warehouseId = warehouseId;
    }

    public int getBakeryId() {
        return bakeryId;
    }

    public void setBakeryId(int bakeryId) {
        this.bakeryId = bakeryId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Timestamp getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(Timestamp reservationDate) {
        this.reservationDate = reservationDate;
    }

    public Timestamp getExpectedDeliveryDate() {
        return expectedDeliveryDate;
    }

    public void setExpectedDeliveryDate(Timestamp expectedDeliveryDate) {
        this.expectedDeliveryDate = expectedDeliveryDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getBakeryName() {
        return bakeryName;
    }

    public void setBakeryName(String bakeryName) {
        this.bakeryName = bakeryName;
    }
} 