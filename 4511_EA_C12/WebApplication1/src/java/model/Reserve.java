package model;

import java.io.Serializable;
import java.util.Date;

public class Reserve implements Serializable {
    private int id;
    private int fruitId;
    private int userId;
    private String shopName;
    private String city;
    private String country;
    private int quantity;
    private Date reserveDate;
    private Date deliveryDate;
    private String status; // "pending", "approved", "delivered", "cancelled"
    
    public Reserve() {
    }
    
    public Reserve(int id, int fruitId, int userId, String shopName, String city, String country, 
                 int quantity, Date reserveDate, Date deliveryDate, String status) {
        this.id = id;
        this.fruitId = fruitId;
        this.userId = userId;
        this.shopName = shopName;
        this.city = city;
        this.country = country;
        this.quantity = quantity;
        this.reserveDate = reserveDate;
        this.deliveryDate = deliveryDate;
        this.status = status;
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
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public String getShopName() {
        return shopName;
    }
    
    public void setShopName(String shopName) {
        this.shopName = shopName;
    }
    
    public String getCity() {
        return city;
    }
    
    public void setCity(String city) {
        this.city = city;
    }
    
    public String getCountry() {
        return country;
    }
    
    public void setCountry(String country) {
        this.country = country;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    public Date getReserveDate() {
        return reserveDate;
    }
    
    public void setReserveDate(Date reserveDate) {
        this.reserveDate = reserveDate;
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
} 