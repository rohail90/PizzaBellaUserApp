package com.orderpizaonline.pizzabella.model;

import java.io.Serializable;

public class FriedRollOrderInfo implements Serializable {
    private String id;
    private String itemName;
    private String imageURL;
    private QuantityInfo variationList;
    private int quantity;
    private int singlePrice;
    private int totalPrice;
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public QuantityInfo getVariationList() {
        return variationList;
    }

    public void setVariationList(QuantityInfo variationList) {
        this.variationList = variationList;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getSinglePrice() {
        return singlePrice;
    }

    public void setSinglePrice(int singlePrice) {
        this.singlePrice = singlePrice;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public FriedRollOrderInfo() {
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
