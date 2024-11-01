package com.flightmanagement;

import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.event.*;

import java.util.Scanner;
import com.exceptions.InvalidChoiceException;

public class FlightReport {
    private FlightSchedule flightSchedule;
    private Scanner scanner;

    public FlightReport(FlightSchedule flightSchedule) {
        this.flightSchedule = flightSchedule;
        this.scanner = new Scanner(System.in);
    }

    public void generateReport() {
        if (flightSchedule.flightCount==0) {
            System.out.println("No flights available in the schedule.");
            return;
        }

        System.out.println("==========================================================================================");
        System.out.println("                                      FLIGHT REPORT                                       ");
        System.out.println("==========================================================================================");
        System.out.printf("%-15s %-15s %-15s %-15s %-15s\n", "Flight ID", "Flight Type", "Status", "Total Seats", "Available Seats");
        System.out.println("==========================================================================================");

        for (Flight flight : flightSchedule.flightList) {
            if (flight != null) {
                System.out.printf("%-15s %-15s %-15s %-15d %-15d\n", 
                    flight.flightId, 
                    flight.type, 
                    flight.status, 
                    flight.totalSeats, 
                    flight.getVacantSeats());
            }
        }

        System.out.println("==========================================================================================");
    }

    // Method to find flights with complete capacity
    public void reportFlightsWithCompleteCapacity() {
        System.out.print("Enter Origin City: ");
        String source = scanner.nextLine();
        
        System.out.print("Enter Destination City: ");
        String destination = scanner.nextLine();
    
        System.out.println("========================================");
        System.out.println("   FLIGHTS WITH COMPLETE CAPACITY      ");
        System.out.println("========================================");
    
        boolean foundFlight = false;
    
        for (Flight flight : flightSchedule.flightList) {
            if (flight != null && flight.origin.equalsIgnoreCase(source) && flight.destination.equalsIgnoreCase(destination)) {
                if (flight.getVacantSeats("Economy") == 0 &&
                    flight.getVacantSeats("Business") == 0 &&
                    flight.getVacantSeats("First") == 0 &&
                    flight.getVacantSeats("Residence") == 0) {
                    System.out.printf("Flight ID: %s, Flight Type: %s\n", flight.flightId, flight.type);
                    foundFlight = true;
                }
            }
        }
    
        if (!foundFlight) {
            System.out.println("No flights found with complete capacity for the specified route.");
        }
    
        System.out.println("========================================");
    }
    
    private void addDate(UNIQUEDATE[] dateList, DATE date) {
        for (int i = 0; i < dateList.length; i++) {
            if (dateList[i] == null) {
                dateList[i] = new UNIQUEDATE(date);
                return;
            }
        }
    }

    // Method to find periods of frequent bookings
    public void reportFrequentBookingPeriods() {
        DATE[] bookingDates = new DATE[flightSchedule.flightCount];
        int i;

        for (i=0; i<flightSchedule.flightCount; i++) {
            bookingDates[i] = flightSchedule.flightList[i].date;
        }

        UNIQUEDATE[] uniqueDates = new UNIQUEDATE[i+1];
        for (DATE d1 : bookingDates) {
            boolean exists = false;
            for (int j=0; j<uniqueDates.length; j++) {
                if  (uniqueDates[j] !=null && d1.equals(uniqueDates[j].date)) {
                    uniqueDates[j].count++;
                    exists = true;
                }
            }

            if (!exists) {
                addDate(uniqueDates, d1);
            }
        }

        System.out.println("========================================");
        System.out.println("        FREQUENT BOOKING PERIODS        ");
        System.out.println("========================================");
        for (int j=0; j<uniqueDates.length; j++) {
            if (uniqueDates[j]!=null) {
                System.out.printf("Date: %s, Bookings: %d\n", uniqueDates[j].date, uniqueDates[j].count);
            }
        }
        System.out.println("========================================");
    }

    private void addDestination(uniqueDestination[] destinationList, String destination) {
        for (int i=0; i<destinationList.length; i++) {
            if (destinationList[i]==null) {
                destinationList[i] = new uniqueDestination(destination);
                return;
            }
        }
    }
    
    // Method to report the most frequent destinations
    public void reportMostFrequentedDestinations() {
        String[] destinations = new String[flightSchedule.flightCount];
        int i;

        for (i=0; i<flightSchedule.flightCount; i++) {
            destinations[i] = flightSchedule.flightList[i].destination;
        }

        uniqueDestination[] uniqueDestinations = new uniqueDestination[i+1];
        for (String d : destinations) {
            boolean exists = false;
            for (int j=0; j<uniqueDestinations.length; j++) {
                if  (uniqueDestinations[j] !=null && d.equals(uniqueDestinations[j].destination)) {
                    uniqueDestinations[j].count++;
                    exists = true;
                }
            }

            if (!exists) {
                addDestination(uniqueDestinations, d);
            }
        }


        // Print the most frequent destinations
        System.out.println("=============================================");
        System.out.println("    MOST FREQUENTLY VISITED DESTINATIONS");
        System.out.println("=============================================");

        for (int j = 0; j < uniqueDestinations.length; j++) {
            System.out.printf("Destination: %s\n", uniqueDestinations[j]);
        }
        
        System.out.println("=============================================");
    }

    public void menu() throws InvalidChoiceException {
        boolean flightreport = true;
        while (flightreport) {
            System.out.println("\n1. Flight Report");
            System.out.println("2. Full Capacity Flights");
            System.out.println("3. Frequent Booking Periods");
            System.out.println("4. Frequent Destinations");
            System.out.println("5. Back");
            System.out.print("Choose an option: ");
            int flightReportChoice = scanner.nextInt();
            scanner.nextLine();
            System.out.println();
            switch (flightReportChoice) {
                case 1:
                    generateReport();
                    break;
                case 2:
                    reportFlightsWithCompleteCapacity();
                    break;
                case 3:
                    reportFrequentBookingPeriods();
                    break;
                case 4:
                    reportMostFrequentedDestinations();
                    break;
                case 5:
                    flightreport = false;
                    break;
                default:
                    throw new InvalidChoiceException("Invalid Menu Choice");
            }
        }
    }

    //####################################### JAVAFX IMPLEMENTATION PROGRAM BEGINS HERE ###############################################

    public GridPane getFlightReportGrid(Scene scene, GridPane sourceGrid) {
        GridPane flightReportGrid = new GridPane();
        flightReportGrid.setAlignment(Pos.CENTER);
        flightReportGrid.setHgap(10);
        flightReportGrid.setVgap(10);

        Label lblHeader = new Label("FLIGHT REPORT");
        flightReportGrid.add(lblHeader, 0, 0, 5, 1);

        String[] columns = {"Flight ID", "Flight Type", "Status", "Total Seats", "Available Seats"};
        for (int i=0; i<columns.length; i++) {
            flightReportGrid.add(new Label(columns[i]), i, 1);
        }

        int i=0;
        for (i=0; i<flightSchedule.flightCount; i++) {
            Flight flight = flightSchedule.flightList[i];
            if (flight != null) {
                flightReportGrid.add(new Label(flight.flightId), 0, i+2);
                flightReportGrid.add(new Label(String.valueOf(flight.type)), 1, i+2);
                flightReportGrid.add(new Label(flight.status), 2, i+2);
                flightReportGrid.add(new Label(String.valueOf(flight.totalSeats)), 3, i+2);
                flightReportGrid.add(new Label(String.valueOf(flight.getVacantSeats())), 4, i+2);
            }
        }

        Button btnBack = new Button("Back");
        flightReportGrid.add(btnBack, 0, i+3);

        btnBack.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent o) {
                scene.setRoot(sourceGrid);
            }
        });

        return flightReportGrid;
    }
}

class UNIQUEDATE {
    DATE date;
    int count;
    UNIQUEDATE(DATE d) {
        date=d;
        count=1;
    }
}

class uniqueDestination {
    String destination;
    int count;
    uniqueDestination(String destination) {
        this.destination=destination;
        count=1;
    }
}