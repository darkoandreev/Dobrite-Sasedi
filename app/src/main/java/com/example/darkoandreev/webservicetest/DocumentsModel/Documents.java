package com.example.darkoandreev.webservicetest.DocumentsModel;

/**
 * Created by darko.andreev on 5/25/2017.
 */

public class Documents  {

    private String issueDate;
    private String dueDate;
    private String documentNumber;
    private String amount;
    private String vat;
    private String totalAmount;
    private String balance;
    private String totalDiscount;
    private String forwardBalance;
    private String statusType;


    public String getStatusType() {
        return issueDate;
    }

    public void setStatusType(String statusType) {
        this.statusType = statusType;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getVat() {
        return vat;
    }

    public void setVat(String vat) {
        this.vat = vat;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getTotalDiscount() {
        return totalDiscount;
    }

    public void setTotalDiscount(String totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    public String getForwardBalance() {
        return forwardBalance;
    }

    public void setForwardBalance(String forwardBalance) {
        this.forwardBalance = forwardBalance;
    }

}
