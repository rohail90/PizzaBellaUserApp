package com.orderpizaonline.pizzabella.model;

import java.io.Serializable;

public class BurgSandOrderInfo implements Serializable {
    private String id;
    private String itemName;
    private String imageURL;
    private String description;
    private int price;
    private int totalPrice;
    private int quantity;
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private OptionalInfo optionalInfo;

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice)  {
        this.totalPrice = totalPrice;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setOptionalInfo(OptionalInfo optionalInfo) {
        this.optionalInfo = optionalInfo;
    }

    public String getDescription() {
        return description;
    }

    public int getPrice() {
        return price;
    }

    public OptionalInfo getOptionalInfo() {
        return optionalInfo;
    }

    public BurgSandOrderInfo() {
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
