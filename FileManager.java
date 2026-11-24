import java.io.File;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

public class FileManager {
    private static final String DATA_FOLDER = "data";
    private static final String EQUIPMENT_FILE = DATA_FOLDER + File.separator + "equipment.txt";
    private static final String FARMER_FILE = DATA_FOLDER + File.separator + "farmers.txt";
    private static final String BOOKING_FILE = DATA_FOLDER + File.separator + "bookings.txt";

    public void saveRecords(BookingManager manager) throws IOException {
        File folder = new File(DATA_FOLDER);
        if (!folder.exists() && !folder.mkdirs()) {
            throw new IOException("Could not create data folder.");
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(EQUIPMENT_FILE))) {
            for (Equipment equipment : manager.getEquipmentList()) {
                writer.write(equipment.getEquipmentType() + "|" + equipment.getEquipmentId()
                        + "|" + equipment.getName() + "|" + equipment.getRentalRatePerDay());
                writer.newLine();
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FARMER_FILE))) {
            for (Farmer farmer : manager.getFarmerList()) {
                writer.write(farmer.getFarmerId() + "|" + farmer.getName()
                        + "|" + farmer.getPhoneNumber());
                writer.newLine();
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(BOOKING_FILE))) {
            for (Booking booking : manager.getBookingList()) {
                writer.write(booking.getBookingId() + "|"
                        + booking.getFarmer().getFarmerId() + "|"
                        + booking.getEquipment().getEquipmentId() + "|"
                        + booking.getStartDate() + "|" + booking.getEndDate() + "|"
                        + booking.isCancelled());
                writer.newLine();
            }
        }
    }

    public void loadRecords(BookingManager manager) throws IOException {
        manager.setEquipmentList(loadEquipment());
        manager.setFarmerList(loadFarmers());
        loadBookings(manager);
        manager.updateAvailability();
    }

    private ArrayList<Equipment> loadEquipment() throws IOException {
        ArrayList<Equipment> equipmentList = new ArrayList<>();
        File file = new File(EQUIPMENT_FILE);
        if (!file.exists()) {
            return equipmentList;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] parts = line.split("\\|", -1);
                if (parts.length != 4) {
                    continue;
                }
                try {
                    String type = parts[0];
                    int id = Integer.parseInt(parts[1]);
                    String name = parts[2];
                    double rate = Double.parseDouble(parts[3]);
                    Equipment equipment;
                    if (type.equalsIgnoreCase("Tractor")) {
                        equipment = new Tractor(id, name, rate);
                    } else if (type.equalsIgnoreCase("Harvester")) {
                        equipment = new Harvester(id, name, rate);
                    } else {
                        equipment = new FarmMachine(id, name, rate, type);
                    }
                    equipmentList.add(equipment);
                } catch (NumberFormatException ignored) {
                    // Skip one malformed equipment record.
                }
            }
        }
        return equipmentList;
    }

    private ArrayList<Farmer> loadFarmers() throws IOException {
        ArrayList<Farmer> farmers = new ArrayList<>();
        File file = new File(FARMER_FILE);
        if (!file.exists()) {
            return farmers;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] parts = line.split("\\|", -1);
                if (parts.length != 3) {
                    continue;
                }
                try {
                    farmers.add(new Farmer(
                            Integer.parseInt(parts[0]), parts[1], parts[2]));
                } catch (NumberFormatException ignored) {
                    // Skip one malformed farmer record.
                }
            }
        }
        return farmers;
    }

    private void loadBookings(BookingManager manager) throws IOException {
        ArrayList<Booking> bookings = new ArrayList<>();
        File file = new File(BOOKING_FILE);
        if (!file.exists()) {
            manager.setBookingList(bookings);
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] parts = line.split("\\|", -1);
                if (parts.length != 6) {
                    continue;
                }
                try {
                    int bookingId = Integer.parseInt(parts[0]);
                    Farmer farmer = manager.findFarmerById(Integer.parseInt(parts[1]));
                    Equipment equipment = manager.findEquipmentById(Integer.parseInt(parts[2]));
                    LocalDate startDate = LocalDate.parse(parts[3]);
                    LocalDate endDate = LocalDate.parse(parts[4]);
                    if (farmer != null && equipment != null
                            && !endDate.isBefore(startDate)) {
                        Booking booking = new Booking(
                                bookingId, farmer, equipment, startDate, endDate);
                        if (Boolean.parseBoolean(parts[5])) {
                            booking.cancel();
                        }
                        bookings.add(booking);
                    }
                } catch (RuntimeException ignored) {
                    // Skip one malformed booking record.
                }
            }
        }
        manager.setBookingList(bookings);
    }
}
