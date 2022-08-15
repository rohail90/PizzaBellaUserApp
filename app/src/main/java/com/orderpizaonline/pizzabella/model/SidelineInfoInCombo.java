package com.orderpizaonline.pizzabella.model;

public class SidelineInfoInCombo {
    private String id;
    private String sidelineName;
    private int quantiy;
    private String imageURL;
    private String price;
    private String Category;
    private int discount;

    public void setSidelineName(String sidelineName) {
        this.sidelineName = sidelineName;
    }

    public String getSidelineName() {
        return sidelineName;
    }

    public SidelineInfoInCombo() {
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setQuantiy(int quantiy) {
        this.quantiy = quantiy;
    }
    public int getQuantiy() {
        return quantiy;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }
}
