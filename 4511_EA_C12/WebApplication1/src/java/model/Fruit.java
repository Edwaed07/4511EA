package model;

import java.io.Serializable;

public class Fruit implements Serializable {
    private int id;
    private String name;
    private String sourceCountry;
    private String description;
    private boolean active;
    
    public Fruit() {
    }
    
    public Fruit(int id, String name, String sourceCountry, String description, boolean active) {
        this.id = id;
        this.name = name;
        this.sourceCountry = sourceCountry;
        this.description = description;
        this.active = active;
    }
    
    // Getters and setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getSourceCountry() {
        return sourceCountry;
    }
    
    public void setSourceCountry(String sourceCountry) {
        this.sourceCountry = sourceCountry;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public boolean isActive() {
        return active;
    }
    
    public void setActive(boolean active) {
        this.active = active;
    }
} 