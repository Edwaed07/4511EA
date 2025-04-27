package model;

import java.io.Serializable;
import java.util.Date;

public class Delivery implements Serializable {
    private int id;
    private int fruitId;
    private String sourceLocation;
    private String destinationLocation;
    private String destinationType; // "centralWarehouse", "shop"
    private int quantity;
    private Date deliveryDate;
    private String status; // "pending", "in-transit", "delivered"
    private int reserveId; // 关联预订ID，如果有的话
    
    public Delivery() {
    }
    
    public Delivery(int id, int fruitId, String sourceLocation, String destinationLocation, 
                  String destinationType, int quantity, Date deliveryDate, String status, int reserveId) {
        this.id = id;
        this.fruitId = fruitId;
        this.sourceLocation = sourceLocation;
        this.destinationLocation = destinationLocation;
        this.destinationType = destinationType;
        this.quantity = quantity;
        this.deliveryDate = deliveryDate;
        this.status = status;
        this.reserveId = reserveId;
    }
    
    // Getters and setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getFruitId() {
        return fruitId;
    }
    
    public void setFruitId(int fruitId) {
        this.fruitId = fruitId;
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
    
    public String getDestinationType() {
        return destinationType;
    }
    
    public void setDestinationType(String destinationType) {
        this.destinationType = destinationType;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    public Date getDeliveryDate() {
        return deliveryDate;
    }
    
    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public int getReserveId() {
        return reserveId;
    }
    
    public void setReserveId(int reserveId) {
        this.reserveId = reserveId;
    }
} 