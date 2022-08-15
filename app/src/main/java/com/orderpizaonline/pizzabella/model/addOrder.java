package com.orderpizaonline.pizzabella.model;

import com.google.gson.annotations.SerializedName;

public class addOrder {
    @SerializedName("order")
    private PizzaSidelinesDBModel order;
    @SerializedName("transaction")
    private TransactionModel transaction;

    public addOrder(PizzaSidelinesDBModel order, TransactionModel transaction) {
        this.order = order;
        this.transaction = transaction;
    }

    public PizzaSidelinesDBModel getOrder() {
        return order;
    }

    public void setOrder(PizzaSidelinesDBModel order) {
        this.order = order;
    }

    public TransactionModel getTransaction() {
        return transaction;
    }

    public void setTransaction(TransactionModel transaction) {
        this.transaction = transaction;
    }
}
