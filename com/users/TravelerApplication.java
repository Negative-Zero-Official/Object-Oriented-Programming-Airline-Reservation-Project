package com.users;

import javafx.animation.*;
import javafx.application.*;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.util.Duration;

public class TravelerApplication extends Application {
    Traveler trav;

    TravelerApplication(Traveler t) {
        trav = t;
    }

    GridPane getMainMenuRoot(Scene scene) {
        GridPane mainMenu = new GridPane();
        mainMenu.setAlignment(Pos.CENTER);
        mainMenu.setHgap(10);
        mainMenu.setVgap(10);

        Label lblWelcome = new Label("Welcome, "+trav.loginID);
        Label lblHeader = new Label("Please select the option of your choice:");
        Button btnDetails = new Button("Input Personal Details");
        Button btnFlightSchedule = new Button("View Flight Schedule");
        Button btnBook = new Button("Book a Flight");
        Button btnCancel = new Button("Cancel Flight Tickets");
        Button btnBack = new Button("Log Out");

        btnDetails.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent o) {
                scene.setRoot(getInputDetailsRoot(scene));
            }
        });

        btnBack.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent o) {
                Stage stage = (Stage)(scene.getWindow());
                stage.close();
            }
        });

        mainMenu.add(lblWelcome, 0, 0);
        mainMenu.add(lblHeader, 0, 1);
        mainMenu.add(btnDetails, 0, 2);
        mainMenu.add(btnFlightSchedule, 0, 3);
        mainMenu.add(btnBook, 0, 4);
        mainMenu.add(btnCancel, 0, 5);
        mainMenu.add(btnBack, 0, 6);

        return mainMenu;
    }

    GridPane getInputDetailsRoot(Scene scene) {
        GridPane inputDetails = new GridPane();
        inputDetails.setAlignment(Pos.CENTER);
        inputDetails.setHgap(10);
        inputDetails.setVgap(10);

        Label lblEmail = new Label("Email:");
        TextField tfEmail = new TextField();
        tfEmail.setPromptText("yourname@example.com");
        if (trav.email!=null) tfEmail.setText(trav.email);
        Label lblPhone = new Label("Phone Number:");
        TextField tfPhone = new TextField();
        tfPhone.setPromptText("1234567890");
        if (trav.contactNo!=null) tfPhone.setText(trav.contactNo);
        Label lblResponse = new Label();
        Button btnConfirm = new Button("Confirm");
        Button btnBack = new Button("Back");

        btnConfirm.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent o) {
                trav.email = tfEmail.getText();
                trav.contactNo = tfPhone.getText();
                lblResponse.setText("Details confirmed.\nReturning to main menu...\nPlease Wait");
                PauseTransition pause = new PauseTransition(Duration.seconds(2));
                pause.setOnFinished(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent o) {
                        scene.setRoot(getMainMenuRoot(scene));
                    }
                });
                pause.play();
            }
        });

        btnBack.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent o) {
                scene.setRoot(getMainMenuRoot(scene));
            }
        });

        inputDetails.add(lblEmail, 0, 0);
        inputDetails.add(tfEmail, 1, 0);
        inputDetails.add(lblPhone, 0, 1);
        inputDetails.add(tfPhone, 1, 1);
        inputDetails.add(btnConfirm, 0, 2);
        inputDetails.add(btnBack, 1, 2);
        inputDetails.add(lblResponse, 1, 3);

        return inputDetails;
    }

    public void start(Stage stage) {
        stage.setTitle("Traveler Login");
        Scene scene = new Scene(new FlowPane(), 600, 600);
        scene.setRoot(getMainMenuRoot(scene));
        stage.setScene(scene);
        stage.show();
    }
}
