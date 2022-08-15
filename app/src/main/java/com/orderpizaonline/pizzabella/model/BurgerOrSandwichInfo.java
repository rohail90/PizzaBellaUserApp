package com.orderpizaonline.pizzabella.model;

import java.io.Serializable;

public class BurgerOrSandwichInfo implements Serializable {
    private String id;
    private String itemName;
    private String imageURL;
    private String description;
    private int discount;
    private int price;
    private  boolean customization;
    private OptionalInfo optionalInfo;
    private int quantity;public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
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

    public void setCustomization(boolean customization) {
        this.customization = customization;
    }

    public boolean isCustomization() {
        return customization;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public int getDiscount() {
        return discount;
    }

    public BurgerOrSandwichInfo() {
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
