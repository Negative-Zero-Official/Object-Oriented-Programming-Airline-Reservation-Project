import javafx.application.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.*;
import javafx.event.*;

import com.catering.*;
import com.dutyfree.*;
import com.exceptions.*;
import com.flightmanagement.*;
import com.users.*;
import java.util.Scanner;

// Suppress warnings about resource leaks (e.g., not closing the Scanner)
public class AirlineReservationSystem extends Application {
    // public static void main(String[] args) {
    //     // Create a Scanner to read user input
    //     Scanner scanner = new Scanner(System.in);

    //     // Initialize objects for flight schedule, duty-free management, catering menu management, and flight report
    static FlightSchedule flightSchedule = new FlightSchedule();
    static DutyFreeManagement dutyFreeManagement = new DutyFreeManagement();
    static CateringMenuManagement cateringMenuManagement = new CateringMenuManagement();
    static FlightReport flightReport = new FlightReport(flightSchedule);

    //     // Main loop to handle user interactions
    //     while (true) {
    //         // Display menu options
    //         System.out.println("1. Traveler Login");
    //         System.out.println("2. Manager Login");
    //         System.out.println("3. Exit");
    //         System.out.print("Choose an option: ");

    //         // Read user's choice
    //         int choice = scanner.nextInt();
    //         scanner.nextLine();  // Consume newline left-over

    //         // Handle user's choice
    //         switch (choice) {
    //             case 1:
    //                 // Traveler login
    //                 System.out.print("Enter Traveler Username: ");
    //                 String travelerUsername = scanner.nextLine();
    //                 System.out.print("Enter Traveler Password: ");
    //                 String travelerPassword = scanner.nextLine();
    //                 Traveler traveler = new Traveler(travelerUsername, travelerPassword, flightSchedule);
    //                 try {
    //                     // Start the traveler's thread
    //                     traveler.t.join();
    //                 } catch (InterruptedException e) {}  // Ignore InterruptedException
    //                 break;

    //             case 2:
    //                 // Manager login
    //                 System.out.print("Enter Manager Username: ");
    //                 String managerUsername = scanner.nextLine();
    //                 System.out.print("Enter Manager Password: ");
    //                 String managerPassword = scanner.nextLine();
    //                 // Simple check for manager credentials (can be expanded to check from a list or database)
    //                 if (managerUsername.equals("admin") && managerPassword.equals("Admin$123")) {
    //                     Manager manager = new Manager(managerUsername, managerPassword, flightSchedule, cateringMenuManagement, dutyFreeManagement, flightReport);
    //                     manager.displayInfo();
    //                     System.out.println();
    //                     manager.menu();  // Display manager's menu
    //                 } else {
    //                     System.out.println("Invalid Manager Username or Password. Try again.");
    //                 }
    //                 break;

    //             case 3:
    //                 // Exit the system
    //                 System.out.println("Exiting...");
    //                 return;

    //             default:
    //                 try {
    //                     // Throw an exception for invalid menu options
    //                     throw new InvalidChoiceException("Invalid Menu Option. Please choose again.");
    //                 } catch (InvalidChoiceException e) {
    //                     System.out.println(e.getMessage());
    //                 }
    //         }
    //     }
    // }

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
                Traveler trav = new Traveler(tfUsername.getText(), tfPassword.getText(), flightSchedule);
                trav.launcher();
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
        launch();
    }

    @Override
    public void start(Stage ps) {
        ps.setTitle("Airline Reservation System");

        Scene scene = new Scene(new FlowPane(), 600, 600);
        
        scene.setRoot(getMainRoot(scene));
        ps.setScene(scene);
        ps.show();
    }
}