package com.orderpizaonline.pizzabella.model;

public class AddressInfo {
    private String id;
    private String houseNo;
    private String street;
    private String area;

    public void setId(String id) {
        this.id = id;
    }

    public void setHouseNo(String houseNo) {
        this.houseNo = houseNo;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getId() {
        return id;
    }

    public String getHouseNo() {
        return houseNo;
    }

    public String getStreet() {
        return street;
    }

    public String getArea() {
        return area;
    }

    public AddressInfo() {
    }

}
