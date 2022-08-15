package com.orderpizaonline.pizzabella.model;

public class DrinksInfoInCombo {
    private String drinksSize;
    private int quantiy;

    public void setDrinksSize(String drinksName) {
        this.drinksSize = drinksName;
    }

    public String getDrinksSize() {
        return drinksSize;
    }

    public DrinksInfoInCombo() {
    }

    public void setQuantiy(int quantiy) {
        this.quantiy = quantiy;
    }
    public int getQuantiy() {
        return quantiy;
    }
}
