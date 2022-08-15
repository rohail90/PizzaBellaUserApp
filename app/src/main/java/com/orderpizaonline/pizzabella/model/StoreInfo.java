package com.orderpizaonline.pizzabella.model;

public class StoreInfo {
    private String id;
    private String shortAddress;
    private String longAddress;
    private String phoneNumber;
    private boolean storeStatus;
    private Double lat;
    private Double lng;
    private String storeFCM;
    private String storeTiming;

    public void setStoreFCM(String storeFCM) {
        this.storeFCM = storeFCM;
    }

    public String getStoreFCM() {
        return storeFCM;
    }
    public void setLat(Double lat) {
        this.lat = lat;
    }

    public void setStoreStatus(boolean storeStatus) {
        this.storeStatus = storeStatus;
    }

    public void setStoreTiming(String storeTiming) {
        this.storeTiming = storeTiming;
    }

    public String getStoreTiming() {
        return storeTiming;
    }

    public boolean getStoreStatus() {
        return storeStatus;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Double getLat() {
        return lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setShortAddress(String shortAddress) {
        this.shortAddress = shortAddress;
    }

    public void setLongAddress(String longAddress) {
        this.longAddress = longAddress;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getId() {
        return id;
    }

    public String getShortAddress() {
        return shortAddress;
    }

    public String getLongAddress() {
        return longAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public StoreInfo() {
    }

}
