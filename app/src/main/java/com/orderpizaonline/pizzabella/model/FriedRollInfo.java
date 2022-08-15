package com.orderpizaonline.pizzabella.model;

import java.io.Serializable;
import java.util.ArrayList;

public class FriedRollInfo implements Serializable {
    private String id;
    private String itemName;
    private String imageURL;
    private int discount;
    private int price;
    private  boolean customization;
    private ArrayList<QuantityInfo> variationList;
    private int quantity;
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }
    public void setCustomization(boolean customization) {
        this.customization = customization;
    }

    public void setVariationList(ArrayList<QuantityInfo> variationList) {
        this.variationList = variationList;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public boolean isCustomization() {
        return customization;
    }

    public ArrayList<QuantityInfo> getVariationList() {
        return variationList;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public int getDiscount() {
        return discount;
    }

    public FriedRollInfo() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

}
