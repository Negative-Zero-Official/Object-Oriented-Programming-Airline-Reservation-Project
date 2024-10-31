package com.users;

import javafx.application.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;

public abstract class User extends Application {
    String loginID, password;

    public User(String loginID, String password) {
        this.loginID = loginID;
        this.password = password;
    }

    @Override
    public abstract void start(Stage ps);

    abstract GridPane getMainMenuGrid(Scene sc);
}