package com.example.kid_toy_store.model;

public class CardInfo {

    public String card_number;
    public String expiry_date;
    public String cvv;

    public CardInfo(String card_number, String expiry_date, String cvv) {
        this.card_number = card_number;
        this.expiry_date = expiry_date;
        this.cvv = cvv;
    }

    public String getCard_number() {
        return card_number;
    }

    public void setCard_number(String card_number) {
        this.card_number = card_number;
    }

    public String getExpiry_date() {
        return expiry_date;
    }

    public void setExpiry_date(String expiry_date) {
        this.expiry_date = expiry_date;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }
}
