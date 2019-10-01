package com.example.cardviewgridviewsample.objects;

public class Gift {
    String gift_name, gift_picture, gift_where_to_buy, gift_note, gift_wrap_status, gift_bought_status;
    double gift_price;

    public Gift() {
    }

    public Gift(String gift_name, String gift_where_to_buy, String gift_note, double gift_price) {
        this.gift_name = gift_name;
        this.gift_where_to_buy = gift_where_to_buy;
        this.gift_note = gift_note;
        this.gift_price = gift_price;
    }

    public String getGift_name() {
        return gift_name;
    }

    public void setGift_name(String gift_name) {
        this.gift_name = gift_name;
    }

    public String getGift_picture() {
        return gift_picture;
    }

    public void setGift_picture(String gift_picture) {
        this.gift_picture = gift_picture;
    }

    public String getGift_where_to_buy() {
        return gift_where_to_buy;
    }

    public void setGift_where_to_buy(String gift_where_to_buy) {
        this.gift_where_to_buy = gift_where_to_buy;
    }

    public String getGift_note() {
        return gift_note;
    }

    public void setGift_note(String gift_note) {
        this.gift_note = gift_note;
    }

    public double getGift_price() {
        return gift_price;
    }

    public void setGift_price(double gift_price) {
        this.gift_price = gift_price;
    }

    public String getGift_wrap_status() {
        return gift_wrap_status;
    }

    public void setGift_wrap_status(String gift_wrap_status) {
        this.gift_wrap_status = gift_wrap_status;
    }

    public String getGift_bought_status() {
        return gift_bought_status;
    }

    public void setGift_bought_status(String gift_bought_status) {
        this.gift_bought_status = gift_bought_status;
    }
}
