package com.users;

import javafx.stage.*;

import com.flightmanagement.*;
import com.exceptions.InvalidChoiceException;


public class Traveler extends User implements Runnable {
    public TravelerApplication app = new TravelerApplication(this);
    // public Thread t;
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
        // t = new Thread(this, loginID);
        // t.start();
    }

    @Override
    public void run() {
        //TODO: Can we even make these multi-threaded?
    }

    //####################################### JAVAFX IMPLEMENTATION PROGRAM BEGINS HERE ###############################################

    public void filter(DATE DoT, String origin, String destination) throws InvalidChoiceException {
        filtered = new FlightSchedule();
        for (Flight f : mainFlightSchedule.flightList) {
            if (f != null && f.origin.equalsIgnoreCase(origin) && f.destination.equalsIgnoreCase(destination) && f.date.equals(DoT)) {
                filtered.addFlight(f);
            }
        }

        if (filtered.flightList[0]==null) {
            throw new InvalidChoiceException("No flights found.");
        }
    }

    @Override
    public void launcher() {
        Stage stage = new Stage();
        app.start(stage);
    }
}
