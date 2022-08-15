package com.orderpizaonline.pizzabella.model;

public class PizzaInfo {
    private String id;
    private String itemName;
    private String imageURL;
    private int discount;
    private SizePriceInfo sizePriceInfo;
    private String FlavourId;

    public PizzaInfo(String id, String itemName, String imageURL, int discount, SizePriceInfo sizePriceInfo, String flavourId) {
        this.id = id;
        this.itemName = itemName;
        this.imageURL = imageURL;
        this.discount = discount;
        this.sizePriceInfo = sizePriceInfo;
        FlavourId = flavourId;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public void setSizePriceInfo(SizePriceInfo sizePriceInfo) {
        this.sizePriceInfo = sizePriceInfo;
    }

    public void setFlavourId(String flavourId) {
        FlavourId = flavourId;
    }

    public int getDiscount() {
        return discount;
    }

    public SizePriceInfo getSizePriceInfo() {
        return sizePriceInfo;
    }

    public String getFlavourId() {
        return FlavourId;
    }

    public PizzaInfo() {
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

/*
package com.orderpizaonline.pizzabella.model;

import java.util.ArrayList;

public class PizzaInfo {
    private String id;
    private String itemName;
    private String imageURL;
    private String price;
    private String size;
    private String type;
    private int discount;

    public void setType(String type) {
        this.type = type;
    }
    public String getType() {
        return type;
    }
    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public int getDiscount() {
        return discount;
    }
    public void setPrice(String price) {
        this.price = price;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getPrice() {
        return price;
    }

    public String getSize() {
        return size;
    }


    public PizzaInfo(String id, String itemName, String imageURL, String size) {
        this.id = id;
        this.itemName = itemName;
        this.imageURL = imageURL;
        this.size = size;
    }

    public PizzaInfo() {
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
*/
