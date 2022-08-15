package com.orderpizaonline.pizzabella.model;

import java.util.ArrayList;

public class ToppingInfo {
    private String id;
    private String itemName;
    private String imageURL;
    private String price;
    private String size;
    private String type;
    private int discount;
    private ArrayList<SidelineInfo> vegeList;


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
    public void setVegeList(ArrayList<SidelineInfo> vegeList) {
        this.vegeList = vegeList;
    }
    public ArrayList<SidelineInfo> getVegeList() {
        return vegeList;
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


    public ToppingInfo(String id, String itemName, String imageURL, String size) {
        this.id = id;
        this.itemName = itemName;
        this.imageURL = imageURL;
        this.size = size;
    }

    public ToppingInfo() {
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
