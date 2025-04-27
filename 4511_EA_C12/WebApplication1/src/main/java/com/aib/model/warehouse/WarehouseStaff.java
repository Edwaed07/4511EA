package com.aib.model.warehouse;

import java.sql.Timestamp;

public class WarehouseStaff {
    private int staffId;
    private String username;
    private String password;
    private String fullName;
    private String warehouseLocation;
    private String email;
    private String phone;
    private Timestamp createdAt;
    private String status;

    // 构造函数
    public WarehouseStaff() {
    }

    public WarehouseStaff(int staffId, String username, String password, String fullName, 
                         String warehouseLocation, String email, String phone, 
                         Timestamp createdAt, String status) {
        this.staffId = staffId;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.warehouseLocation = warehouseLocation;
        this.email = email;
        this.phone = phone;
        this.createdAt = createdAt;
        this.status = status;
    }

    // Getters and Setters
    public int getStaffId() {
        return staffId;
    }

    public void setStaffId(int staffId) {
        this.staffId = staffId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getWarehouseLocation() {
        return warehouseLocation;
    }

    public void setWarehouseLocation(String warehouseLocation) {
        this.warehouseLocation = warehouseLocation;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
} 