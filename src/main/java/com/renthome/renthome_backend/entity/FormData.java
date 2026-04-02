package com.renthome.renthome_backend.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "properties")
public class FormData {

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "property_images", joinColumns = @JoinColumn(name = "property_id"))
    @Column(name = "image_url")
    private java.util.List<String> images = new java.util.ArrayList<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "public_id", unique = true, nullable = false, length = 50)
    private String publicId; // PUBLIC ID (USED IN QR / URL)

    /* ================= DYNAMIC FORM DATA ================= */

    @Lob
    @Column(columnDefinition = "TEXT")
    private String formJson; // stores JSON for dynamic forms

    public String getFormJson() {
        return formJson;
    }

    public String getPublicId() {
        return publicId;
    }

    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }

    public void setFormJson(String formJson) {
        this.formJson = formJson;
    }

    /* ================= PROPERTY BASIC ================= */

    @Column(name = "generated_at")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate generatedAt;

    @Column(length = 30)
    private String propertyType; // Apartment / Flat / PG

    @Column(length = 100)
    private String title;

    private String role; // ADMIN or USER
    /* ================= LOCATION ================= */

    @Column(length = 100)
    private String apartmentName;

    @Column(length = 50)
    private String towerName;

    @Column(length = 20)
    private String bhk;

    @Column(length = 10)
    private String floor;

    @Column(length = 10)
    private String totalFloors;

    @Column(length = 20)
    private String builtUpArea;

    /* ================= GEO LOCATION ================= */

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    /* ================= RENT ================= */

    @Column(length = 20)
    private String rent;

    @Column(length = 20)
    private String deposit;

    @Column(length = 50)
    private String maintenanceType;

    @Column(length = 20)
    private String maintenanceAmount;

    /* ================= FEATURES ================= */

    @Column(length = 10)
    private String lift;

    @Column(length = 20)
    private String parking;

    @Column(length = 10)
    private String powerBackup;

    @Column(length = 20)
    private String waterSupply;

    @Column(length = 20)
    private String security;

    /* ================= AMENITIES ================= */

    @Lob
    @Column(columnDefinition = "TEXT")
    private String amenities; // comma-separated

    /* ================= AVAILABILITY ================= */

    @Column(length = 20)
    private String availableFrom;

    @Column(length = 30)
    private String preferredTenants;

    /* ================= OWNER ================= */

    @Column(length = 50)
    private String ownerName;

    @Column(length = 15)
    private String mobile;

    @Column(length = 15)
    private String alternateContact;

    @Column(length = 100)
    private String email;

    @Column(length = 20)
    private String preferredContact;

    @Column(length = 100)
    private String city;

    @Column(length = 100)
    private String state;

    @Column(length = 10)
    private String pincode;

    @Column(length = 150)
    private String area;

    @Column(nullable = false)
    private boolean posted = false;

    /* ================= META ================= */

    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private Long ownerId;

    /* ================= AUTO TIMESTAMP ================= */

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();

        if (this.publicId == null) {
            this.publicId = java.util.UUID.randomUUID().toString();
        }

        if (this.generatedAt == null) {
            this.generatedAt = LocalDate.now(); // ✅ SET ONCE
        }
    }

    /* ================= GETTERS ================= */

    public Long getOwnerId() {
        return ownerId;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public LocalDate getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(LocalDate generatedAt) {
        this.generatedAt = generatedAt;
    }

    public boolean isPosted() {
        return posted;
    }

    public void setPosted(boolean posted) {
        this.posted = posted;
    }

    public Long getId() {
        return id;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public String getCity() { return city; }
public void setCity(String city) { this.city = city; }

public String getState() { return state; }
public void setState(String state) { this.state = state; }

public String getPincode() { return pincode; }
public void setPincode(String pincode) { this.pincode = pincode; }

public String getArea() { return area; }
public void setArea(String area) { this.area = area; }
    
    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public String getTitle() {
        return title;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getApartmentName() {
        return apartmentName;
    }

    public void setApartmentName(String apartmentName) {
        this.apartmentName = apartmentName;
    }

    public String getTowerName() {
        return towerName;
    }

    public void setTowerName(String towerName) {
        this.towerName = towerName;
    }

    public String getBhk() {
        return bhk;
    }

    public void setBhk(String bhk) {
        this.bhk = bhk;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getTotalFloors() {
        return totalFloors;
    }

    public void setTotalFloors(String totalFloors) {
        this.totalFloors = totalFloors;
    }

    public String getBuiltUpArea() {
        return builtUpArea;
    }

    public void setBuiltUpArea(String builtUpArea) {
        this.builtUpArea = builtUpArea;
    }

    public String getRent() {
        return rent;
    }

    public void setRent(String rent) {
        this.rent = rent;
    }

    public String getDeposit() {
        return deposit;
    }

    public void setDeposit(String deposit) {
        this.deposit = deposit;
    }

    public String getMaintenanceType() {
        return maintenanceType;
    }

    public void setMaintenanceType(String maintenanceType) {
        this.maintenanceType = maintenanceType;
    }

    public String getMaintenanceAmount() {
        return maintenanceAmount;
    }

    public void setMaintenanceAmount(String maintenanceAmount) {
        this.maintenanceAmount = maintenanceAmount;
    }

    public String getLift() {
        return lift;
    }

    public java.util.List<String> getImages() {
        return images;
    }

    public void setImages(java.util.List<String> images) {
        this.images = images;
    }

    public void setLift(String lift) {
        this.lift = lift;
    }

    public String getParking() {
        return parking;
    }

    public void setParking(String parking) {
        this.parking = parking;
    }

    public String getPowerBackup() {
        return powerBackup;
    }

    public void setPowerBackup(String powerBackup) {
        this.powerBackup = powerBackup;
    }

    public String getWaterSupply() {
        return waterSupply;
    }

    public void setWaterSupply(String waterSupply) {
        this.waterSupply = waterSupply;
    }

    public String getSecurity() {
        return security;
    }

    public void setSecurity(String security) {
        this.security = security;
    }

    public String getAmenities() {
        return amenities;
    }

    public void setAmenities(String amenities) {
        this.amenities = amenities;
    }

    public String getAvailableFrom() {
        return availableFrom;
    }

    public void setAvailableFrom(String availableFrom) {
        this.availableFrom = availableFrom;
    }

    public String getPreferredTenants() {
        return preferredTenants;
    }

    public void setPreferredTenants(String preferredTenants) {
        this.preferredTenants = preferredTenants;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAlternateContact() {
        return alternateContact;
    }

    public void setAlternateContact(String alternateContact) {
        this.alternateContact = alternateContact;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPreferredContact() {
        return preferredContact;
    }

    public void setPreferredContact(String preferredContact) {
        this.preferredContact = preferredContact;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

}
