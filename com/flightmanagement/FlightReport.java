package com.flightmanagement;

import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.event.*;

import com.exceptions.*;

public class FlightReport {
    static GridPane managerGrid;
    private FlightSchedule flightSchedule;

    public FlightReport(FlightSchedule flightSchedule) {
        this.flightSchedule = flightSchedule;
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
    private FlightSchedule reportFlightsWithCompleteCapacity() {
    
        boolean foundFlight = false;

        FlightSchedule filter = new FlightSchedule();

        for (Flight f : flightSchedule.flightList) {
            if (f != null && f.getVacantSeats()==0) {
                filter.addFlight(f);
                foundFlight = true;
            }
        }

        if (foundFlight) return filter;
        else return null;
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
    private UNIQUEDATE[] reportFrequentBookingPeriods() {
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

        return uniqueDates;
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
    private uniqueDestination[] reportMostFrequentedDestinations() {
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

        return uniqueDestinations;
        // Print the most frequent destinations
        // System.out.println("=============================================");
        // System.out.println("    MOST FREQUENTLY VISITED DESTINATIONS");
        // System.out.println("=============================================");

        // for (int j = 0; j < uniqueDestinations.length; j++) {
        //     System.out.printf("Destination: %s\n", uniqueDestinations[j]);
        // }
        
        // System.out.println("=============================================");
    }

    //####################################### JAVAFX IMPLEMENTATION PROGRAM BEGINS HERE ###############################################

    public GridPane getMenuGrid(Scene scene, GridPane sourceGrid) {
        managerGrid = sourceGrid;
        GridPane menuGrid = new GridPane();
        menuGrid.setAlignment(Pos.CENTER);
        menuGrid.setHgap(10);
        menuGrid.setVgap(10);

        Label lblHeader = new Label("Please select an option from the menu below:");
        Button btnGenReport = new Button("Generate Flight Report");
        btnGenReport.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        btnGenReport.setPrefWidth(150);
        Button btnFullCapacity = new Button("Full Capacity Flights");
        btnFullCapacity.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        btnFullCapacity.setPrefWidth(150);
        Button btnFreqPeriods = new Button("Frequent Booking Periods");
        btnFreqPeriods.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        Button btnFreqDestinations = new Button("Frequent Destinations");
        btnFreqDestinations.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        Button btnBack = new Button("Back");
        btnBack.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        btnGenReport.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent o) {
                scene.setRoot(getGenReportGrid(scene));
            }
        });

        btnFullCapacity.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent o) {
                try {
                    scene.setRoot(getFullCapacityGrid(scene));
                } catch (InvalidChoiceException e) {
                    @SuppressWarnings("unused")
                    ExceptionWindow error = new ExceptionWindow(e);
                }
            }
        });

        btnFreqPeriods.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent o) {
                try {
                    scene.setRoot(getFreqPeriodsGrid(scene));
                } catch (InvalidChoiceException e) {
                    @SuppressWarnings("unused")
                    ExceptionWindow error = new ExceptionWindow(e);
                }
            }
        });

        btnFreqDestinations.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent o) {
                try {
                    scene.setRoot(getFreqDestinationsGrid(scene));
                }  catch (InvalidChoiceException e) {
                    @SuppressWarnings("unused")
                    ExceptionWindow error = new ExceptionWindow(e);
                }
            }
        });

        btnBack.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent o) {
                scene.setRoot(managerGrid);
            }
        });

        menuGrid.add(lblHeader, 0, 0, 2, 1);
        menuGrid.add(btnGenReport, 0, 1);
        menuGrid.add(btnFullCapacity, 1, 1);
        menuGrid.add(btnFreqPeriods, 0, 2);
        menuGrid.add(btnFreqDestinations, 1, 2);
        menuGrid.add(btnBack, 0, 3, 2, 1);

        return menuGrid;
    }

    public GridPane getMenuGrid(Scene scene) {
        return getMenuGrid(scene, managerGrid);
    }

    GridPane getGenReportGrid(Scene scene) {
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
                scene.setRoot(getMenuGrid(scene));
            }
        });

        return flightReportGrid;
    }

    GridPane getFullCapacityGrid(Scene scene) throws InvalidChoiceException {
        FlightSchedule filter = reportFlightsWithCompleteCapacity();

        if (filter==null) {
            throw new InvalidChoiceException("No flights will complete capacity.");
        }

        GridPane fullCapacityGrid = filter.viewFlights();

        Label lblHeader = new Label("FLIGHTS WITH FULL CAPACITY");
        Button btnBack = new Button("<-");
        fullCapacityGrid.add(btnBack, 0, 0);
        fullCapacityGrid.add(lblHeader, 0, 1, 3, 1);

        btnBack.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent o) {
                scene.setRoot(getMenuGrid(scene));
            }
        });

        return fullCapacityGrid;
    }

    GridPane getFreqPeriodsGrid(Scene scene) throws InvalidChoiceException {
        UNIQUEDATE[] uniqueDates = reportFrequentBookingPeriods();

        if (uniqueDates[0]==null) {
            throw new InvalidChoiceException("No flights in system.");
        }

        GridPane freqPeriodsGrid = new GridPane();
        freqPeriodsGrid.setAlignment(Pos.CENTER);
        freqPeriodsGrid.setHgap(10);
        freqPeriodsGrid.setVgap(10);

        Label lblHeader = new Label("FREQUENT BOOKING PERIODS");
        Button btnBack = new Button("<-");
        freqPeriodsGrid.add(btnBack, 0, 0);
        freqPeriodsGrid.add(lblHeader, 0, 1, 3, 1);
        String[] columns = {"Date", "Count"};
        for (int i=0;  i<columns.length; i++) {
            freqPeriodsGrid.add(new Label(columns[i]), i, 2);
        }

        for (int i=0; i<uniqueDates.length; i++) {
            if (uniqueDates[i]!=null) {
                freqPeriodsGrid.add(new Label(String.valueOf(uniqueDates[i].date)), 0, i+3);
                freqPeriodsGrid.add(new Label(String.valueOf(uniqueDates[i].count)), 1, i+3);
            }
        }

        btnBack.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent o) {
                scene.setRoot(getMenuGrid(scene));
            }
        });

        return freqPeriodsGrid;
    }

    GridPane getFreqDestinationsGrid(Scene scene) throws InvalidChoiceException {
        uniqueDestination[] uniqueDestinations = reportMostFrequentedDestinations();

        if (uniqueDestinations[0]==null) {
            throw new InvalidChoiceException("No flights in system.");
        }

        GridPane freqDestionationsGrid = new GridPane();
        freqDestionationsGrid.setAlignment(Pos.CENTER);
        freqDestionationsGrid.setHgap(10);
        freqDestionationsGrid.setVgap(10);

        Label lblHeader = new Label("FREQUENTED DESTINATIONS");
        Button btnBack = new Button("<-");
        freqDestionationsGrid.add(btnBack, 0, 0);
        freqDestionationsGrid.add(lblHeader, 0, 1, 3, 1);
        String[] columns = {"Destination", "Count"};

        for (int i=0;  i<columns.length; i++) {
            freqDestionationsGrid.add(new Label(columns[i]), i, 2);
        }

        for (int i=0; i<uniqueDestinations.length; i++) {
            if (uniqueDestinations[i]!=null) {
                freqDestionationsGrid.add(new Label(uniqueDestinations[i].destination), 0, i+3);
                freqDestionationsGrid.add(new Label(String.valueOf(uniqueDestinations[i].count)), 1, i+3);
            }
        }

        btnBack.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent o) {
                scene.setRoot(getMenuGrid(scene));
            }
        });

        return freqDestionationsGrid;
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