package com.users;

import javafx.stage.*;

import java.util.Scanner;
import com.flightmanagement.*;
import com.exceptions.InvalidChoiceException;


public class Traveler extends User implements Runnable {
    public TravelerApplication app = new TravelerApplication(this);
    public Thread t;
    Scanner scanner = new Scanner(System.in);
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
        t = new Thread(this, loginID);
        // t.start();
    }

    @Override
    public void displayInfo() {
        System.out.println("Welcome " + loginID + "!\n");
        if (email!=null) {
            System.out.println("Email: "+email);
            System.out.println("Contact Number: "+contactNo);
        }
    }

    private void inputDetails() {
        if (email!=null) {
            System.out.println("Email already exists");
            displayInfo();
            System.out.println("Do you want to update your details?");
            System.out.println("1. Yes");
            System.out.println("2. No");
            System.out.print("Enter choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            if (choice != 1) {
                return;
            }
        }
        System.out.print("Enter Email ID: ");
        email = scanner.nextLine();
        System.out.print("Enter contact number: ");
        contactNo = scanner.nextLine();
    }

    private void genInvoice() {
        System.out.println("========================================");
        System.out.println("                INVOICE                 ");
        System.out.println("========================================");
        System.out.printf("%-15s %s\n", "Flight ID: "+booked.flightId, "Class: "+classChoice);
        System.out.printf("%-15s\n", "Departure Date: "+booked.date);
        System.out.printf("%-15s %s\n", "No of seats booked: "+noOfSeats, "Singular Seat Price: "+bookedSeatPrice);
        double subTotal =  bookedSeatPrice * noOfSeats;
        double serviceTax = 0.05*subTotal;
        System.out.printf("%-15s $%.2f\n",  "Sub Total: ", subTotal);
        System.out.printf("%-15s $%.2f\n",  "Service Tax: ", serviceTax);
        System.out.printf("%-15s $%.2f\n", "Total:  ", subTotal+serviceTax);
        System.out.println("========================================");
    }

    synchronized private void bookFlight() throws InvalidChoiceException {
        System.out.println("FLIGHT BOOKING");
        System.out.println("Enter Date of Travel: ");
        DATE d = new DATE();
        System.out.print("Enter day: ");
        d.day = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter month: ");
        d.month = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter year: ");
        d.year  = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter origin: ");
        String origin = scanner.nextLine();
        System.out.print("Enter destination: ");
        String destination = scanner.nextLine();
        filtered = new FlightSchedule();

        for (Flight f : mainFlightSchedule.flightList) {
            if (f != null && f.origin.equals(origin) && f.destination.equals(destination) && f.date.equals(d)) {
                filtered.addFlight(f);
            }
        }

        if (filtered.flightList[0]==null) {
            throw new InvalidChoiceException("No flights found.");
        }

        System.out.println("Available flights:\n");
        filtered.viewFlights();

        System.out.print("Select flight (Flight ID): ");
        String flightChoice = scanner.nextLine();
        boolean exists = false;
        int index;
        for (index=0; index<mainFlightSchedule.flightList.length; index++) {
            Flight f = mainFlightSchedule.flightList[index];
            if (f != null && f.flightId.equals(flightChoice)) {
                exists=true;
                break;
            }
        }

        if (!exists) {
            throw new InvalidChoiceException("Flight does not exist.");
        }

        Flight selected = filtered.flightList[index];
        System.out.print("Enter travel class (Economy/Business/First/Residence): ");
        classChoice = scanner.nextLine();
        if (!classChoice.equals("Economy") && !classChoice.equals("Business") && !classChoice.equals("First") && !classChoice.equals("Residence")) {
            throw new InvalidChoiceException("Invalid choice of traveling class.");
        }

        if (selected.getVacantSeats(classChoice)<=0) {
            throw new InvalidChoiceException("No seats available for the selected class.");
        }

        System.out.println("Price of each seat: "+selected.displayPrice(classChoice));

        System.out.print("Enter number of seats to book: ");
        noOfSeats = scanner.nextInt();
        scanner.nextLine();
        bookedSeatPrice = selected.getPrice(classChoice);
        
        if (noOfSeats>selected.getVacantSeats(classChoice)) {
            System.out.println("Not enough vacant seats on flight.");
        } else {
            selected.updateVacantSeats(classChoice, -1*(noOfSeats));
            System.out.println("Successfully booked "+noOfSeats+" "+classChoice+" class seats!");
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {}

        if (selected.cateringAvailable) {
            System.out.println("\nCatering is available on your flight!");
            System.out.println("The catering menu is as follows, please ask our staff to purchase any item(s)!");
            selected.printCateringMenu();
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {}

        if (selected.dutyFreeAvailable) {
            System.out.println("\nDuty-Free items are available for your flight!");
            System.out.println("Duty-Free items are as follows, please ask our staff to purchase any item(s)!");
            selected.printDutyFreeItems();
            System.out.println();
        }
        booked = selected;
        genInvoice();
    }

    private void cancelBooking() {
        System.out.println("Are you sure you want to cancel your booking for flight "+booked.flightId+" from "+booked.origin+" to "+booked.destination+"?");
        System.out.print("Please enter 'yes' to confirm cancellation: ");
        @SuppressWarnings("resource")
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        if (input.equalsIgnoreCase("yes")) {
            System.out.println("Booking cancelled successfully!");
            booked.updateVacantSeats(classChoice, noOfSeats);
            booked = null;
            noOfSeats=0;
        } else {
            System.out.println("Booking not cancelled.");
        }
    }

    public Flight getBooked() {
        return booked;
    }

    public void menu() {
        boolean travelerMenu = true;
        while (travelerMenu) {
            System.out.println("\n1. Input Personal Details");
            System.out.println("2. View Flight Schedule");
            System.out.println("3. Book a Flight");
            System.out.println("4. Cancel Booking");
            System.out.println("5. Logout");
            System.out.print("Choose an option: ");
            int travelerChoice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (travelerChoice) {
                case 1:
                    inputDetails();
                    break;
                case 2:
                    mainFlightSchedule.viewFlights();
                    break;
                case 3:
                    try {
                        bookFlight();
                    } catch (InvalidChoiceException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 4:
                    cancelBooking();
                    break;
                case 5:
                    travelerMenu = false;
                    break;
                default:
                    try {
                        throw new InvalidChoiceException("Invalid option. Please choose again.");
                    } catch (InvalidChoiceException e) {
                        System.out.println(e.getMessage());
                    }
            }
        }
    }

    @Override
    public void run() {
        // displayInfo();
        // menu();
    }

    public void launcher() {
        Stage stage = new Stage();
        app.start(stage);
    }
}
