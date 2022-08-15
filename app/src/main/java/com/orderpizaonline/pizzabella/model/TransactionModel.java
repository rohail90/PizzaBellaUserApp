package com.orderpizaonline.pizzabella.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TransactionModel {
    @SerializedName("TransactionId")
    @Expose
    private String transactionId;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("currencyIsoCode")
    @Expose
    private String currencyIsoCode;
    @SerializedName("marchentAccountId")
    @Expose
    private String marchentAccountId;
    @SerializedName("subMarchentAccountId")
    @Expose
    private String subMarchentAccountId;
    @SerializedName("masterMarchentAccountId")
    @Expose
    private String masterMarchentAccountId;
    @SerializedName("createAt")
    @Expose
    private String createAt;

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCurrencyIsoCode() {
        return currencyIsoCode;
    }

    public void setCurrencyIsoCode(String currencyIsoCode) {
        this.currencyIsoCode = currencyIsoCode;
    }

    public String getMarchentAccountId() {
        return marchentAccountId;
    }

    public void setMarchentAccountId(String marchentAccountId) {
        this.marchentAccountId = marchentAccountId;
    }

    public String getSubMarchentAccountId() {
        return subMarchentAccountId;
    }

    public void setSubMarchentAccountId(String subMarchentAccountId) {
        this.subMarchentAccountId = subMarchentAccountId;
    }

    public String getMasterMarchentAccountId() {
        return masterMarchentAccountId;
    }

    public void setMasterMarchentAccountId(String masterMarchentAccountId) {
        this.masterMarchentAccountId = masterMarchentAccountId;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }
}
