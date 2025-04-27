package com.aib.model.warehouse;

import java.sql.Timestamp;

public class Delivery {
    private int deliveryId;
    private int reservationId;
    private int warehouseId;
    private int bakeryId;
    private int fruitId;
    private int inventoryId;
    private int quantity;
    private int driverId;
    private String vehicleNumber;
    private Timestamp dispatchTime;
    private Timestamp estimatedArrivalTime;
    private Timestamp actualArrivalTime;
    private String status; // pending, dispatched, in_transit, delivered, cancelled
    private String notes;
    
    // 用于连接显示的附加属性
    private String fruitName;
    private String warehouseLocation;
    private String bakeryName;
    private String driverName;
    
    public Delivery() {
    }
    
    public Delivery(int deliveryId, int reservationId, int warehouseId, int bakeryId, int fruitId,
                   int inventoryId, int quantity, int driverId, String vehicleNumber, 
                   Timestamp dispatchTime, Timestamp estimatedArrivalTime, Timestamp actualArrivalTime,
                   String status, String notes) {
        this.deliveryId = deliveryId;
        this.reservationId = reservationId;
        this.warehouseId = warehouseId;
        this.bakeryId = bakeryId;
        this.fruitId = fruitId;
        this.inventoryId = inventoryId;
        this.quantity = quantity;
        this.driverId = driverId;
        this.vehicleNumber = vehicleNumber;
        this.dispatchTime = dispatchTime;
        this.estimatedArrivalTime = estimatedArrivalTime;
        this.actualArrivalTime = actualArrivalTime;
        this.status = status;
        this.notes = notes;
    }

    public int getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(int deliveryId) {
        this.deliveryId = deliveryId;
    }

    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getDriverId() {
        return driverId;
    }

    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public Timestamp getDispatchTime() {
        return dispatchTime;
    }

    public void setDispatchTime(Timestamp dispatchTime) {
        this.dispatchTime = dispatchTime;
    }

    public Timestamp getEstimatedArrivalTime() {
        return estimatedArrivalTime;
    }

    public void setEstimatedArrivalTime(Timestamp estimatedArrivalTime) {
        this.estimatedArrivalTime = estimatedArrivalTime;
    }

    public Timestamp getActualArrivalTime() {
        return actualArrivalTime;
    }

    public void setActualArrivalTime(Timestamp actualArrivalTime) {
        this.actualArrivalTime = actualArrivalTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }
} 