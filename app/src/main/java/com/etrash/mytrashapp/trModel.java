package com.etrash.mytrashapp;

public class trModel {
    String Coin, Date, Payment_Method, Paymentdetails, Status;

    public trModel() {
    }

    public String getCoin() {
        return Coin;
    }

    public void setCoin(String coin) {
        Coin = coin;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getPayment_Method() {
        return Payment_Method;
    }

    public void setPayment_Method(String payment_Method) {
        Payment_Method = payment_Method;
    }

    public String getPaymentdetails() {
        return Paymentdetails;
    }

    public void setPaymentdetails(String paymentdetails) {
        Paymentdetails = paymentdetails;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
