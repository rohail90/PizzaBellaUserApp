package com.orderpizaonline.pizzabella.model;
import java.util.ArrayList;
import java.util.List;

public class ComboOrderInfo {
    private String id;
    private String itemName;
    private String imageURL;
    private String price;
    private int totalPrice;
    private int quantity;
    private String type;
    private String description;
    private String dealDescription;
    PizzaInfoInCombo pizzaInfo;
    List<PizzaToppingInfo> pizzaFlavours;
    private int burgerSandwichQuantity;
    private List<FriedRollInfo> friedRollInfoList;
    private List<BurgerOrSandwichInfo> burgerOrSandwichInfoList;
    private BeveragesInfoInCombo beveragesInfoInCombo;
    private List<PairValuesModel> coldPizList = new ArrayList<>();

    public void setBurgerSandwichQuantity(int burgerSandwichQuantity) {
        this.burgerSandwichQuantity = burgerSandwichQuantity;
    }

    public List<PizzaToppingInfo> getPizzaFlavours() {
        return pizzaFlavours;
    }

    public void setPizzaFlavours(List<PizzaToppingInfo> pizzaFlavours) {
        this.pizzaFlavours = pizzaFlavours;
    }


    public String getDealDescription() {
        return dealDescription;
    }

    public void setDealDescription(String dealDescription) {
        this.dealDescription = dealDescription;
    }

    public List<PairValuesModel> getColdPizList() {
        return coldPizList;
    }

    public void setColdPizList(List<PairValuesModel> coldPizList) {
        this.coldPizList = coldPizList;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public ComboOrderInfo() {
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

}