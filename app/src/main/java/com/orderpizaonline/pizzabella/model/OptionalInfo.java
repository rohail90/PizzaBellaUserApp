package com.orderpizaonline.pizzabella.model;

import java.io.Serializable;

public class OptionalInfo implements Serializable {
    private String optionalName;
    private int optionalPrice;
    public String getOptionalName() {
        return optionalName;
    }

    public int getOptionalPrice() {
        return optionalPrice;
    }

    public void setOptionalName(String optionalName) {
        this.optionalName = optionalName;
    }

    public void setOptionalPrice(int optionalPrice) {
        this.optionalPrice = optionalPrice;
    }
}
