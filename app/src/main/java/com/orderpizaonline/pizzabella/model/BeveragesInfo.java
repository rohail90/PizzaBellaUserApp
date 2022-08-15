package com.orderpizaonline.pizzabella.model;

import java.io.Serializable;
import java.util.ArrayList;

public class BeveragesInfo implements Serializable {
    private String id;
    private String beveragesType;
    private String name;
    private ArrayList<SizeInfo> sizesArrayList;
    private Boolean customization;
    private  ArrayList<String> coldDrinksInfo;
    private int discount;

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public void setColdDrinksInfo(ArrayList<String> coldDrinksInfo) {
        this.coldDrinksInfo = coldDrinksInfo;
    }

    public ArrayList<String> getColdDrinksInfo() {
        return coldDrinksInfo;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setBeveragesType(String beveragesType) {
        this.beveragesType = beveragesType;
    }

    public String getId() {
        return id;
    }

    public String getBeveragesType() {
        return beveragesType;
    }

    public BeveragesInfo() {
    }

    public void setSizesArrayList(ArrayList<SizeInfo> sizesArrayList) {
        this.sizesArrayList = sizesArrayList;
    }

    public void setCustomization(Boolean customization) {
        this.customization = customization;
    }

    public ArrayList<SizeInfo> getSizesArrayList() {
        return sizesArrayList;
    }

    public Boolean getCustomization() {
        return customization;
    }
}
