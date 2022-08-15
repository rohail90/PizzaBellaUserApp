package com.orderpizaonline.pizzabella.model;

public class SidelineOrderInfo {
    private String id;
    private String sidelineName;
    private String imageURL;
    private String price;
    private String Category;
    private int quantity;
    private int discount;

    private SizeInfo sizeInfo;
    private String totalPrice;
    private String beveragesType;
    private String name;
    private String coldDrinksInfo;

    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    public SizeInfo getSizeInfo() {
        return sizeInfo;
    }

    public void setSizeInfo(SizeInfo sizeInfo) {
        this.sizeInfo = sizeInfo;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getBeveragesType() {
        return beveragesType;
    }

    public void setBeveragesType(String beveragesType) {
        this.beveragesType = beveragesType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColdDrinksInfo() {
        return coldDrinksInfo;
    }

    public void setColdDrinksInfo(String coldDrinksInfo) {
        this.coldDrinksInfo = coldDrinksInfo;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public void setPrice(String price) {
        this.price = price;
    }


    public String getPrice() {
        return price;
    }



    public SidelineOrderInfo(String id, String sidelineName, String imageURL, String price, String category, int quanityt, int discount) {
        this.id = id;
        this.sidelineName = sidelineName;
        this.imageURL = imageURL;
        this.price = price;
        this.Category = category;
        this.quantity = quanityt;
        this.discount = discount;

    }

    public SidelineOrderInfo() {
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSidelineName() {
        return sidelineName;
    }

    public void setSidelineName(String sidelineName) {
        this.sidelineName = sidelineName;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

}
