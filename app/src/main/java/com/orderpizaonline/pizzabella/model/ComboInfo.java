package com.orderpizaonline.pizzabella.model;
import java.util.List;

public class ComboInfo {
    private String id;
    private String itemName;
    private String imageURL;
    private String price;
    private String type;
    private String description;
    private int friedRollQuantity;
    PizzaInfoInCombo pizzaInfo;
    private int burgerSandwichQuantity;
    private int discount;
    private List<FriedRollInfo> friedRollInfoList;
    private List<BurgerOrSandwichInfo> burgerOrSandwichInfoList;
    private BeveragesInfoInCombo beveragesInfoInCombo;



    public void setBurgerSandwichQuantity(int burgerSandwichQuantity) {
        this.burgerSandwichQuantity = burgerSandwichQuantity;
    }

    public int getBurgerSandwichQuantity() {
        return burgerSandwichQuantity;
    }


    public void setBeveragesInfoInCombo(BeveragesInfoInCombo beveragesInfoInCombo) {
        this.beveragesInfoInCombo = beveragesInfoInCombo;
    }

    public BeveragesInfoInCombo getBeveragesInfoInCombo() {
        return beveragesInfoInCombo;
    }

    public void setFriedRollInfoList(List<FriedRollInfo> friedRollInfoList) {
        this.friedRollInfoList = friedRollInfoList;
    }

    public void setBurgerOrSandwichInfoList(List<BurgerOrSandwichInfo> burgerOrSandwichInfoList) {
        this.burgerOrSandwichInfoList = burgerOrSandwichInfoList;
    }

    public List<FriedRollInfo> getFriedRollInfoList() {
        return friedRollInfoList;
    }

    public List<BurgerOrSandwichInfo> getBurgerOrSandwichInfoList() {
        return burgerOrSandwichInfoList;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDiscount() {
        return discount;
    }
    public void setPrice(String price) {
        this.price = price;
    }
    public String getPrice() {
        return price;
    }
    public ComboInfo() {
    }

    public void setPizzaInfo(PizzaInfoInCombo pizzaInfo) {
        this.pizzaInfo = pizzaInfo;
    }

    public PizzaInfoInCombo getPizzaInfo() {
        return pizzaInfo;
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

    public int getFriedRollQuantity() {
        return friedRollQuantity;
    }

    public void setFriedRollQuantity(int friedRollQuantity) {
        this.friedRollQuantity = friedRollQuantity;
    }
}