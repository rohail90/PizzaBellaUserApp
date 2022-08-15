package com.orderpizaonline.pizzabella.model;

import java.util.List;

public class PizzaSidelinesDBModel {

    private String id;
    private String userId;
    private String userPhone;
    private int orderStatus;
    private String name;
    private String orderNotes;
    private String discountedCode;
    private String outletAddress;
    private String outletId;
    private String outletFCMToken;
    private String userAddress;
    private String orderDate;
    private String date;
    private String orderPrice;
    private String paymentMethod;
    public List<SidelineOrderInfo> sidelinesOrder;
    public List<PizzaOrderCartModel> pizzaOrder;
    public List<ChineseCornerOrderInfo> chineseOrder;
    public List<FriedRollOrderInfo> friedOrder;
    public List<ComboOrderInfo> mealOrder;
    public List<ComboOrderInfo> dealOrder;

    private String orderType;

    public List<ComboOrderInfo> getDealOrder() {
        return dealOrder;
    }

    public void setDealOrder(List<ComboOrderInfo> dealOrder) {
        this.dealOrder = dealOrder;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public PizzaSidelinesDBModel() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<ComboOrderInfo> getMealOrder() {
        return mealOrder;
    }

    public void setMealOrder(List<ComboOrderInfo> mealOrder) {
        this.mealOrder = mealOrder;
    }

    public List<FriedRollOrderInfo> getFriedOrder() {
        return friedOrder;
    }

    public void setFriedOrder(List<FriedRollOrderInfo> friedOrder) {
        this.friedOrder = friedOrder;
    }

    public List<ChineseCornerOrderInfo> getChineseOrder() {
        return chineseOrder;
    }

    public void setChineseOrder(List<ChineseCornerOrderInfo> chineseOrder) {
        this.chineseOrder = chineseOrder;
    }

    public String getOutletFCMToken() {
        return outletFCMToken;
    }

    public void setOutletFCMToken(String outletFCMToken) {
        this.outletFCMToken = outletFCMToken;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<SidelineOrderInfo> getSidelinesOrder() {
        return sidelinesOrder;
    }

    public void setSidelinesOrder(List<SidelineOrderInfo> sidelinesOrder) {
        this.sidelinesOrder = sidelinesOrder;
    }

    public List<PizzaOrderCartModel> getPizzaOrder() {
        return pizzaOrder;
    }

    public void setPizzaOrder(List<PizzaOrderCartModel> pizzaOrder) {
        this.pizzaOrder = pizzaOrder;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrderNotes() {
        return orderNotes;
    }

    public void setOrderNotes(String orderNotes) {
        this.orderNotes = orderNotes;
    }

    public String getDiscountedCode() {
        return discountedCode;
    }

    public void setDiscountedCode(String discountedCode) {
        this.discountedCode = discountedCode;
    }

    public String getOutletAddress() {
        return outletAddress;
    }

    public void setOutletAddress(String outletAddress) {
        this.outletAddress = outletAddress;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getOutletId() {
        return outletId;
    }

    public void setOutletId(String outletId) {
        this.outletId = outletId;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(String orderPrice) {
        this.orderPrice = orderPrice;
    }

    public String getPaymentMethod() { return paymentMethod; }

    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
}
