package com.users;

public abstract class User {
    String loginID, password;

    public User(String loginID, String password) {
        this.loginID = loginID;
        this.password = password;
    }

    public abstract void launcher();
}