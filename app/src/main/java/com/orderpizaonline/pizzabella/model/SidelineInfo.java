package com.orderpizaonline.pizzabella.model;

public class SidelineInfo {
    private String id;
    private String sidelineName;
    private String imageURL;
    private String price;
    private String Category;
    private int discount;
    private SizePriceInfo sizePriceInfo;

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public int getDiscount() {
        return discount;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setCategory(String category) {
        this.Category = category;
    }

    public String getPrice() {
        return price;
    }

    public SizePriceInfo getSizePriceInfo() {
        return sizePriceInfo;
    }

    public void setSizePriceInfo(SizePriceInfo sizePriceInfo) {
        this.sizePriceInfo = sizePriceInfo;
    }

    public SidelineInfo(String id, String sidelineName, String imageURL, String price, String category, int discount) {
        this.id = id;
        this.sidelineName = sidelineName;
        this.imageURL = imageURL;
        this.price = price;
        Category = category;
        this.discount = discount;
    }

    public String getCategory() {
        return Category;
    }

    public SidelineInfo() {
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
