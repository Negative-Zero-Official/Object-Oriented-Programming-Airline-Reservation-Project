import javafx.application.*;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;

import com.catering.*;
import com.dutyfree.*;
import com.exceptions.*;
import com.flightmanagement.*;
import com.users.*;

public class AirlineReservationSystem extends Application {
    static FlightSchedule flightSchedule = new FlightSchedule();
    static DutyFreeManagement dutyFreeManagement = new DutyFreeManagement();
    static CateringMenuManagement cateringMenuManagement = new CateringMenuManagement();
    static FlightReport flightReport = new FlightReport(flightSchedule);

    //Main Login Scene Root
    static GridPane getMainRoot(Scene scene) {
        GridPane mainLoginGrid = new GridPane();
        mainLoginGrid.setAlignment(Pos.CENTER);
        mainLoginGrid.setHgap(10);
        mainLoginGrid.setVgap(10);

        Label lblTitle = new Label("Welcome to the Airline Reservation System.\nPlease select how you want to log in:");
        Button btnTraveler = new Button("Traveler Login");
        Button btnManager = new Button("Manager Login");
        Button btnExit = new Button("Exit");

        btnTraveler.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent o) {
                scene.setRoot(getTravLoginRoot(scene));
            }
        });

        btnManager.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent o) {
                scene.setRoot(getMngLoginRoot(scene));
            }
        });

        btnExit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent o) {
                System.exit(0);
            }
        });

        mainLoginGrid.add(lblTitle, 0, 0);
        mainLoginGrid.add(btnTraveler, 0, 1);
        mainLoginGrid.add(btnManager, 0, 2);
        mainLoginGrid.add(btnExit, 0, 3);

        return mainLoginGrid;
    }

    //Traveler Login Scene Root
    static GridPane getTravLoginRoot(Scene scene) {
        GridPane travLoginGrid = new GridPane();
        travLoginGrid.setAlignment(Pos.CENTER);
        travLoginGrid.setHgap(10);
        travLoginGrid.setVgap(10);

        Label lblHeader = new Label("TRAVELER LOGIN");
        Label lblUsername = new Label("Username:");
        TextField tfUsername = new TextField();
        tfUsername.setPromptText("Username");
        Label lblPassword = new Label("Password:");
        TextField tfPassword = new TextField();
        tfPassword.setPromptText("Password");
        Button btnLogin = new Button("Login");
        Button btnBack = new Button("Back");
        
        btnLogin.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent o) {
                if (tfUsername.getText().equals("") || tfPassword.getText().equals("")) {
                    try {
                        throw new InvalidChoiceException("One of the mandatory options was not provided.");
                    } catch (InvalidChoiceException e) {
                        @SuppressWarnings("unused")
                        ExceptionWindow error = new ExceptionWindow(e);
                    }
                    return;
                }
                Traveler trav = new Traveler(tfUsername.getText(), tfPassword.getText(), flightSchedule);
                trav.launcher();
                tfUsername.clear();
                tfPassword.clear();
            }
        });

        btnBack.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent o) {
                scene.setRoot(getMainRoot(scene));
            }
        });

        travLoginGrid.add(lblHeader, 1, 0);
        travLoginGrid.add(lblUsername, 0, 1);
        travLoginGrid.add(tfUsername, 2, 1);
        travLoginGrid.add(lblPassword, 0, 2);
        travLoginGrid.add(tfPassword, 2, 2);
        travLoginGrid.add(btnLogin, 1, 3);
        travLoginGrid.add(btnBack, 1, 4);

        return travLoginGrid;
    }

    //Manager Login Scene Root
    static GridPane getMngLoginRoot(Scene scene) {
        GridPane mngLoginGrid = new GridPane();
        mngLoginGrid.setAlignment(Pos.CENTER);
        mngLoginGrid.setHgap(10);
        mngLoginGrid.setVgap(10);

        Label lblHeader = new Label("MANAGER LOGIN");
        Label lblUsername = new Label("Username:");
        TextField tfUsername = new TextField();
        tfUsername.setPromptText("Username");
        Label lblPassword = new Label("Password:");
        TextField tfPassword = new TextField();
        tfPassword.setPromptText("Password");
        Button btnLogin = new Button("Login");
        Button btnBack = new Button("Back");

        btnBack.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent o) {
                scene.setRoot(getMainRoot(scene));
            }
        });

        mngLoginGrid.add(lblHeader, 1, 0);
        mngLoginGrid.add(lblUsername, 0, 1);
        mngLoginGrid.add(tfUsername, 2, 1);
        mngLoginGrid.add(lblPassword, 0, 2);
        mngLoginGrid.add(tfPassword, 2, 2);
        mngLoginGrid.add(btnLogin, 1, 3);
        mngLoginGrid.add(btnBack, 1, 4);

        return mngLoginGrid;
    }
    public static void main(String[] args) {

        //Adding random flights to flightSchedule for testing
        DATE date1 = new DATE(15, 10, 2025); // 15th October 2025
        DATE date2 = new DATE(20, 10, 2025); // 20th October 2025
        DATE date3 = new DATE(5, 11, 2024);  // 5th November 2024

        // Create random flights
        Flight flight1 = new Flight("FL123", "New York", "Los Angeles", date1, 
                                     FlightType.DOMESTIC, 200, true, true, 
                                     150, 150.0, 
                                     30, 300.0, 
                                     20, 500.0, 
                                     0, 0.0); // No residence seats

        Flight flight2 = new Flight("FL456", "London", "New York", date2, 
                                     FlightType.INTERNATIONAL, 300, true, false, 
                                     180, 200.0, 
                                     50, 400.0, 
                                     30, 600.0, 
                                     40, 250.0); // 40 residence seats

        Flight flight3 = new Flight("FL789", "Tokyo", "Seoul", date3, 
                                     FlightType.INTERNATIONAL, 150, false, true, 
                                     100, 100.0, 
                                     30, 250.0, 
                                     20, 400.0, 
                                     0, 0.0); // No residence seats
        flightSchedule.addFlight(flight1);
        flightSchedule.addFlight(flight2);
        flightSchedule.addFlight(flight3);

        launch();
    }

    @Override
    public void start(Stage ps) {
        ps.setTitle("Airline Reservation System");

        Scene scene = new Scene(new FlowPane(), 1280, 720);
        
        scene.setRoot(getMainRoot(scene));
        ps.setScene(scene);
        ps.show();
        // ps.setMaximized(true);
    }
}