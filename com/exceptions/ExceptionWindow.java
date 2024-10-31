package com.exceptions;

import javafx.application.*;
import javafx.geometry.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.event.*;

public class ExceptionWindow extends Application {
    Exception exception;
    
    public ExceptionWindow(Exception e) {
        this.exception = e;
        start(new Stage());
    }

    @Override
    public void start(Stage ps) {
        ps.setTitle("ERROR!!!");
        GridPane gp = new GridPane();
        gp.setPadding(new Insets(10));
        gp.setAlignment(Pos.CENTER);
        gp.setHgap(10);
        gp.setVgap(10);

        Label lblErrorMessage = new Label(exception.getMessage());
        Button btnClose = new Button("Close");

        btnClose.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent o) {
                ps.close();
            }
        });

        gp.add(lblErrorMessage, 0, 0);
        gp.add(btnClose, 0, 1);

        Scene sc = new Scene(gp);
        ps.setScene(sc);
        ps.show();
    }
}
