package com.example.cardviewgridviewsample.objects;

public class Users {
    String user_name, user_username, user_password;
    double user_budget;
    int user_gift_qty;

    public Users() {
    }

    public Users(String user_name, String user_username, String user_password) {
        this.user_name = user_name;
        this.user_username = user_username;
        this.user_password = user_password;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_username() {
        return user_username;
    }

    public void setUser_username(String user_username) {
        this.user_username = user_username;
    }

    public String getUser_password() {
        return user_password;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }

    public double getUser_budget() {
        return user_budget;
    }

    public void setUser_budget(double user_budget) {
        this.user_budget = user_budget;
    }

    public int getUser_gift_qty() {
        return user_gift_qty;
    }

    public void setUser_gift_qty(int user_gift_qty) {
        this.user_gift_qty = user_gift_qty;
    }
}
