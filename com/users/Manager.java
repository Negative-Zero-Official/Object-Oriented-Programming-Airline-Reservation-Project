package com.users;
import com.catering.CateringMenuManagement;
import com.dutyfree.DutyFreeManagement;
import com.exceptions.*;
import com.flightmanagement.*;
import java.util.Scanner;

import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;

@SuppressWarnings("resource")
public class Manager extends User {
    Stage stage = new Stage();
    FlightSchedule flightSchedule;
    CateringMenuManagement cateringMenuManagement;
    DutyFreeManagement dutyFreeManagement;
    FlightReport flightReport;
    Scanner scanner = new Scanner(System.in);
    public Manager(String loginID, String password, FlightSchedule sch, CateringMenuManagement cmm, DutyFreeManagement dfm, FlightReport fr) {
        super(loginID, password);
        flightSchedule = sch;
        cateringMenuManagement = cmm;
        dutyFreeManagement = dfm;
        flightReport = fr;
        start(stage);
    }

    public void displayInfo() {
        System.out.println("Welcome, Manager " + loginID + "!");
        System.out.println("You have administrator permissions.");
    }

    public void menu() {
        boolean managerMenu = true;
        while (managerMenu) {
            System.out.println("\n1. Manage Flights");
            System.out.println("2. Manage Duty-Free");
            System.out.println("3. Manage Catering");
            System.out.println("4. Logout");
            System.out.print("Choose an option: ");
            int managerChoice = scanner.nextInt();
            System.out.println();
            try {
                switch (managerChoice) {
                    case 1:
                        boolean flightManagement = true;
                        while (flightManagement) {
                            System.out.println("\n1. Flight Report");
                            System.out.println("2. Add Flight");
                            System.out.println("3. Update Flight");
                            System.out.println("4. View Flights");
                            System.out.println("5. Delete Flight");
                            System.out.println("6. Back");
                            System.out.print("Choose an option: ");
                            int flightManagementChoice = scanner.nextInt();
                            System.out.println();

                            switch (flightManagementChoice) {
                                case 1:
                                    flightReport.menu();
                                    break;
                                case 2:
                                    addFlight(flightSchedule);
                                    break;
                                case 3:
                                    updateFlight(flightSchedule);
                                    break;
                                case 4:
                                    // flightSchedule.viewFlights();
                                    break;
                                case 5:
                                    deleteFlight(flightSchedule);
                                    break;
                                case 6:
                                    flightManagement = false;
                                    break;
                                default:
                                    throw new InvalidChoiceException("Invalid choice. Please try again.");
                            }
                        }
                        break;
                    case 2:
                        dutyFreeManagement.manageDutyFree();
                        break;
                    case 3:
                        cateringMenuManagement.manageCatering();
                        break;
                    case 4:
                        managerMenu = false;
                        break;
                    default:
                        throw new InvalidChoiceException("Invalid choice. Please try again.");
                }
            } catch (InvalidChoiceException | ExceededMaxSizeException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    // Add flight method
    public void addFlight(FlightSchedule flightSchedule) throws ExceededMaxSizeException {
        if (flightSchedule.flightCount==flightSchedule.maxSize) {
            throw new ExceededMaxSizeException("Too many flights for system. Please create new schedule object to continue adding flights.");
        }
        Scanner sc = new Scanner(System.in);
        Flight flight = new Flight();

        System.out.print("Flight ID: ");
        flight.flightId = sc.nextLine();

        System.out.print("Flight type (DOMESTIC/INTERNATIONAL): ");
        String strFlightType = sc.nextLine();
        flight.type = FlightType.valueOf(strFlightType);

        System.out.print("Origin: ");
        flight.origin = sc.nextLine();

        System.out.print("Destination: ");
        flight.destination = sc.nextLine();

        System.out.println("Date: ");
        flight.setDate();

        System.out.print("Total Seats: ");
        flight.totalSeats = sc.nextInt();
        sc.nextLine(); // Consume newline

        System.out.print("Catering Availability (true/false): ");
        flight.cateringAvailable = sc.nextBoolean();
        sc.nextLine(); // Consume newline
        if (flight.cateringAvailable) {
            flight.manageCateringMenu();
        }

        System.out.print("Duty-Free Availability (true/false): ");
        flight.dutyFreeAvailable = sc.nextBoolean();
        sc.nextLine(); // Consume newline
        if (flight.dutyFreeAvailable) {
            flight.manageDutyFree();
        }

        System.out.print("Status: ");
        flight.status = sc.nextLine();

        System.out.print("Number of Economy Seats: ");
        flight.economySeats = sc.nextInt();
        flight.vacantEconomySeats = flight.economySeats;
        sc.nextLine(); // Consumer newline

        if (flight.economySeats>0) {
            System.out.print("Seat Price for Economy: ");
            flight.economySeatPrice = sc.nextDouble();
            sc.nextLine(); // Consume newline
        }

        System.out.print("Number of Business Seats: ");
        flight.businessSeats = sc.nextInt();
        flight.vacantBusinessSeats = flight.businessSeats;
        sc.nextLine(); // Consume newline

        if (flight.businessSeats>0) {
            System.out.print("Seat Price for Business: ");
            flight.businessSeatPrice = sc.nextDouble();
            sc.nextLine(); //Consume newline
        }

        System.out.print("Number of First Seats: ");
        flight.firstSeats = sc.nextInt();
        flight.vacantFirstSeats = flight.firstSeats;
        sc.nextLine(); // Consume newline

        if (flight.firstSeats>0) {
            System.out.print("Seat Price for First: ");
            flight.firstSeatPrice = sc.nextDouble();
            sc.nextLine(); // Consume newline
        }

        System.out.print("Number of Residence seats: ");
        flight.residenceSeats = sc.nextInt();
        flight.vacantResidenceSeats = flight.residenceSeats;
        sc.nextLine(); // Consume newline

        if (flight.residenceSeats>0) {
            System.out.print("Seat Price for Residence: ");
            flight.residenceSeatPrice = sc.nextDouble();
            sc.nextLine(); // Consume newline
        }

        // Add the flight to the schedule
        flightSchedule.addFlight(flight);
        System.out.println("Successfully added flight " + flight.flightId + " to the schedule.");
    }

    // Update flight method
    public void updateFlight(FlightSchedule sch) throws InvalidChoiceException {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter Flight ID of flight to update: ");
        String identifier = sc.nextLine();
        Flight flight = null;
        for (Flight fl : sch.flightList) {
            if (identifier.equals(fl.flightId)) {
                flight = fl;
                break;
            }
        }

        if (flight == null) {
            throw new InvalidChoiceException("Flight not found.");
        }

        System.out.println("\n1. Flight ID \n2. Flight type \n3. Origin \n4. Destination \n5. Date \n6. Total Seats \n7. Ticket Price \n8. Catering Availability \n9. Duty-Free Availability\n10. Status \n11. Exit");
        System.out.print("What do you want to update: ");
        int choice = sc.nextInt();
        sc.nextLine(); // Consume newline

        switch (choice) {
            case 1:
                System.out.println("Current Flight ID: " + flight.flightId);
                System.out.print("New Flight ID: ");
                flight.flightId = sc.nextLine();
                System.out.println("Flight ID updated successfully!");
                break;
            case 2:
                System.out.println("Current flight type: "+flight.type);
                System.out.print("New flight type: ");
                String strFlightType = sc.nextLine();
                flight.type = FlightType.valueOf(strFlightType);
                System.out.println("Flight type updated successfully!");
                break;
            case 3:
                System.out.println("Current Origin: " + flight.origin);
                System.out.print("New Origin: ");
                flight.origin = sc.nextLine();
                System.out.println("Origin updated successfully!");
                break;
            case 4:
                System.out.println("Current Destination: " + flight.destination);
                System.out.print("New Destination: ");
                flight.destination = sc.nextLine();
                System.out.println("Destination updated successfully!");
                break;
            case 5:
                System.out.println("Current Date: "+flight.getDate());
                System.out.println("Set new date: ");
                flight.setDate();
                System.out.println("Date updated scuccessfully.");
                break;
            case 6:
                System.out.println("Current Total Seats: " + flight.totalSeats);
                System.out.print("New Total Seats: ");
                flight.totalSeats = sc.nextInt();
                sc.nextLine(); // Consume newline
                System.out.println("Total seats updated successfully!");
                break;
            case 7:
                System.out.println("Current Economy Seat Price: " + flight.economySeatPrice);
                System.out.print("New Economy Seat Price: ");
                flight.economySeatPrice = sc.nextDouble();
                sc.nextLine(); //Consume newLine
                System.out.println("Economy seat price updated successfully!");
                break;
            case 8:
                System.out.println("Current Catering Availability: " + flight.cateringAvailable);
                System.out.print("New Catering Availability: ");
                flight.cateringAvailable = sc.nextBoolean();
                sc.nextLine(); // Consume newline
                System.out.println("Catering availability updated successfully!");
                if (flight.cateringAvailable) {
                    flight.manageCateringMenu();
                }
                break;
            case 9:
                System.out.println("Current Duty-Free Availability: "+flight.dutyFreeAvailable);
                System.out.print("New Duty-Free Availability: ");
                flight.dutyFreeAvailable = sc.nextBoolean();
                sc.nextLine();
                if (flight.dutyFreeAvailable) {
                    flight.manageDutyFree();
                }
                break;
            case 10:
                System.out.println("Current Status: " + flight.status);
                System.out.print("New Status: ");
                flight.status = sc.nextLine();
                System.out.println("Status updated successfully!");
                break;
            case 11:
                System.out.println("Exiting...");
                break;
            default:
                throw new InvalidChoiceException("Invalid choice!");
        }
        
    }

    // Method to delete flight
    public void deleteFlight(FlightSchedule flightSchedule) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter Flight ID to cancel: ");
        String flightId = sc.nextLine();
        
        for (Flight flight : flightSchedule.flightList) {
            if (flight != null && flight.flightId.equals(flightId)) {
                flight.status = "Canceled"; // Set status to Canceled
                System.out.println("Flight " + flightId + " has been canceled.");              
                return;
            }
        }
        System.out.println("Flight not found.");
        
    }

    //####################################### JAVAFX IMPLEMENTATION PROGRAM BEGINS HERE ###############################################
    
    public Stage getStage() {
        return stage;
    }

    @Override
    public void start(Stage ps) {
        this.stage = ps;
        stage.setTitle("Manager");
        Scene scene = new Scene(new FlowPane(), 1280, 720);
        scene.setRoot(getMainMenuGrid(scene));
        stage.setScene(scene);
    }

    @Override
    GridPane getMainMenuGrid(Scene scene) {
        GridPane mainMenuGrid = new GridPane();
        mainMenuGrid.setAlignment(Pos.CENTER);
        mainMenuGrid.setHgap(10);
        mainMenuGrid.setVgap(10);

        Label lblWelcome = new Label("Welcome, "+loginID);
        Label lblHeader = new Label("Please select an option from the menu below:");
        Button btnMFlights = new Button("Manage Flights");
        Button btnMDutyFree = new Button("Manage Duty-Free");
        Button btnMCatering = new Button("Manage Catering");
        Button btnLogOut = new Button("Log Out");
        btnMFlights.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        btnMDutyFree.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        btnMCatering.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        btnLogOut.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        btnMFlights.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent o) {
                scene.setRoot(getFlightMngGrid(scene));
            }
        });

        btnLogOut.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent o) {
                Stage stage = (Stage) scene.getWindow();
                stage.close();
            }
        });

        mainMenuGrid.add(lblWelcome, 0, 0, 2, 1);
        mainMenuGrid.add(lblHeader, 0, 1, 3, 1);
        mainMenuGrid.add(btnMFlights, 0, 2);
        mainMenuGrid.add(btnMDutyFree, 1, 2);
        mainMenuGrid.add(btnMCatering, 0, 3);
        mainMenuGrid.add(btnLogOut, 1, 3);

        return mainMenuGrid;
    }

    GridPane getFlightMngGrid(Scene scene) {
        GridPane flightMngGrid = new GridPane();
        flightMngGrid.setAlignment(Pos.CENTER);
        flightMngGrid.setHgap(10);
        flightMngGrid.setVgap(10);

        Label lblHeader = new Label("Please select an option from the menu below:");
        Button btnFlightReport = new Button("Flight Report");
        btnFlightReport.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        Button btnAddFlight = new Button("Add Flight");
        btnAddFlight.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        Button btnUpdateFlight = new Button("Update Flight");
        btnUpdateFlight.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        Button btnViewFlights = new Button("View Flights");
        btnViewFlights.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        Button btnDeleteFlight = new Button("Delete Flight");
        btnDeleteFlight.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        Button btnBack = new Button("Back");
        btnBack.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        btnFlightReport.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent o) {
                scene.setRoot(flightReport.getFlightReportGrid(scene, flightMngGrid));
            }
        });
        
        btnAddFlight.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent o) {
                try {
                    scene.setRoot(getAddFlightGrid(scene));
                } catch (ExceededMaxSizeException e) {
                    @SuppressWarnings("unused")
                    ExceptionWindow error = new ExceptionWindow(e);
                }
            }
        });

        btnUpdateFlight.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent o) {
                try {
                    scene.setRoot(getUpdateFlightGrid(scene));
                } catch (InvalidChoiceException e) {
                    @SuppressWarnings("unused")
                    ExceptionWindow error = new ExceptionWindow(e);
                }
            }
        });

        btnViewFlights.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent o) {
                scene.setRoot(getViewFlightGrid(scene));
            }
        });

        btnBack.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent o) {
                scene.setRoot(getMainMenuGrid(scene));
            }
        });

        flightMngGrid.add(lblHeader, 0, 0, 3, 1);
        flightMngGrid.add(btnFlightReport, 0, 1);
        flightMngGrid.add(btnAddFlight, 1, 1);
        flightMngGrid.add(btnUpdateFlight, 0, 2);
        flightMngGrid.add(btnViewFlights, 1, 2);
        flightMngGrid.add(btnDeleteFlight, 0, 3);
        flightMngGrid.add(btnBack, 1, 3);

        return flightMngGrid;
    }

    GridPane getAddFlightGrid(Scene scene) throws ExceededMaxSizeException {
        if (flightSchedule.flightCount==flightSchedule.maxSize) {
            throw new ExceededMaxSizeException("Too many flights for system. Please create new schedule object to continue adding flights.");
        }

        GridPane addFlightGrid = new GridPane();
        addFlightGrid.setAlignment(Pos.CENTER);
        addFlightGrid.setHgap(10);
        addFlightGrid.setVgap(10);

        Label lblHeader = new Label("ADD FLIGHT TO FLIGHT SCHEDULE");

        Label lblFlightId = new Label("Flight ID:");
        TextField tfFlightId = new TextField();

        Label lblFlightType = new Label("Flight Type:");
        ComboBox<FlightType> cboxFlightType = new ComboBox<>();
        cboxFlightType.getItems().addAll(FlightType.values());

        Label lblOrigin = new Label("Origin:");
        TextField tfOrigin = new TextField();

        Label lblDestination = new Label("Destination:");
        TextField tfDestination = new TextField();

        Label lblDate = new Label("DATE:");

        Label lblDay = new Label("Select day:");
        ComboBox<Integer> cboxDay = new ComboBox<>();
        for (int i=0; i<=31; i++) {
            cboxDay.getItems().add(i);
        }

        Label lblMonth = new Label("Select month:");
        ComboBox<Integer> cboxMonth = new ComboBox<>();
        for (int i=1; i<=12; i++) {
            cboxMonth.getItems().add(i);
        }

        Label lblYear = new Label("Select year:");
        ComboBox<Integer> cboxYear = new ComboBox<>();
        for (int i=2024; i<=2034; i++) {
            cboxYear.getItems().add(i);
        }

        Label lblCatering = new Label("Catering Availability:");
        ComboBox<Boolean> cboxCatering = new ComboBox<>();
        cboxCatering.getItems().addAll(Boolean.TRUE, Boolean.FALSE);
        cboxCatering.setValue(Boolean.FALSE);

        Label lblDutyFree = new Label("Catering Availability:");
        ComboBox<Boolean> cboxDutyFree = new ComboBox<>();
        cboxDutyFree.getItems().addAll(Boolean.TRUE, Boolean.FALSE);
        cboxDutyFree.setValue(Boolean.FALSE);

        Label lblStatus = new Label("Status:");
        ComboBox<String> cboxStatus = new ComboBox<>();
        cboxStatus.getItems().addAll("Scheduled", "Delayed", "Cancelled");
        cboxStatus.setValue("Scheduled");

        Label lblEconomySeats = new Label("Number of Economy Seats:");
        TextField tfEconomySeats = new TextField();

        Label lblEconomyPrice = new Label("Economy Seat Price:");
        TextField tfEconomyPrice = new TextField();

        Label lblBusinessSeats = new Label("Number of Business Seats:");
        TextField tfBusinessSeats = new TextField();

        Label lblBusinessPrice = new Label("Business Seat Price:");
        TextField tfBusinessPrice = new TextField();

        Label lblFirstSeats = new Label("Number of First Class Seats:");
        TextField tfFirstSeats = new TextField();

        Label lblFirstPrice = new Label("First Class Seat Price:");
        TextField tfFirstPrice = new TextField();

        Label lblResidenceSeats = new Label("Number of Residence Seats:");
        TextField tfResidenceSeats = new TextField();

        Label lblResidencePrice = new Label("Residence Seat Price:");
        TextField tfResidencePrice = new TextField();

        Label lblResponse = new Label();

        Button btnAdd = new Button("Add Flight");
        Button btnBack = new Button("Back");

        btnAdd.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent o) {
                if (
                    tfFlightId.getText().isBlank() || cboxFlightType.getSelectionModel().getSelectedItem()==null || tfOrigin.getText().isBlank() ||
                    tfDestination.getText().isBlank() || cboxDay.getSelectionModel().getSelectedItem()==null || cboxMonth.getSelectionModel().getSelectedItem()==null ||
                    cboxYear.getSelectionModel().getSelectedItem()==null
                ) {
                    try {
                        throw new InvalidChoiceException("One or more mandatory options were not selected");
                    } catch (InvalidChoiceException e) {
                        @SuppressWarnings("unused")
                        ExceptionWindow error = new ExceptionWindow(e);
                        lblResponse.setText("Adding flight failed.");
                        return;
                    }
                }

                DATE DoT = new DATE(cboxDay.getSelectionModel().getSelectedItem(),
                                    cboxMonth.getSelectionModel().getSelectedItem(),
                                    cboxYear.getSelectionModel().getSelectedItem());
                
                fillBlankTextField(tfEconomySeats);
                fillBlankTextField(tfEconomyPrice);
                fillBlankTextField(tfBusinessSeats);
                fillBlankTextField(tfBusinessPrice);
                fillBlankTextField(tfFirstSeats);
                fillBlankTextField(tfFirstPrice);
                fillBlankTextField(tfResidenceSeats);
                fillBlankTextField(tfResidencePrice);

                Flight flight = new Flight();
                flight.flightId = tfFlightId.getText();
                flight.type = cboxFlightType.getSelectionModel().getSelectedItem();
                flight.origin = tfOrigin.getText();
                flight.destination = tfDestination.getText();
                flight.date = DoT;
                flight.cateringAvailable = cboxCatering.getSelectionModel().getSelectedItem();
                flight.dutyFreeAvailable = cboxDutyFree.getSelectionModel().getSelectedItem();
                flight.status = cboxStatus.getSelectionModel().getSelectedItem();
                flight.economySeats = Integer.parseInt(tfEconomySeats.getText());
                flight.economySeatPrice = Double.parseDouble(tfEconomyPrice.getText());
                flight.businessSeats = Integer.parseInt(tfBusinessSeats.getText());
                flight.businessSeatPrice = Double.parseDouble(tfBusinessPrice.getText());
                flight.firstSeats = Integer.parseInt(tfFirstSeats.getText());
                flight.firstSeatPrice = Double.parseDouble(tfFirstPrice.getText());
                flight.residenceSeats = Integer.parseInt(tfResidenceSeats.getText());
                flight.residenceSeatPrice = Double.parseDouble(tfResidencePrice.getText());

                flight.fill();

                flightSchedule.addFlight(flight);
                lblResponse.setText("Successfully added flight "+flight.flightId+" to the flight schedule.");
            }
        });

        btnBack.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent o) {
                scene.setRoot(getFlightMngGrid(scene));
            }
        });

        addFlightGrid.add(lblHeader, 0, 0, 2, 1);
        addFlightGrid.add(lblFlightId, 0, 1);
        addFlightGrid.add(tfFlightId, 0, 2);
        addFlightGrid.add(lblFlightType, 1, 1);
        addFlightGrid.add(cboxFlightType, 1, 2);
        addFlightGrid.add(lblOrigin, 0, 3);
        addFlightGrid.add(tfOrigin, 0, 4);
        addFlightGrid.add(lblDestination, 1, 3);
        addFlightGrid.add(tfDestination, 1, 4);
        addFlightGrid.add(lblDate, 0, 5);
        addFlightGrid.add(lblDay, 0, 6);
        addFlightGrid.add(cboxDay, 0, 7);
        addFlightGrid.add(lblMonth, 1, 6);
        addFlightGrid.add(cboxMonth, 1, 7);
        addFlightGrid.add(lblYear, 2, 6);
        addFlightGrid.add(cboxYear, 2, 7);
        addFlightGrid.add(lblCatering, 0, 9);
        addFlightGrid.add(cboxCatering, 0, 10);
        addFlightGrid.add(lblDutyFree, 1, 9);
        addFlightGrid.add(cboxDutyFree, 1, 10);
        addFlightGrid.add(lblStatus, 0, 12);
        addFlightGrid.add(cboxStatus, 1, 12);
        addFlightGrid.add(lblEconomySeats, 0, 14);
        addFlightGrid.add(tfEconomySeats, 1, 14);
        addFlightGrid.add(lblEconomyPrice, 0, 15);
        addFlightGrid.add(tfEconomyPrice, 1, 15);
        addFlightGrid.add(lblBusinessSeats, 2, 14);
        addFlightGrid.add(tfBusinessSeats, 3, 14);
        addFlightGrid.add(lblBusinessPrice, 2, 15);
        addFlightGrid.add(tfBusinessPrice, 3, 15);
        addFlightGrid.add(lblFirstSeats, 0, 17);
        addFlightGrid.add(tfFirstSeats, 1, 17);
        addFlightGrid.add(lblFirstPrice, 0, 18);
        addFlightGrid.add(tfFirstPrice, 1, 18);
        addFlightGrid.add(lblResidenceSeats, 2, 17);
        addFlightGrid.add(tfResidenceSeats, 3, 17);
        addFlightGrid.add(lblResidencePrice, 2, 18);
        addFlightGrid.add(tfResidencePrice, 3, 18);
        addFlightGrid.add(btnAdd, 0, 19);
        addFlightGrid.add(btnBack, 1, 19);
        addFlightGrid.add(lblResponse, 0, 20, 2, 1);

        return addFlightGrid;
    }

    GridPane getUpdateFlightGrid(Scene scene) throws InvalidChoiceException {
        if (flightSchedule.flightCount<1) {
            throw new InvalidChoiceException("No flights to update. Please add a flight first.");
        }

        GridPane updateFlightGrid = new GridPane();
        updateFlightGrid.setAlignment(Pos.CENTER);
        updateFlightGrid.setHgap(10);
        updateFlightGrid.setVgap(10);

        Label lblHeader = new Label("UPDATE FLIGHT");

        Label lblFlightId = new Label("Flight ID:");
        ComboBox<Flight> cboxFlightId = new ComboBox<>();
        for (int i=0; i<flightSchedule.flightCount; i++) {
            cboxFlightId.getItems().add(flightSchedule.flightList[i]);
        }

        Label lblFlightType = new Label("Flight Type:");
        lblFlightType.setDisable(true);
        ComboBox<FlightType> cboxFlightType = new ComboBox<>();
        cboxFlightType.getItems().addAll(FlightType.values());
        cboxFlightType.setDisable(true);

        Label lblOrigin = new Label("Origin:");
        lblOrigin.setDisable(true);
        TextField tfOrigin = new TextField();
        tfOrigin.setDisable(true);

        Label lblDestination = new Label("Destination:");
        lblDestination.setDisable(true);
        TextField tfDestination = new TextField();
        tfDestination.setDisable(true);

        Label lblDate = new Label("DATE:");
        lblDate.setDisable(true);

        Label lblDay = new Label("Select day:");
        lblDay.setDisable(true);
        ComboBox<Integer> cboxDay = new ComboBox<>();
        for (int i=0; i<=31; i++) {
            cboxDay.getItems().add(i);
        }
        cboxDay.setDisable(true);

        Label lblMonth = new Label("Select month:");
        lblMonth.setDisable(true);
        ComboBox<Integer> cboxMonth = new ComboBox<>();
        for (int i=1; i<=12; i++) {
            cboxMonth.getItems().add(i);
        }
        cboxMonth.setDisable(true);

        Label lblYear = new Label("Select year:");
        lblYear.setDisable(true);
        ComboBox<Integer> cboxYear = new ComboBox<>();
        for (int i=2024; i<=2034; i++) {
            cboxYear.getItems().add(i);
        }
        cboxYear.setDisable(true);

        Label lblCatering = new Label("Catering Availability:");
        lblCatering.setDisable(true);
        ComboBox<Boolean> cboxCatering = new ComboBox<>();
        cboxCatering.getItems().addAll(Boolean.TRUE, Boolean.FALSE);
        cboxCatering.setDisable(true);

        Label lblDutyFree = new Label("Catering Availability:");
        lblDutyFree.setDisable(true);
        ComboBox<Boolean> cboxDutyFree = new ComboBox<>();
        cboxDutyFree.getItems().addAll(Boolean.TRUE, Boolean.FALSE);
        cboxDutyFree.setDisable(true);

        Label lblStatus = new Label("Status:");
        lblStatus.setDisable(true);
        ComboBox<String> cboxStatus = new ComboBox<>();
        cboxStatus.getItems().addAll("Scheduled", "Delayed", "Cancelled");
        cboxStatus.setDisable(true);

        Label lblEconomySeats = new Label("Number of Economy Seats:");
        lblEconomySeats.setDisable(true);
        TextField tfEconomySeats = new TextField();
        tfEconomySeats.setDisable(true);

        Label lblEconomyPrice = new Label("Economy Seat Price:");
        lblEconomyPrice.setDisable(true);
        TextField tfEconomyPrice = new TextField();
        tfEconomyPrice.setDisable(true);

        Label lblBusinessSeats = new Label("Number of Business Seats:");
        lblBusinessSeats.setDisable(true);
        TextField tfBusinessSeats = new TextField();
        tfBusinessSeats.setDisable(true);

        Label lblBusinessPrice = new Label("Business Seat Price:");
        lblBusinessPrice.setDisable(true);
        TextField tfBusinessPrice = new TextField();
        tfBusinessPrice.setDisable(true);

        Label lblFirstSeats = new Label("Number of First Class Seats:");
        lblFirstSeats.setDisable(true);
        TextField tfFirstSeats = new TextField();
        tfFirstSeats.setDisable(true);

        Label lblFirstPrice = new Label("First Class Seat Price:");
        lblFirstPrice.setDisable(true);
        TextField tfFirstPrice = new TextField();
        tfFirstPrice.setDisable(true);

        Label lblResidenceSeats = new Label("Number of Residence Seats:");
        lblResidenceSeats.setDisable(true);
        TextField tfResidenceSeats = new TextField();
        tfResidenceSeats.setDisable(true);

        Label lblResidencePrice = new Label("Residence Seat Price:");
        lblResidencePrice.setDisable(true);
        TextField tfResidencePrice = new TextField();
        tfResidencePrice.setDisable(true);

        Label lblResponse = new Label();
        lblResponse.setDisable(true);

        Button btnUpdate = new Button("Update Flight");
        btnUpdate.setDisable(true);
        Button btnBack = new Button("Back");

        cboxFlightId.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent o) {
                if (cboxFlightId.getSelectionModel().getSelectedItem()==null) {
                    cboxFlightType.getSelectionModel().clearSelection();
                    tfOrigin.clear();
                    tfDestination.clear();
                    cboxDay.getSelectionModel().clearSelection();
                    cboxMonth.getSelectionModel().clearSelection();
                    cboxYear.getSelectionModel().clearSelection();
                    cboxCatering.getSelectionModel().clearSelection();
                    cboxDutyFree.getSelectionModel().clearSelection();
                    cboxStatus.getSelectionModel().clearSelection();
                    tfEconomySeats.clear();
                    tfEconomyPrice.clear();
                    tfBusinessSeats.clear();
                    tfBusinessPrice.clear();
                    tfFirstSeats.clear();
                    tfFirstPrice.clear();
                    tfResidenceSeats.clear();
                    tfResidencePrice.clear();
                    lblFlightType.setDisable(true);
                    cboxFlightType.setDisable(true);
                    lblOrigin.setDisable(true);
                    tfOrigin.setDisable(true);
                    lblDestination.setDisable(true);
                    tfDestination.setDisable(true);
                    lblDate.setDisable(true);
                    lblDay.setDisable(true);
                    cboxDay.setDisable(true);
                    lblMonth.setDisable(true);
                    cboxMonth.setDisable(true);
                    lblYear.setDisable(true);
                    cboxYear.setDisable(true);
                    lblCatering.setDisable(true);
                    cboxCatering.setDisable(true);
                    lblDutyFree.setDisable(true);
                    cboxDutyFree.setDisable(true);
                    cboxStatus.setDisable(true);
                    lblEconomySeats.setDisable(true);
                    tfEconomySeats.setDisable(true);
                    lblEconomyPrice.setDisable(true);
                    tfEconomyPrice.setDisable(true);
                    lblBusinessSeats.setDisable(true);
                    tfBusinessSeats.setDisable(true);
                    lblBusinessPrice.setDisable(true);
                    tfBusinessPrice.setDisable(true);
                    lblFirstSeats.setDisable(true);
                    tfFirstSeats.setDisable(true);
                    lblFirstPrice.setDisable(true);
                    tfFirstPrice.setDisable(true);
                    lblResidenceSeats.setDisable(true);
                    tfResidenceSeats.setDisable(true);
                    lblResidencePrice.setDisable(true);
                    tfResidencePrice.setDisable(true);
                    lblResponse.setDisable(true);
                    btnUpdate.setDisable(true);
                    return;
                }

                Flight flight = cboxFlightId.getSelectionModel().getSelectedItem();

                lblFlightType.setDisable(false);
                cboxFlightType.setDisable(false);
                cboxFlightType.setValue(flight.type);

                lblOrigin.setDisable(false);
                tfOrigin.setDisable(false);
                tfOrigin.setText(flight.origin);

                lblDestination.setDisable(false);
                tfDestination.setDisable(false);
                tfDestination.setText(flight.destination);
                
                lblDate.setDisable(false);

                lblDay.setDisable(false);
                cboxDay.setDisable(false);
                cboxDay.setValue(flight.date.day);
                
                lblMonth.setDisable(false);
                cboxMonth.setDisable(false);
                cboxMonth.setValue(flight.date.month);

                lblYear.setDisable(false);
                cboxYear.setDisable(false);
                cboxYear.setValue(flight.date.year);

                lblCatering.setDisable(false);
                cboxCatering.setDisable(false);
                cboxCatering.setValue(flight.cateringAvailable);

                lblDutyFree.setDisable(false);
                cboxDutyFree.setDisable(false);
                cboxDutyFree.setValue(flight.dutyFreeAvailable);

                lblStatus.setDisable(false);
                cboxStatus.setDisable(false);
                cboxStatus.setValue(flight.status);

                lblEconomySeats.setDisable(false);
                tfEconomySeats.setDisable(false);
                tfEconomySeats.setText(String.valueOf(flight.economySeats));

                lblEconomyPrice.setDisable(false);
                tfEconomyPrice.setDisable(false);
                tfEconomyPrice.setText(String.valueOf(flight.economySeatPrice));

                lblBusinessSeats.setDisable(false);
                tfBusinessSeats.setDisable(false);
                tfBusinessSeats.setText(String.valueOf(flight.businessSeats));

                lblBusinessPrice.setDisable(false);
                tfBusinessPrice.setDisable(false);
                tfBusinessPrice.setText(String.valueOf(flight.businessSeatPrice));
                
                lblFirstSeats.setDisable(false);
                tfFirstSeats.setDisable(false);
                tfFirstSeats.setText(String.valueOf(flight.firstSeats));

                lblFirstPrice.setDisable(false);
                tfFirstPrice.setDisable(false);
                tfFirstPrice.setText(String.valueOf(flight.firstSeatPrice));

                lblResidenceSeats.setDisable(false);
                tfResidenceSeats.setDisable(false);
                tfResidenceSeats.setText(String.valueOf(flight.residenceSeats));

                lblResidencePrice.setDisable(false);
                tfResidencePrice.setDisable(false);
                tfResidencePrice.setText(String.valueOf(flight.residenceSeatPrice));

                lblResponse.setDisable(false);
                btnUpdate.setDisable(false);
            }
        });

        btnUpdate.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent o) {
                if (
                    cboxFlightId.getSelectionModel().getSelectedItem()==null || cboxFlightType.getSelectionModel().getSelectedItem()==null || tfOrigin.getText().isBlank() ||
                    tfDestination.getText().isBlank() || cboxDay.getSelectionModel().getSelectedItem()==null || cboxMonth.getSelectionModel().getSelectedItem()==null ||
                    cboxYear.getSelectionModel().getSelectedItem()==null
                ) {
                    try {
                        throw new InvalidChoiceException("One or more mandatory options were not selected");
                    } catch (InvalidChoiceException e) {
                        @SuppressWarnings("unused")
                        ExceptionWindow error = new ExceptionWindow(e);
                        lblResponse.setText("Updating flight failed.");
                        return;
                    }
                }

                DATE DoT = new DATE(cboxDay.getSelectionModel().getSelectedItem(),
                                    cboxMonth.getSelectionModel().getSelectedItem(),
                                    cboxYear.getSelectionModel().getSelectedItem());
                
                fillBlankTextField(tfEconomySeats);
                fillBlankTextField(tfEconomyPrice);
                fillBlankTextField(tfBusinessSeats);
                fillBlankTextField(tfBusinessPrice);
                fillBlankTextField(tfFirstSeats);
                fillBlankTextField(tfFirstPrice);
                fillBlankTextField(tfResidenceSeats);
                fillBlankTextField(tfResidencePrice);

                Flight flight = cboxFlightId.getSelectionModel().getSelectedItem();

                flight.type = cboxFlightType.getSelectionModel().getSelectedItem();
                flight.origin = tfOrigin.getText();
                flight.destination = tfDestination.getText();
                flight.date = DoT;
                flight.cateringAvailable = cboxCatering.getSelectionModel().getSelectedItem();
                flight.dutyFreeAvailable = cboxDutyFree.getSelectionModel().getSelectedItem();
                flight.status = cboxStatus.getSelectionModel().getSelectedItem();
                flight.economySeats = Integer.parseInt(tfEconomySeats.getText());
                flight.economySeatPrice = Double.parseDouble(tfEconomyPrice.getText());
                flight.businessSeats = Integer.parseInt(tfBusinessSeats.getText());
                flight.businessSeatPrice = Double.parseDouble(tfBusinessPrice.getText());
                flight.firstSeats = Integer.parseInt(tfFirstSeats.getText());
                flight.firstSeatPrice = Double.parseDouble(tfFirstPrice.getText());
                flight.residenceSeats = Integer.parseInt(tfResidenceSeats.getText());
                flight.residenceSeatPrice = Double.parseDouble(tfResidencePrice.getText());

                lblResponse.setText("Successfully updated flight "+flight.flightId);
            }
        });

        btnBack.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent o) {
                scene.setRoot(getFlightMngGrid(scene));
            }
        });

        updateFlightGrid.add(lblHeader, 0, 0, 2, 1);
        updateFlightGrid.add(lblFlightId, 0, 1);
        updateFlightGrid.add(cboxFlightId, 0, 2);
        updateFlightGrid.add(lblFlightType, 1, 1);
        updateFlightGrid.add(cboxFlightType, 1, 2);
        updateFlightGrid.add(lblOrigin, 0, 3);
        updateFlightGrid.add(tfOrigin, 0, 4);
        updateFlightGrid.add(lblDestination, 1, 3);
        updateFlightGrid.add(tfDestination, 1, 4);
        updateFlightGrid.add(lblDate, 0, 5);
        updateFlightGrid.add(lblDay, 0, 6);
        updateFlightGrid.add(cboxDay, 0, 7);
        updateFlightGrid.add(lblMonth, 1, 6);
        updateFlightGrid.add(cboxMonth, 1, 7);
        updateFlightGrid.add(lblYear, 2, 6);
        updateFlightGrid.add(cboxYear, 2, 7);
        updateFlightGrid.add(lblCatering, 0, 9);
        updateFlightGrid.add(cboxCatering, 0, 10);
        updateFlightGrid.add(lblDutyFree, 1, 9);
        updateFlightGrid.add(cboxDutyFree, 1, 10);
        updateFlightGrid.add(lblStatus, 0, 12);
        updateFlightGrid.add(cboxStatus, 1, 12);
        updateFlightGrid.add(lblEconomySeats, 0, 14);
        updateFlightGrid.add(tfEconomySeats, 1, 14);
        updateFlightGrid.add(lblEconomyPrice, 0, 15);
        updateFlightGrid.add(tfEconomyPrice, 1, 15);
        updateFlightGrid.add(lblBusinessSeats, 2, 14);
        updateFlightGrid.add(tfBusinessSeats, 3, 14);
        updateFlightGrid.add(lblBusinessPrice, 2, 15);
        updateFlightGrid.add(tfBusinessPrice, 3, 15);
        updateFlightGrid.add(lblFirstSeats, 0, 17);
        updateFlightGrid.add(tfFirstSeats, 1, 17);
        updateFlightGrid.add(lblFirstPrice, 0, 18);
        updateFlightGrid.add(tfFirstPrice, 1, 18);
        updateFlightGrid.add(lblResidenceSeats, 2, 17);
        updateFlightGrid.add(tfResidenceSeats, 3, 17);
        updateFlightGrid.add(lblResidencePrice, 2, 18);
        updateFlightGrid.add(tfResidencePrice, 3, 18);
        updateFlightGrid.add(btnUpdate, 0, 19);
        updateFlightGrid.add(btnBack, 1, 19);
        updateFlightGrid.add(lblResponse, 0, 20, 2, 1);

        return updateFlightGrid;
    }

    GridPane getViewFlightGrid(Scene scene) {
        GridPane viewFlightGrid = flightSchedule.viewFlights();
        Button btnBack = new Button("<-");

        btnBack.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent o) {
                scene.setRoot(getFlightMngGrid(scene));
            }
        });

        viewFlightGrid.add(btnBack, 0, 0);
        scene.setRoot(viewFlightGrid);
        return viewFlightGrid;

    }

    private void fillBlankTextField(TextField tf) {
        if (tf.getText().isBlank()) {
            tf.setText("0");
        }
    }
}
