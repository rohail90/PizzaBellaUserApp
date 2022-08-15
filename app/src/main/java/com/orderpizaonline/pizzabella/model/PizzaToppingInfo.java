package com.orderpizaonline.pizzabella.model;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class PizzaToppingInfo {
    private String id;
    private String itemName;
    private String imageURL;
    private String price;
    private Bitmap image;
    private String imageUri;

    private String leftTop;
    private String leftBottom;
    private String rightTop;
    private String rightBottom;
    private String rightHalf;
    private String leftHalf;

    ArrayList<SidelineInfo> vegeInfoArrayList;

    public void setVegeInfoArrayList(ArrayList<SidelineInfo> vegeInfoArrayList) {
        this.vegeInfoArrayList = vegeInfoArrayList;
    }

    public ArrayList<SidelineInfo> getVegeInfoArrayList() {
        return vegeInfoArrayList;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public PizzaToppingInfo() {
    }
    public void setPrice(String price) {
        this.price = price;
    }
    public String getPrice() {
        return price;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
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

    public String getLeftTop() {
        return leftTop;
    }

    public void setLeftTop(String leftTop) {
        this.leftTop = leftTop;
    }

    public String getLeftBottom() {
        return leftBottom;
    }

    public void setLeftBottom(String leftBottom) {
        this.leftBottom = leftBottom;
    }

    public String getRightTop() {
        return rightTop;
    }

    public void setRightTop(String rightTop) {
        this.rightTop = rightTop;
    }

    public String getRightBottom() {
        return rightBottom;
    }

    public void setRightBottom(String rightBottom) {
        this.rightBottom = rightBottom;
    }

    public String getRightHalf() {
        return rightHalf;
    }

    public void setRightHalf(String rightHalf) {
        this.rightHalf = rightHalf;
    }

    public String getLeftHalf() {
        return leftHalf;
    }

    public void setLeftHalf(String leftHalf) {
        this.leftHalf = leftHalf;
    }
}
