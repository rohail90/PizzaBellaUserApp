package com.orderpizaonline.pizzabella.model;

public class DrinksModel {
    private String DrinkName;
    private int id;

    public void setDrinkName(String drinksName) {
        this.DrinkName = drinksName;
    }

    public String getDrinkName() {
        return DrinkName;
    }

    public DrinksModel() {
    }

    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }
}
