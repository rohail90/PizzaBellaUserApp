package com.orderpizaonline.pizzabella.model;

public class QuantityInfo {
    private String noOfPieces;
    private int price;

    public void setNoOfPieces(String noOfPieces) {
        this.noOfPieces = noOfPieces;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getNoOfPieces() {
        return noOfPieces;
    }

    public int getPrice() {
        return price;
    }
}
