package com.orderpizaonline.pizzabella.model;

public class PizzaOrderInfoBella {

    private String id;
    private String itemName;
    private String imageURL;
    private int discount;
    private SizePriceInfo sizePriceInfo;
    private String FlavourId;

    private int totalPrice;
    private SidelineInfo crust;
    private int quantity;
    private String singlePrice;

    private String sizePrice;
    private String sizeName;

    private PairValuesModel extraTopping;
    private PizzaToppingInfo pizzaToppingInfo;

    public String getSinglePrice() {
        return singlePrice;
    }

    public void setSinglePrice(String singlePrice) {
        this.singlePrice = singlePrice;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public int getDiscount() {
        return discount;
    }

    public PizzaOrderInfoBella() {
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

    public SidelineInfo getCrust() {
        return crust;
    }

    public void setCrust(SidelineInfo crust) {
        this.crust = crust;
    }
}
