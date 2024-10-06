import java.util.Scanner;

// Custom exceptions
class ExceededMaxSizeException extends Exception {
    public ExceededMaxSizeException(String msg) {
        super(msg);
    }
}

class InvalidChoiceException extends Exception {
    public InvalidChoiceException(String msg) {
        super(msg);
    }
}

// User classes
abstract class User {
    String loginID, password;

    public User(String loginID, String password) {
        this.loginID = loginID;
        this.password = password;
    }

    abstract void displayInfo();
}

class Traveler extends User {
    public Traveler(String loginID, String password) {
        super(loginID, password);
    }

    @Override
    public void displayInfo() {
        System.out.println("Welcome " + loginID + "!\n");
    }
}

class Manager extends User {
    public Manager(String loginID, String password) {
        super(loginID, password);
    }

    @Override
    public void displayInfo() {
        System.out.println("Welcome, Manager " + loginID + "!");
        System.out.println("You have administrator permissions.");
    }

    // Add flight method
    public void addFlight(FlightSchedule flightSchedule) {
        Scanner sc = new Scanner(System.in);
        Flight flight = new Flight();

        System.out.print("Flight ID: ");
        flight.flightId = sc.nextLine();

        System.out.print("Origin: ");
        flight.origin = sc.nextLine();

        System.out.print("Destination: ");
        flight.destination = sc.nextLine();

        System.out.print("Total Seats: ");
        flight.totalSeats = sc.nextInt();
        sc.nextLine(); // Consume newline

        System.out.print("Catering Availability (true/false): ");
        flight.cateringAvailable = sc.nextBoolean();
        sc.nextLine(); // Consume newline

        System.out.print("Duty-Free Availability (true/false): ");
        flight.dutyFreeAvailable = sc.nextBoolean();
        sc.nextLine(); // Consume newline

        System.out.print("Status: ");
        flight.status = sc.nextLine();

        System.out.print("Number of Economy Seats: ");
        flight.economySeats = sc.nextInt();
        System.out.print("Seat Price for Economy: ");
        flight.economySeatPrice = sc.nextDouble();

        System.out.print("Number of Business Seats: ");
        flight.businessSeats = sc.nextInt();
        System.out.print("Seat Price for Business: ");
        flight.businessSeatPrice = sc.nextDouble();

        System.out.print("Number of First Seats: ");
        flight.firstSeats = sc.nextInt();
        System.out.print("Seat Price for First: ");
        flight.firstSeatPrice = sc.nextDouble();

        // Setting Residence seat to 1 by default
        flight.residenceSeats = 1;

        // Get price for Residence class ticket
        System.out.print("Seat Price for Residence: ");
        flight.residenceSeatPrice = sc.nextDouble();

        // Add the flight to the schedule
        try {
            flightSchedule.addFlight(flight);
        } catch (ExceededMaxSizeException e) {
            System.out.println(e.getMessage());
        }
    }

    // Update flight method
    public void updateFlight(FlightSchedule sch, Flight flight) throws InvalidChoiceException {
        Scanner sc = new Scanner(System.in);
        System.out.println("\n1. Flight ID \n2. Origin \n3. Destination \n4. Total Seats \n5. Ticket Price \n6. Catering Availability \n7. Status");
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
                System.out.println("Current Origin: " + flight.origin);
                System.out.print("New Origin: ");
                flight.origin = sc.nextLine();
                System.out.println("Origin updated successfully!");
                break;
            case 3:
                System.out.println("Current Destination: " + flight.destination);
                System.out.print("New Destination: ");
                flight.destination = sc.nextLine();
                System.out.println("Destination updated successfully!");
                break;
            case 4:
                System.out.println("Current Total Seats: " + flight.totalSeats);
                System.out.print("New Total Seats: ");
                flight.totalSeats = sc.nextInt();
                sc.nextLine(); // Consume newline
                System.out.println("Total seats updated successfully!");
                break;
            case 5:
                System.out.println("Current Economy Seat Price: " + flight.economySeatPrice);
                System.out.print("New Economy Seat Price: ");
                flight.economySeatPrice = sc.nextDouble();
                System.out.println("Economy seat price updated successfully!");
                break;
            case 6:
                System.out.println("Current Catering Availability: " + flight.cateringAvailable);
                System.out.print("New Catering Availability: ");
                flight.cateringAvailable = sc.nextBoolean();
                sc.nextLine(); // Consume newline
                System.out.println("Catering availability updated successfully!");
                if (flight.cateringAvailable) {
                    flight.addCateringMenu("Economy");
                    flight.addCateringMenu("Business");
                    flight.addCateringMenu("First");
                }
                break;
            case 7:
                System.out.println("Current Status: " + flight.status);
                System.out.print("New Status: ");
                flight.status = sc.nextLine();
                System.out.println("Status updated successfully!");
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
}

// Flight Type Enum
enum FlightType {
    DOMESTIC, INTERNATIONAL
}

// Flight class
class Flight {
    String flightId;
    String origin;
    String destination;
    int totalSeats;
    boolean cateringAvailable;
    boolean dutyFreeAvailable;
    String status;
    int economySeats;
    double economySeatPrice;
    int businessSeats;
    double businessSeatPrice;
    int firstSeats;
    double firstSeatPrice;
    int residenceSeats;
    double residenceSeatPrice; // Add residence seat price

    CateringMenuItem[] cateringMenu = new CateringMenuItem[10]; // Assuming a max of 10 menu items

    public void addCateringMenu(String classType) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter Dish Name: ");
        String name = sc.nextLine();
        System.out.print("Enter Dish Type (Starter, Main, Dessert, Beverage): ");
        String type = sc.nextLine();
        System.out.print("Enter Allergens: ");
        String allergens = sc.nextLine();

        for (int i = 0; i < cateringMenu.length; i++) {
            if (cateringMenu[i] == null) {
                cateringMenu[i] = new CateringMenuItem(name, type, allergens, classType);
                System.out.println(name + " added to the catering menu for " + classType + ".");
                break;
            }
        }
    }

    public void displayCateringMenu(FlightSchedule flightSchedule) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter Flight ID to view catering menu: ");
        String flightId = sc.nextLine();

        Flight flightToViewMenu = null;
        for (Flight flight : flightSchedule.flightList) {
            if (flight != null && flight.flightId.equals(flightId)) {
                flightToViewMenu = flight;
                break;
            }
        }

        if (flightToViewMenu != null) {
            System.out.println("Select Class Type:");
            System.out.println("1. Economy");
            System.out.println("2. Business");
            System.out.println("3. First");
            System.out.print("Choose an option: ");
            int choice = sc.nextInt();
            sc.nextLine(); // Consume newline

            String classType = "";
            switch (choice) {
                case 1:
                    classType = "Economy";
                    break;
                case 2:
                    classType = "Business";
                    break;
                case 3:
                    classType = "First";
                    break;
                default:
                    System.out.println("Invalid choice! No menu will be displayed.");
                    return;
            }

            // Print the catering menu for the selected class
            System.out.println("\nCatering Menu for " + classType + ":");
            flightToViewMenu.printCateringMenu(classType);
        } else {
            System.out.println("Flight not found.");
        }
    }
}

class CateringMenuItem {
    String name;
    String type;
    String allergens;
    String classType;

    public CateringMenuItem(String name, String type, String allergens, String classType) {
        this.name = name;
        this.type = type;
        this.allergens = allergens;
        this.classType = classType;
    }
}

class FlightSchedule {
    public int maxSize = 100; // Moved maxSize inside the class
    Flight[] flightList;
    private int flightCount = 0;

    public FlightSchedule() {
        flightList = new Flight[maxSize];
    }

    public void addFlight(Flight f) throws ExceededMaxSizeException {
        if (flightCount >= maxSize) {
            throw new ExceededMaxSizeException("Too many flights. Use a different schedule object.");
        } else {
            flightList[flightCount++] = f;
            System.out.println("Successfully added flight " + f.flightId + " to the schedule.");
        }
    }

    public void viewFlights() {
    System.out.printf("%-15s %-15s %-15s %-15s %-15s %-15s %-15s %-15s %-15s\n", 
                      "Flight ID", "Origin", "Destination", "Total Seats", "Status", 
                      "Economy Price", "Business Price", "First Price", "Residence Price");
    System.out.println("-----------------------------------------------------------------------------------------------------------------------");
    for (Flight flight : flightList) {
        if (flight != null) {
            System.out.printf("%-15s %-15s %-15s %-15d %-15s %.2f            %.2f            %.2f            %.2f\n", 
                              flight.flightId, flight.origin, flight.destination, 
                              flight.totalSeats, flight.status, 
                              flight.economySeatPrice, flight.businessSeatPrice, 
                              flight.firstSeatPrice, flight.residenceSeatPrice);
        }
    }
}

}

// Main class
public class Main {
    public static void main(String[] args) {
        AirlineReservationSystem system = new AirlineReservationSystem();
        Manager manager = system.new Manager("admin", "password");
        FlightSchedule schedule = system.new FlightSchedule();

        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("\nMenu: ");
            System.out.println("1. Add Flight");
            System.out.println("2. Update Flight");
            System.out.println("3. Delete Flight");
            System.out.println("4. View Flights");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    manager.addFlight(schedule);
                    break;
                case 2:
                    System.out.print("Enter Flight ID to update: ");
                    String flightId = scanner.next();
                    Flight flightToUpdate = null;
                    for (Flight flight : schedule.flightList) {
                        if (flight != null && flight.flightId.equals(flightId)) {
                            flightToUpdate = flight;
                            break;
                        }
                    }
                    if (flightToUpdate != null) {
                        try {
                            manager.updateFlight(schedule, flightToUpdate);
                        } catch (InvalidChoiceException e) {
                            System.out.println(e.getMessage());
                        }
                    } else {
                        System.out.println("Flight not found.");
                    }
                    break;
                case 3:
                    manager.deleteFlight(schedule);
                    break;
                case 4:
                    schedule.viewFlights();
                    break;
                case 5:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        scanner.close();
    }
}
