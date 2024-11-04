package com.users;

import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;

import com.flightmanagement.*;
import com.exceptions.*;


public class Traveler extends User {
    int noOfSeats;
    String classChoice;
    String email=null;
    String contactNo=null;
    double bookedSeatPrice;

    FlightSchedule mainFlightSchedule;
    FlightSchedule filtered;
    Flight booked;

    public Traveler(String loginID, String password, FlightSchedule flightSchedule) {
        super(loginID, password);
        mainFlightSchedule = flightSchedule;
        start(new Stage());
    }

    public void filter(DATE DoT, String origin, String destination) throws InvalidChoiceException {
        filtered = new FlightSchedule();
        for (Flight f : mainFlightSchedule.flightList) {
            if (f != null && f.origin.equalsIgnoreCase(origin) && f.destination.equalsIgnoreCase(destination) && f.date.equals(DoT) && !f.status.equalsIgnoreCase("Cancelled")) {
                filtered.addFlight(f);
            }
        }

        if (filtered.flightList[0]==null) {
            throw new InvalidChoiceException("No flights found.");
        }
    }

    //####################################### JAVAFX IMPLEMENTATION PROGRAM BEGINS HERE ###############################################

    public void start(Stage stage) {
        stage.setTitle("Traveler");
        Scene scene = new Scene(new FlowPane(), 1280, 720);
        scene.setRoot(getMainMenuGrid(scene));
        stage.setScene(scene);
        stage.show();
    }

    @Override
    GridPane getMainMenuGrid(Scene scene) {
        GridPane mainMenu = new GridPane();
        mainMenu.setAlignment(Pos.CENTER);
        mainMenu.setHgap(10);
        mainMenu.setVgap(10);

        Label lblWelcome = new Label("Welcome, "+loginID);
        Label lblHeader = new Label("Please select an option from the menu below:");
        Button btnDetails = new Button("Input Personal Details");
        btnDetails.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        Button btnFlightSchedule = new Button("View Flight Schedule");
        btnFlightSchedule.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        Button btnBook = new Button("Book a Flight");
        btnBook.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        Button btnCancel = new Button("Cancel Flight Tickets");
        btnCancel.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        Button btnInvoice = new Button("View Invoice");
        btnInvoice.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        Button btnViewCatering = new Button("View Catering Options");
        btnViewCatering.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        Button btnViewDutyFree = new Button("View Duty Free Options");
        btnViewDutyFree.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        Button btnLogOut = new Button("Log Out");
        btnLogOut.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        btnDetails.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent o) {
                scene.setRoot(getInputDetailsGrid(scene));
            }
        });

        btnFlightSchedule.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent o) {
                scene.setRoot(getFlightSchedGrid(scene));
            }
        });

        btnBook.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent o) {
                try {
                    scene.setRoot(getBookingGridOne(scene));
                } catch (InvalidChoiceException e) {
                    @SuppressWarnings("unused")
                    ExceptionWindow error = new ExceptionWindow(e);
                }
            }
        });

        btnCancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent o) {
                try {
                    scene.setRoot(getCancelGrid(scene));
                } catch (InvalidChoiceException e) {
                    @SuppressWarnings("unused")
                    ExceptionWindow error = new ExceptionWindow(e);
                }
            }
        });

        btnInvoice.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent o) {
                if (booked==null) {
                    try {
                        throw new InvalidChoiceException("You have not booked a flight.\nPlease book a flight to generate invoice.");
                    } catch (InvalidChoiceException e) {
                        @SuppressWarnings("unused")
                        ExceptionWindow error = new ExceptionWindow(e);
                        return;
                    }
                }
                Stage invoice = new Stage();
                invoice.setTitle("Generated Invoice for Flight Booking");
                Scene invoiceScene = new Scene(new FlowPane(), 600, 600);
                invoiceScene.setRoot(getInvoiceGrid());
                invoice.setScene(invoiceScene);
                invoice.show();
            }
        });

        btnViewCatering.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent o) {
                if (booked==null || !booked.cateringAvailable) {
                    try {
                        throw new InvalidChoiceException("Flight with catering not booked.");
                    } catch (InvalidChoiceException e) {
                        @SuppressWarnings("unused")
                        ExceptionWindow error = new ExceptionWindow(e);
                        return;
                    }
                }

                scene.setRoot(booked.cateringMenu.getViewItemGrid(scene, mainMenu));
            }
        });

        btnViewDutyFree.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent o) {
                if (booked==null || !booked.dutyFreeAvailable) {
                    try {
                        throw new InvalidChoiceException("Flight with duty free not booked.");
                    } catch (InvalidChoiceException e) {
                        @SuppressWarnings("unused")
                        ExceptionWindow error = new ExceptionWindow(e);
                        return;
                    }
                }

                scene.setRoot(booked.DutyFree.getViewItemGrid(scene, mainMenu));
            }
        });

        btnLogOut.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent o) {
                Stage stage = (Stage)(scene.getWindow());
                stage.close();
            }
        });

        mainMenu.add(lblWelcome, 0, 0, 2, 1);
        mainMenu.add(lblHeader, 0, 1, 2, 1);
        mainMenu.add(btnDetails, 0, 2);
        mainMenu.add(btnFlightSchedule, 1, 2);
        mainMenu.add(btnBook, 0, 3);
        mainMenu.add(btnCancel, 1, 3);
        mainMenu.add(btnInvoice, 0, 4);
        mainMenu.add(btnViewCatering, 1, 4);
        mainMenu.add(btnViewDutyFree, 0, 5);
        mainMenu.add(btnLogOut, 1, 5);

        return mainMenu;
    }

    GridPane getInputDetailsGrid(Scene scene) {
        GridPane inputDetails = new GridPane();
        inputDetails.setAlignment(Pos.CENTER);
        inputDetails.setHgap(10);
        inputDetails.setVgap(10);

        Label lblEmail = new Label("Email:");
        TextField tfEmail = new TextField();
        tfEmail.setPromptText("yourname@example.com");
        if (email!=null) tfEmail.setText(email);
        Label lblPhone = new Label("Phone Number:");
        TextField tfPhone = new TextField();
        tfPhone.setPromptText("1234567890");
        if (contactNo!=null) tfPhone.setText(contactNo);
        Label lblResponse = new Label();
        Button btnConfirm = new Button("Confirm");
        Button btnBack = new Button("Back");

        btnConfirm.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent o) {
                email = tfEmail.getText();
                contactNo = tfPhone.getText();
                lblResponse.setText("Details confirmed.");
            }
        });

        btnBack.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent o) {
                scene.setRoot(getMainMenuGrid(scene));
            }
        });

        inputDetails.add(lblEmail, 0, 0);
        inputDetails.add(tfEmail, 1, 0);
        inputDetails.add(lblPhone, 0, 1);
        inputDetails.add(tfPhone, 1, 1);
        inputDetails.add(btnConfirm, 0, 2);
        inputDetails.add(btnBack, 1, 2);
        inputDetails.add(lblResponse, 1, 3, 2, 1);

        return inputDetails;
    }

    GridPane getFlightSchedGrid(Scene scene) {
        GridPane flightSchedGrid = mainFlightSchedule.viewFlights();

        Label lblHeader = new Label("ALL FLIGHTS SCHEDULE:");
        Button btnBack = new Button("Back");

        btnBack.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent o) {
                scene.setRoot(getMainMenuGrid(scene));
            }
        });

        flightSchedGrid.add(lblHeader, 0, 0, 2, 1);
        flightSchedGrid.add(btnBack, 0, mainFlightSchedule.flightCount+3);

        return flightSchedGrid;
    }

    GridPane getBookingGridOne(Scene scene) throws InvalidChoiceException {
        if (booked!=null) {
            throw new InvalidChoiceException("You have already booked a flight.  Please cancel your booking first.");
        }
        GridPane bookingGrid = new GridPane();
        bookingGrid.setAlignment(Pos.CENTER);
        bookingGrid.setHgap(10);
        bookingGrid.setVgap(10);

        Label lblHeader = new Label("FLIGHT BOOKING");

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

        Label lblOrigin = new Label("Enter origin:");
        TextField tfOrigin = new TextField();
        Label lblDestination = new Label("Enter destination:");
        TextField tfDestination = new TextField();

        Button btnSearch = new Button("Search");
        Button btnCancel = new Button("Cancel");

        btnSearch.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent o) {
                if (cboxDay.getValue()==null || cboxMonth.getValue()==null ||  cboxYear.getValue()==null || tfOrigin.getText()==null || tfDestination.getText()==null) {
                    try {
                        throw new InvalidChoiceException("One of the mandatory options was not selected.");
                    } catch (InvalidChoiceException e) {
                        @SuppressWarnings("unused")
                        ExceptionWindow error = new ExceptionWindow(e);
                    }
                }

                int day = cboxDay.getValue();
                int month = cboxMonth.getValue();
                int year = cboxYear.getValue();
                DATE d = new DATE(day, month, year);
                String origin = tfOrigin.getText();
                String destination = tfDestination.getText();

                try {
                    filter(d, origin, destination);
                } catch (InvalidChoiceException e) {
                    @SuppressWarnings("unused")
                    ExceptionWindow error = new ExceptionWindow(e);
                    return;
                }

                scene.setRoot(getBookingGridTwo(scene));
            }
        });

        btnCancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent o) {
                scene.setRoot(getMainMenuGrid(scene));
            }
        });

        bookingGrid.add(lblHeader, 0, 0);
        bookingGrid.add(lblDay, 0, 1);
        bookingGrid.add(cboxDay, 0, 2);
        bookingGrid.add(lblMonth, 1, 1);
        bookingGrid.add(cboxMonth, 1, 2);
        bookingGrid.add(lblYear, 2, 1);
        bookingGrid.add(cboxYear, 2, 2);
        bookingGrid.add(lblOrigin, 0, 3);
        bookingGrid.add(tfOrigin, 0, 4);
        bookingGrid.add(lblDestination, 1, 3);
        bookingGrid.add(tfDestination, 1, 4);
        bookingGrid.add(btnSearch, 0, 5);
        bookingGrid.add(btnCancel, 1, 5);

        return bookingGrid;
    }

    GridPane getBookingGridTwo(Scene scene) {
        GridPane filteredGrid = filtered.viewFlights();
        
        Label lblHeader = new Label("FILTERED FLIGHT LIST:");
        Label lblFlightId = new Label("Select flight to book:");
        ComboBox<Flight> cboxFlightOptions = new ComboBox<>();
        Button btnBook = new Button("Book");
        Button btnBack = new Button("Back");

        for (int i=0; i<filtered.flightCount; i++) {
            cboxFlightOptions.getItems().add(filtered.flightList[i]);
        }

        btnBook.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent o) {
                Flight selected = cboxFlightOptions.getSelectionModel().getSelectedItem();
                scene.setRoot(getBookingGridThree(scene, selected));
            }

        });

        btnBack.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent o) {
                try {
                    scene.setRoot(getBookingGridOne(scene));
                } catch (InvalidChoiceException e) {
                    @SuppressWarnings("unused")
                    ExceptionWindow error = new ExceptionWindow(e);
                }
            }
        });

        filteredGrid.add(lblHeader, 0, 0, 2, 1);
        filteredGrid.add(lblFlightId, 0, filtered.flightCount+3);
        filteredGrid.add(cboxFlightOptions, 1, filtered.flightCount+3);
        filteredGrid.add(btnBook, 0, filtered.flightCount+4);
        filteredGrid.add(btnBack, 1, filtered.flightCount+4);


        return filteredGrid;
    }

    GridPane getBookingGridThree(Scene scene, Flight selected) {

        GridPane bookingGrid = new GridPane();
        bookingGrid.setAlignment(Pos.CENTER);
        bookingGrid.setHgap(10);
        bookingGrid.setVgap(10);

        Label lblSelected = new Label("Selected flight: "+selected.flightId);
        Label lblClassSelect = new Label("Select Seat Class:");
        ComboBox<String> cboxClassOptions = new ComboBox<>();
        cboxClassOptions.getItems().addAll("Economy", "Business", "First", "Residence");
        Label lblSeatPrice = new Label();
        Label lblNoOfSeats = new Label("Select number of seats to book:");
        lblNoOfSeats.setVisible(false);
        Spinner<Integer> spnNoOfSeats = new Spinner<>();
        spnNoOfSeats.setVisible(false);
        Label lblResponse = new Label();
        Button btnBook = new Button("Book");
        btnBook.setDisable(true);
        Button btnBack = new Button("Back");

        cboxClassOptions.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent o) {
                String selectedItem = cboxClassOptions.getSelectionModel().getSelectedItem();
                if (selected.getVacantSeats(selectedItem)!=0) {
                    lblSeatPrice.setText("Price of each seat: "+selected.displayPrice(selectedItem));
                    lblNoOfSeats.setVisible(true);
                    SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, selected.getVacantSeats(selectedItem), 0, 1);
                    spnNoOfSeats.setValueFactory(valueFactory);
                    spnNoOfSeats.setVisible(true);
                    btnBook.setDisable(false);
                } else {
                    lblSeatPrice.setText("No seats available for selected class.");
                    lblNoOfSeats.setVisible(false);
                    spnNoOfSeats.setVisible(false);
                    btnBook.setDisable(true);
                }
            }
        });

        btnBook.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent o) {
                int selectedNoOfSeats = spnNoOfSeats.getValue();
                String selectedItem = cboxClassOptions.getSelectionModel().getSelectedItem();
                booked = selected;
                noOfSeats = selectedNoOfSeats;
                classChoice = selectedItem;
                bookedSeatPrice = booked.getPrice(classChoice);
                selected.updateVacantSeats(selectedItem, -1*noOfSeats);
                lblResponse.setText("Successfully booked "+noOfSeats+" "+selectedItem+" seats for Flight "+selected.flightId);
                btnBook.setDisable(true);
                Stage invoice = new Stage();
                invoice.setTitle("Generated Invoice for Flight Booking");
                Scene invoiceScene = new Scene(new FlowPane(), 600, 600);
                invoiceScene.setRoot(getInvoiceGrid());
                invoice.setScene(invoiceScene);
                invoice.show();
            }
        });

        btnBack.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent o) {
                if (booked == null) {
                    scene.setRoot(getBookingGridTwo(scene));
                } else {
                    scene.setRoot(getMainMenuGrid(scene));
                }
            }
        });

        bookingGrid.add(lblSelected, 0, 0);
        bookingGrid.add(lblClassSelect, 0, 1);
        bookingGrid.add(cboxClassOptions, 1, 1);
        bookingGrid.add(lblSeatPrice, 0, 2, 2, 1);
        bookingGrid.add(lblNoOfSeats, 0, 3);
        bookingGrid.add(spnNoOfSeats, 1, 3);
        bookingGrid.add(lblResponse, 0, 4, 2, 1);
        bookingGrid.add(btnBook, 0, 5);
        bookingGrid.add(btnBack, 1, 5);

        return bookingGrid;
    }

    GridPane getInvoiceGrid() {
        GridPane invoiceGrid = new GridPane();
        invoiceGrid.setAlignment(Pos.CENTER);
        invoiceGrid.setHgap(10);
        invoiceGrid.setVgap(10);

        Label lblInvoiceHeader = new Label("Invoice");
        Label lblFlightId = new Label("Flight ID: " + booked.flightId);
        Label lblClass = new Label("Class: " + classChoice);
        Label lblNoOfSeats = new Label("Number of Seats: " + noOfSeats);
        Label lblPrice = new Label(String.format("Price per Seat: $ %.2f", bookedSeatPrice));
        double subTotal = bookedSeatPrice * noOfSeats;
        double serviceTax = 0.05*subTotal;
        Label lblSubTotal = new Label(String.format("Sub Total: $ %.2f", subTotal));
        Label lblServiceTax = new Label(String.format("Service Tax: $ %.2f", serviceTax));
        Label lblTotalPrice = new Label(String.format("Total Price: $ %.2f", (subTotal+serviceTax)));
        Label lblDate = new Label("Date: " + booked.date);

        invoiceGrid.add(lblInvoiceHeader, 0, 0);
        invoiceGrid.add(lblFlightId, 0, 1);
        invoiceGrid.add(lblClass, 0, 2);
        invoiceGrid.add(lblNoOfSeats, 0, 3);
        invoiceGrid.add(lblPrice, 0, 4);
        invoiceGrid.add(lblSubTotal, 0, 5);
        invoiceGrid.add(lblServiceTax, 0, 6);
        invoiceGrid.add(lblTotalPrice, 0, 7);
        invoiceGrid.add(lblDate, 0, 8);

        return invoiceGrid;
    }

    GridPane getCancelGrid(Scene scene) throws InvalidChoiceException {
        if (booked==null) {
            throw new InvalidChoiceException("You have not booked a flight.");
        }
        GridPane cancelGrid = new GridPane();
        cancelGrid.setAlignment(Pos.CENTER);
        cancelGrid.setHgap(10);
        cancelGrid.setVgap(10);

        Label lblWarning = new Label("Are you sure you want to cancel your booking for flight "+booked.flightId+"?");
        Label lblWarning2 = new Label("THIS WILL DELETE ALL YOUR BOOKING INFORMATION.");
        Label lblWarning3 = new Label("THIS PROCESS CANNOT BE UNDONE.");
        Button btnConfirm = new Button("CONFIRM");
        Button btnBack = new Button("Back");
        Label lblConfirm = new Label("Enter password to cancel booking:");
        lblConfirm.setVisible(false);
        TextField tfConfirmationPassword = new TextField();
        tfConfirmationPassword.setPromptText("ENTER PASSWORD TO CONFIRM DELETION");
        tfConfirmationPassword.setVisible(false);
        Button btnConfirm2 = new Button("CANCEL BOOKING");
        btnConfirm2.setVisible(false);
        Label lblResponse = new Label();

        btnConfirm.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent o) {
                lblConfirm.setVisible(true);
                tfConfirmationPassword.setVisible(true);
                btnConfirm.setVisible(false);
                btnConfirm2.setVisible(true);
            }
        });

        btnConfirm2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent o) {
                if (tfConfirmationPassword.getText().equals(password)) {
                    lblResponse.setText("Booking cancelled successfully");
                    booked.updateVacantSeats(classChoice, noOfSeats);
                    noOfSeats = 0;
                    booked = null;
                    classChoice = "";
                    btnConfirm2.setDisable(true);
                } else {
                    lblResponse.setText("Incorrect password. Please try again");
                }
            }
        });

        btnBack.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent o) {
                scene.setRoot(getMainMenuGrid(scene));
            }
        });

        cancelGrid.add(lblWarning, 0, 0, 2, 1);
        cancelGrid.add(lblWarning2, 0, 1, 2, 1);
        cancelGrid.add(lblWarning3, 0, 2, 2, 1);
        cancelGrid.add(btnConfirm, 0, 5);
        cancelGrid.add(btnBack, 1, 5);
        cancelGrid.add(lblConfirm, 0, 3);
        cancelGrid.add(tfConfirmationPassword, 0, 4, 2, 1);
        cancelGrid.add(btnConfirm2, 0, 5);
        cancelGrid.add(lblResponse, 0, 6, 2, 1);

        return cancelGrid;
    }
}
