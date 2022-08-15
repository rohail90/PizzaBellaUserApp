package com.orderpizaonline.pizzabella.model;

import java.util.List;

public class PizzaOrderCartModel {
    private String pizzaName;
    private String totalPrice;
    private String pizzaType;
    private SidelineInfo dough;
    private SidelineInfo crust;
    private SidelineInfo sauce;
    private SidelineInfo spice;
    private ComboInfo comboInfo;

    private List<PizzaToppingInfo> toppingInfoList;
    private List<SidelineOrderInfo> pizzaExtraToppingList;
    //private List<Uri> toppingInfoList;

    private String id;
    private String imageURL;
    private String price;
    private int quantity;
    //PizzaInfoInCombo pizzaInfo;

    private String itemName;
    private int discount;
    private SizePriceInfo sizePriceInfo;
    private String FlavourId;

    private String sizePrice;
    private String sizeName;

    private PairValuesModel extraTopping;
    private PizzaToppingInfo pizzaToppingInfo;

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public SizePriceInfo getSizePriceInfo() {
        return sizePriceInfo;
    }

    public void setSizePriceInfo(SizePriceInfo sizePriceInfo) {
        this.sizePriceInfo = sizePriceInfo;
    }

    public String getFlavourId() {
        return FlavourId;
    }

    public void setFlavourId(String flavourId) {
        FlavourId = flavourId;
    }

    public String getSizePrice() {
        return sizePrice;
    }

    public void setSizePrice(String sizePrice) {
        this.sizePrice = sizePrice;
    }

    public String getSizeName() {
        return sizeName;
    }

    public void setSizeName(String sizeName) {
        this.sizeName = sizeName;
    }

    public PairValuesModel getExtraTopping() {
        return extraTopping;
    }

    public void setExtraTopping(PairValuesModel extraTopping) {
        this.extraTopping = extraTopping;
    }

    public PizzaToppingInfo getPizzaToppingInfo() {
        return pizzaToppingInfo;
    }

    public void setPizzaToppingInfo(PizzaToppingInfo pizzaToppingInfo) {
        this.pizzaToppingInfo = pizzaToppingInfo;
    }

    public List<SidelineOrderInfo> getPizzaExtraToppingList() {
        return pizzaExtraToppingList;
    }

    public void setPizzaExtraToppingList(List<SidelineOrderInfo> pizzaExtraToppingList) {
        this.pizzaExtraToppingList = pizzaExtraToppingList;
    }

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

    public ComboInfo getComboInfo() {
        return comboInfo;
    }

    public void setComboInfo(ComboInfo comboInfo) {
        this.comboInfo = comboInfo;
    }

    public void setPrice(String price) {
        this.price = price;
    }
    public String getPrice() {
        return price;
    }
    public PizzaOrderCartModel() {
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
