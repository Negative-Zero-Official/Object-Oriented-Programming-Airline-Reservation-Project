package com.flightmanagement;
import com.catering.*;
import com.dutyfree.*;

public class Flight {
    public String flightId;
    public String origin;
    public String destination;
    public DATE date = new DATE();
    public FlightType type;
    public int totalSeats;
    public boolean cateringAvailable;
    public boolean dutyFreeAvailable;
    public String status;
    public int economySeats;
    public int vacantEconomySeats;
    public double economySeatPrice;
    public int businessSeats;
    public int vacantBusinessSeats;
    public double businessSeatPrice;
    public int firstSeats;
    public int vacantFirstSeats;
    public double firstSeatPrice;
    public int residenceSeats;
    public int vacantResidenceSeats;
    public double residenceSeatPrice;
    public CateringMenuManagement cateringMenu = new CateringMenuManagement();
    public DutyFreeManagement DutyFree = new DutyFreeManagement();

    //Default Constructor
    public Flight() {}

    //Constructor to add hard coded flights for testing
    public Flight(String flightId, String origin, String destination, DATE date, FlightType type, 
                int totalSeats, boolean cateringAvailable, boolean dutyFreeAvailable, 
                int economySeats, double economySeatPrice, 
                int businessSeats, double businessSeatPrice, 
                int firstSeats, double firstSeatPrice, 
                int residenceSeats, double residenceSeatPrice) {
        this.flightId = flightId;
        this.origin = origin;
        this.destination = destination;
        this.date = date; // Assuming DATE has a constructor that can take day, month, year
        this.type = type;
        this.totalSeats = totalSeats;
        this.cateringAvailable = cateringAvailable;
        this.dutyFreeAvailable = dutyFreeAvailable;
        
        // Initialize seat counts and prices
        this.economySeats = economySeats;
        this.vacantEconomySeats = economySeats; // Initially, all economy seats are vacant
        this.economySeatPrice = economySeatPrice;

        this.businessSeats = businessSeats;
        this.vacantBusinessSeats = businessSeats; // Initially, all business seats are vacant
        this.businessSeatPrice = businessSeatPrice;

        this.firstSeats = firstSeats;
        this.vacantFirstSeats = firstSeats; // Initially, all first class seats are vacant
        this.firstSeatPrice = firstSeatPrice;

        this.residenceSeats = residenceSeats;
        this.vacantResidenceSeats = residenceSeats; // Initially, all residence seats are vacant
        this.residenceSeatPrice = residenceSeatPrice;

        // Set the initial status of the flight (optional, can be modified as needed)
        this.status = "Scheduled"; // Default status
    }

    public String displayPrice(String classType) {
        switch(classType) {
            case "Economy":
                return "$" + String.format("%.2f", economySeatPrice);
            case "Business":
                return "$" + String.format("%.2f", businessSeatPrice);
            case "First":
                return "$" + String.format("%.2f", firstSeatPrice);
            case "Residence":
                return "$" + String.format("%.2f", residenceSeatPrice);
            default:
                return "";
        }
    }

    public double getPrice(String classType) {
        switch (classType) {
            case "Economy":
                return economySeatPrice;
            case "Business":
                return businessSeatPrice;
            case "First":
                return firstSeatPrice;
            case "Residence":
                return residenceSeatPrice;
            default:
                return -1;
        }
    }

    public int getVacantSeats(String classType) {
        switch (classType) {
            case "Economy":
                return vacantEconomySeats;
            case "Business":
                return vacantBusinessSeats;
            case "First":
                return vacantFirstSeats;
            case "Residence":
                return vacantResidenceSeats;
            default:
                return -1;
        }
    }

    public int getVacantSeats() {
        return vacantEconomySeats  + vacantBusinessSeats + vacantFirstSeats + vacantResidenceSeats;

    }

    public void updateVacantSeats(String classType, int change) {
        switch (classType) {
            case "Economy":
                vacantEconomySeats += change;
                economySeatPrice = economySeatPrice / (1 + (change / (double)economySeats)*0.1);
                break;
            case "Business":
                vacantBusinessSeats += change;
                businessSeatPrice = businessSeatPrice / (1 + (change / (double)businessSeats)*0.1);
                break;
            case "First":
                vacantFirstSeats += change;
                firstSeatPrice = firstSeatPrice / (1 + (change / (double)firstSeats)*0.1);
                break;
            case "Residence":
                vacantResidenceSeats += change;
                residenceSeatPrice = residenceSeatPrice / (1 + (change / (double)residenceSeats)*0.1);
                break;
        }
    }

    public int getTotalSeats(String classType) {
        switch (classType) {
            case "Economy":
                return economySeats;
            case "Business":
                return businessSeats;
            case "First":
                return firstSeats;
            case "Residence":
                return residenceSeats;
            default:
                return -1;
        }
    }

    public String getDate() {
        return date.toString();
    }

    public void fill() {
        vacantEconomySeats = economySeats;
        vacantBusinessSeats = businessSeats;
        vacantFirstSeats = firstSeats;
        vacantResidenceSeats = residenceSeats;
        totalSeats = economySeats + businessSeats + firstSeats + residenceSeats;
    }

    @Override
    public String toString() {
        return flightId;
    }
}