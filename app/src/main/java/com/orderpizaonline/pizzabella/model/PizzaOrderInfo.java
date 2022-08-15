package com.orderpizaonline.pizzabella.model;

import java.util.List;

public class PizzaOrderInfo {
    private String pizzaName;
    private String pizzaPrice;
    private String totalPrice;
    private String pizzaType;
    private SidelineInfo dough;
    private SidelineInfo crust;
    private SidelineInfo sauce;
    private SidelineInfo spice;
    private ComboInfo comboInfo;
    private ChineseCornerInfo chineseCornerInfo;

    private List<PizzaToppingInfo> toppingInfoList;
    private List<SidelineInfo> sidelineInfoList;
    private List<SidelineOrderInfo> pizzaExtraToppingList;
    private List<SidelineOrderInfo> pizzaExtraDipsList;
    private List<SidelineOrderInfo> pizzaExtraDrinksList;
    private List<SidelineOrderInfo> pizzaExtraSidelinesList;
    private List<SidelineOrderInfo> pizzaExtraDessertsList;

    private String id;
    private String itemName;
    private String imageURL;
    private String price;
    PizzaInfoInCombo pizzaInfo;
    private int quantity;
    //PizzaInfoInCombo pizzaInfo;
    private int discount;

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getPizzaType() {
        return pizzaType;
    }

    public void setPizzaType(String pizzaType) {
        this.pizzaType = pizzaType;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public ChineseCornerInfo getChineseCornerInfo() {
        return chineseCornerInfo;
    }

    public void setChineseCornerInfo(ChineseCornerInfo chineseCornerInfo) {
        this.chineseCornerInfo = chineseCornerInfo;
    }

    public ComboInfo getComboInfo() {
        return comboInfo;
    }

    public void setComboInfo(ComboInfo comboInfo) {
        this.comboInfo = comboInfo;
    }

    public List<SidelineOrderInfo> getPizzaExtraDipsList() {
        return pizzaExtraDipsList;
    }

    public List<SidelineOrderInfo> getPizzaExtraSidelinesList() {
        return pizzaExtraSidelinesList;
    }

    public void setPizzaExtraSidelinesList(List<SidelineOrderInfo> pizzaExtraSidelinesList) {
        this.pizzaExtraSidelinesList = pizzaExtraSidelinesList;
    }

    public List<SidelineOrderInfo> getPizzaExtraDessertsList() {
        return pizzaExtraDessertsList;
    }

    public void setPizzaExtraDessertsList(List<SidelineOrderInfo> pizzaExtraDessertsList) {
        this.pizzaExtraDessertsList = pizzaExtraDessertsList;
    }

    public void setPizzaExtraDipsList(List<SidelineOrderInfo> pizzaExtraDipsList) {
        this.pizzaExtraDipsList = pizzaExtraDipsList;
    }

    public List<SidelineOrderInfo> getPizzaExtraDrinksList() {
        return pizzaExtraDrinksList;
    }

    public void setPizzaExtraDrinksList(List<SidelineOrderInfo> pizzaExtraDrinksList) {
        this.pizzaExtraDrinksList = pizzaExtraDrinksList;
    }

    public List<SidelineOrderInfo> getPizzaExtraToppingList() {
        return pizzaExtraToppingList;
    }

    public void setPizzaExtraToppingList(List<SidelineOrderInfo> pizzaExtraToppingList) {
        this.pizzaExtraToppingList = pizzaExtraToppingList;
    }

    public List<SidelineInfo> getSidelineInfoList() {
        return sidelineInfoList;
    }

    public void setSidelineInfoList(List<SidelineInfo> sidelineInfoList) {
        this.sidelineInfoList = sidelineInfoList;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public int getDiscount() {
        return discount;
    }

    public void setSidelineInfo(SidelineInfoInCombo sidelineInfo) {
        this.sidelineInfo = sidelineInfo;
    }

    public void setDrinksInfo(DrinksInfoInCombo drinksInfo) {
        this.drinksInfo = drinksInfo;
    }

    public DrinksInfoInCombo getDrinksInfo() {
        return drinksInfo;
    }

    public SidelineInfoInCombo getSidelineInfo() {
        return sidelineInfo;
    }

    private SidelineInfoInCombo sidelineInfo;
    private DrinksInfoInCombo drinksInfo;
    public void setPrice(String price) {
        this.price = price;
    }
    public String getPrice() {
        return price;
    }
    public PizzaOrderInfo() {
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

    public String getPizzaName() {
        return pizzaName;
    }

    public void setPizzaName(String pizzaName) {
        this.pizzaName = pizzaName;
    }

    public String getPizzaPrice() {
        return pizzaPrice;
    }

    public void setPizzaPrice(String pizzaPrice) {
        this.pizzaPrice = pizzaPrice;
    }

    public SidelineInfo getDough() {
        return dough;
    }

    public void setDough(SidelineInfo dough) {
        this.dough = dough;
    }

    public SidelineInfo getCrust() {
        return crust;
    }

    public void setCrust(SidelineInfo crust) {
        this.crust = crust;
    }

    public SidelineInfo getSauce() {
        return sauce;
    }

    public void setSauce(SidelineInfo sauce) {
        this.sauce = sauce;
    }

    public SidelineInfo getSpice() {
        return spice;
    }

    public void setSpice(SidelineInfo spice) {
        this.spice = spice;
    }

    public List<PizzaToppingInfo> getToppingInfoList() {
        return toppingInfoList;
    }

    public void setToppingInfoList(List<PizzaToppingInfo> toppingInfoList) {
        this.toppingInfoList = toppingInfoList;
    }
}
