package com.eismayilzada.receipttracker;

public class Receipt {

    String storeName;
    String receiptId;
    String totalSum;
    String currency;

    public Receipt(String storeName,String receiptId,String totalSum,String currency){
        this.storeName=storeName;
        this.receiptId = receiptId;
        this.totalSum = totalSum;
        this.currency = currency;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(String receiptId) {
        this.receiptId = receiptId;
    }

    public String getTotalSum() {
        return totalSum;
    }

    public void setTotalSum(String totalSum) {
        this.totalSum = totalSum;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
