package com.orderpizaonline.pizzabella.model;

public class SelectionModel {
    private String myAddress;
    private boolean isMyAddressSelected;
    private String outletAddress;
    private boolean isOutletAddressSelected;
    private String outletId;
    private String outletToken;


    public SelectionModel() {
    }

    public String getOutletToken() {
        return outletToken;
    }

    public void setOutletToken(String outletToken) {
        this.outletToken = outletToken;
    }

    public String getOutletId() {
        return outletId;
    }

    public void setOutletId(String outletId) {
        this.outletId = outletId;
    }

    public String getMyAddress() {
        return myAddress;
    }

    public void setMyAddress(String myAddress) {
        this.myAddress = myAddress;
    }

    public boolean isMyAddressSelected() {
        return isMyAddressSelected;
    }

    public void setMyAddressSelected(boolean myAddressSelected) {
        isMyAddressSelected = myAddressSelected;
    }

    public String getOutletAddress() {
        return outletAddress;
    }

    public void setOutletAddress(String outletAddress) {
        this.outletAddress = outletAddress;
    }

    public boolean isOutletAddressSelected() {
        return isOutletAddressSelected;
    }

    public void setOutletAddressSelected(boolean outletAddressSelected) {
        isOutletAddressSelected = outletAddressSelected;
    }
}
