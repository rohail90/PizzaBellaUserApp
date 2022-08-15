package com.orderpizaonline.pizzabella.model;

public class VegeInfo {
    private String id;
    private String vegeName;
    private String imageURL;

    public VegeInfo(String id, String vegeName, String imageURL) {
        this.id = id;
        this.vegeName = vegeName;
        this.imageURL = imageURL;

    }

    public VegeInfo() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVegeName() {
        return vegeName;
    }

    public void setVegeName(String vegeName) {
        this.vegeName = vegeName;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

}
