package com.orderpizaonline.pizzabella.model;

import java.io.Serializable;

public class SizeInfo implements Serializable {
    private String size;
    private int price;

    public void setSize(String size) {
        this.size = size;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getSize() {
        return size;
    }

    public int getPrice() {
        return price;
    }
}
