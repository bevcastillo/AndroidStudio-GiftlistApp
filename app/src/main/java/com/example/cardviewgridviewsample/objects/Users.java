package com.example.cardviewgridviewsample.objects;

public class Users {
    String user_name, user_username, user_password;

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
}