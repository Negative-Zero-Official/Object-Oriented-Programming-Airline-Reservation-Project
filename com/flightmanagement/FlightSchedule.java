package com.flightmanagement;

import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.geometry.*;

public class FlightSchedule {
    public int maxSize = 100;
    public Flight[] flightList;
    public int flightCount = 0;

    public FlightSchedule() {
        flightList = new Flight[maxSize];
    }

    public void addFlight(Flight f) {
        boolean replaced = false;
        for (int i=0; i<flightCount; i++) {
            if (flightList[i] != null && flightList[i].status.equals("Cancelled")) {
                flightList[i] = f;
                replaced = true;
                return;
            }
        }

        if (!replaced) {
            flightList[flightCount++] = f;
        }
    }

    public GridPane viewFlights() {
        GridPane flightSchedViewGrid = new GridPane();
        flightSchedViewGrid.setAlignment(Pos.CENTER);
        flightSchedViewGrid.setHgap(10);
        flightSchedViewGrid.setVgap(10);

        String[] headers = {"Flight ID", "Origin", "Destination", "Date", "Total Seats", "Status", "Economy Price", "Business Price", "First Price", "Residence Price"};
        for (int i = 0; i < headers.length; i++) {
            flightSchedViewGrid.add(new Label(headers[i]), i, 2);
        }

        // Add each flight's details to the GridPane
        for (int i = 0; i < flightCount; i++) {
            Flight flight = flightList[i];
            if (flight != null) {
                flightSchedViewGrid.add(new Label(flight.flightId), 0, i + 3);
                flightSchedViewGrid.add(new Label(flight.origin), 1, i + 3);
                flightSchedViewGrid.add(new Label(flight.destination), 2, i + 3);
                flightSchedViewGrid.add(new Label(flight.date.toString()), 3, i + 3);
                flightSchedViewGrid.add(new Label(String.valueOf(flight.totalSeats)), 4, i + 3);
                flightSchedViewGrid.add(new Label(flight.status), 5, i + 3);
                flightSchedViewGrid.add(new Label(flight.displayPrice("Economy")), 6, i + 3);
                flightSchedViewGrid.add(new Label(flight.displayPrice("Business")), 7, i + 3);
                flightSchedViewGrid.add(new Label(flight.displayPrice("First")), 8, i + 3);
                flightSchedViewGrid.add(new Label(flight.displayPrice("Residence")), 9, i + 3);
            }
        }

        return flightSchedViewGrid;
    }
}