package model;

import java.io.Serializable;
import java.util.Date;

public class Inventory implements Serializable {
    private int id;
    private int fruitId;
    private String locationType; // "sourceWarehouse", "centralWarehouse", "shop"
    private String locationName; // country/city/shop name
    private int quantity;
    private Date lastUpdated;
    
    public Inventory() {
    }
    
    public Inventory(int id, int fruitId, String locationType, String locationName, int quantity, Date lastUpdated) {
        this.id = id;
        this.fruitId = fruitId;
        this.locationType = locationType;
        this.locationName = locationName;
        this.quantity = quantity;
        this.lastUpdated = lastUpdated;
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
    
    public String getLocationType() {
        return locationType;
    }
    
    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }
    
    public String getLocationName() {
        return locationName;
    }
    
    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    public Date getLastUpdated() {
        return lastUpdated;
    }
    
    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
} 