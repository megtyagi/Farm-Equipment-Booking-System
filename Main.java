import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static final BookingManager manager = new BookingManager();
    private static final FileManager fileManager = new FileManager();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            fileManager.loadRecords(manager);
            System.out.println("Records loaded successfully.");
        } catch (Exception exception) {
            System.out.println("Could not load saved records: " + exception.getMessage());
        }

        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Choose an option: ");

            try {
                switch (choice) {
                    case 1 -> addEquipment();
                    case 2 -> viewEquipment();
                    case 3 -> registerFarmer();
                    case 4 -> viewFarmers();
                    case 5 -> bookEquipment();
                    case 6 -> viewBookings();
                    case 7 -> cancelBooking();
                    case 8 -> checkAvailability();
                    case 9 -> saveRecords();
                    case 10 -> running = exitApplication();
                    default -> System.out.println("Invalid option. Please choose 1 to 10.");
                }
            } catch (Exception exception) {
                System.out.println("Error: " + exception.getMessage());
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n=== Farm Equipment Booking System ===");
        System.out.println("1. Add farm equipment");
        System.out.println("2. View farm equipment");
        System.out.println("3. Register farmer");
        System.out.println("4. View farmers");
        System.out.println("5. Book equipment");
        System.out.println("6. View bookings");
        System.out.println("7. Cancel booking");
        System.out.println("8. Check date availability");
        System.out.println("9. Save records");
        System.out.println("10. Save and exit");
    }

    private static void addEquipment() {
        System.out.println("Equipment type:");
        System.out.println("1. Tractor");
        System.out.println("2. Harvester");
        System.out.println("3. General Farm Machine");
        int type = readInt("Choose type: ");

        System.out.print("Name: ");
        String name = readText();
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty.");
        }
        double rate = readDouble("Rental rate per day: Rs.");
        if (rate <= 0) {
            throw new IllegalArgumentException("Rental rate must be greater than zero.");
        }

        int id = manager.getNextEquipmentId();
        Equipment equipment;
        if (type == 1) {
            equipment = new Tractor(id, name, rate);
        } else if (type == 2) {
            equipment = new Harvester(id, name, rate);
        } else if (type == 3) {
            equipment = new FarmMachine(id, name, rate, "Farm Machine");
        } else {
            throw new IllegalArgumentException("Invalid equipment type.");
        }

        manager.addEquipment(equipment);
        System.out.println("Equipment added: " + equipment);
    }

    private static void viewEquipment() {
        ArrayList<Equipment> equipmentList = manager.getEquipmentList();
        if (equipmentList.isEmpty()) {
            System.out.println("No equipment has been added.");
            return;
        }
        System.out.println("\n--- Farm Equipment ---");
        for (Equipment equipment : equipmentList) {
            System.out.println(equipment);
        }
    }

    private static void registerFarmer() {
        System.out.print("Farmer name: ");
        String name = readText();
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty.");
        }
        System.out.print("Phone number: ");
        String phone = readText();
        if (phone.isEmpty()) {
            throw new IllegalArgumentException("Phone number cannot be empty.");
        }

        Farmer farmer = new Farmer(manager.getNextFarmerId(), name, phone);
        manager.registerFarmer(farmer);
        System.out.println("Farmer registered: " + farmer);
    }

    private static void viewFarmers() {
        if (manager.getFarmerList().isEmpty()) {
            System.out.println("No farmers have been registered.");
            return;
        }
        System.out.println("\n--- Registered Farmers ---");
        for (Farmer farmer : manager.getFarmerList()) {
            System.out.println(farmer);
        }
    }

    private static void bookEquipment() {
        if (manager.getFarmerList().isEmpty()) {
            throw new IllegalArgumentException("Register a farmer before making a booking.");
        }
        if (manager.getEquipmentList().isEmpty()) {
            throw new IllegalArgumentException("Add equipment before making a booking.");
        }

        viewFarmers();
        int farmerId = readInt("Enter farmer ID: ");
        Farmer farmer = manager.findFarmerById(farmerId);
        if (farmer == null) {
            throw new IllegalArgumentException("Farmer not found.");
        }

        viewEquipment();
        int equipmentId = readInt("Enter equipment ID: ");
        Equipment equipment = manager.findEquipmentById(equipmentId);
        if (equipment == null) {
            throw new IllegalArgumentException("Equipment not found.");
        }

        LocalDate startDate = readDate("Start date (YYYY-MM-DD): ");
        LocalDate endDate = readDate("End date (YYYY-MM-DD): ");

        Booking booking = manager.bookEquipment(
                farmer, equipment, startDate, endDate);
        System.out.println("Booking created successfully:");
        System.out.println(booking);
    }

    private static void viewBookings() {
        if (manager.getBookingList().isEmpty()) {
            System.out.println("No bookings have been made.");
            return;
        }
        System.out.println("\n--- Bookings ---");
        for (Booking booking : manager.getBookingList()) {
            System.out.println(booking);
        }
    }

    private static void cancelBooking() {
        viewBookings();
        if (manager.getBookingList().isEmpty()) {
            return;
        }
        int bookingId = readInt("Enter booking ID to cancel: ");
        if (manager.cancelBooking(bookingId)) {
            System.out.println("Booking cancelled successfully.");
        } else {
            System.out.println("Booking not found or already cancelled.");
        }
    }

    private static void checkAvailability() {
        viewEquipment();
        if (manager.getEquipmentList().isEmpty()) {
            return;
        }
        int equipmentId = readInt("Enter equipment ID: ");
        Equipment equipment = manager.findEquipmentById(equipmentId);
        if (equipment == null) {
            throw new IllegalArgumentException("Equipment not found.");
        }

        LocalDate startDate = readDate("Start date (YYYY-MM-DD): ");
        LocalDate endDate = readDate("End date (YYYY-MM-DD): ");
        if (manager.isDateAvailable(equipment, startDate, endDate)) {
            System.out.println("Equipment is available for that date range.");
        } else {
            System.out.println("Equipment is NOT available for that date range.");
        }
    }

    private static void saveRecords() {
        try {
            fileManager.saveRecords(manager);
            System.out.println("Records saved successfully in the data folder.");
        } catch (Exception exception) {
            System.out.println("Could not save records: " + exception.getMessage());
        }
    }

    private static boolean exitApplication() {
        saveRecords();
        System.out.println("Thank you for using Farm Equipment Booking System.");
        return false;
    }

    private static int readInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException exception) {
                System.out.println("Please enter a whole number.");
            }
        }
    }

    private static double readDouble(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException exception) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private static LocalDate readDate(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return LocalDate.parse(scanner.nextLine().trim());
            } catch (DateTimeParseException exception) {
                System.out.println("Please enter a valid date in YYYY-MM-DD format.");
            }
        }
    }

    private static String readText() {
        return scanner.nextLine().trim();
    }
}
