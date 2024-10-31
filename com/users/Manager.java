package com.users;
import com.catering.CateringMenuManagement;
import com.dutyfree.DutyFreeManagement;
import com.exceptions.*;
import com.flightmanagement.*;
import java.util.Scanner;

@SuppressWarnings("resource")
public class Manager extends User {
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
        if (flightSchedule.flightList.length==flightSchedule.maxSize-1) {
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
    @Override
    public void launcher() {
        //TODO: Write the ManagerApplication Class
    }
}
