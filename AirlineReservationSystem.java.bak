import com.catering.*;
import com.dutyfree.*;
import com.exceptions.*;
import com.flightmanagement.*;
import com.users.*;
import java.util.Scanner;

// Suppress warnings about resource leaks (e.g., not closing the Scanner)
@SuppressWarnings("resource")
class AirlineReservationSystem {
    public static void main(String[] args) {
        // Create a Scanner to read user input
        Scanner scanner = new Scanner(System.in);

        // Initialize objects for flight schedule, duty-free management, catering menu management, and flight report
        FlightSchedule flightSchedule = new FlightSchedule();
        DutyFreeManagement dutyFreeManagement = new DutyFreeManagement();
        CateringMenuManagement cateringMenuManagement = new CateringMenuManagement();
        FlightReport flightReport = new FlightReport(flightSchedule);

        // Main loop to handle user interactions
        while (true) {
            // Display menu options
            System.out.println("1. Traveler Login");
            System.out.println("2. Manager Login");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");

            // Read user's choice
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline left-over

            // Handle user's choice
            switch (choice) {
                case 1:
                    // Traveler login
                    System.out.print("Enter Traveler Username: ");
                    String travelerUsername = scanner.nextLine();
                    System.out.print("Enter Traveler Password: ");
                    String travelerPassword = scanner.nextLine();
                    Traveler traveler = new Traveler(travelerUsername, travelerPassword, flightSchedule);
                    try {
                        // Start the traveler's thread
                        traveler.t.join();
                    } catch (InterruptedException e) {}  // Ignore InterruptedException
                    break;

                case 2:
                    // Manager login
                    System.out.print("Enter Manager Username: ");
                    String managerUsername = scanner.nextLine();
                    System.out.print("Enter Manager Password: ");
                    String managerPassword = scanner.nextLine();
                    // Simple check for manager credentials (can be expanded to check from a list or database)
                    if (managerUsername.equals("admin") && managerPassword.equals("Admin$123")) {
                        Manager manager = new Manager(managerUsername, managerPassword, flightSchedule, cateringMenuManagement, dutyFreeManagement, flightReport);
                        manager.displayInfo();
                        System.out.println();
                        manager.menu();  // Display manager's menu
                    } else {
                        System.out.println("Invalid Manager Username or Password. Try again.");
                    }
                    break;

                case 3:
                    // Exit the system
                    System.out.println("Exiting...");
                    return;

                default:
                    try {
                        // Throw an exception for invalid menu options
                        throw new InvalidChoiceException("Invalid Menu Option. Please choose again.");
                    } catch (InvalidChoiceException e) {
                        System.out.println(e.getMessage());
                    }
            }
        }
    }
}