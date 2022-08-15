package com.orderpizaonline.pizzabella.model;

public class ChineseCornerOrderInfo {
    private String id;
    private String name;
    private String ImageURL;
    private int singlePrice;
    private int totalPrice;
    private int quantity;
    private int discountedPrice;

    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDiscountedPrice(int discountedPrice) {
        this.discountedPrice = discountedPrice;
    }
    public String getImageURL() {
        return ImageURL; }

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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL; }

    public int getDiscountedPrice() {
        return discountedPrice;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }


    public ChineseCornerOrderInfo() {
    }

}
